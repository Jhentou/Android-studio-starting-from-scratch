package com.example.sf.ch10camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Uri imgUri;
    ImageView imv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imv = (ImageView)findViewById(R.id.imageView);
    }
    public void onGet(View v){
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String fname = "p" + System.currentTimeMillis() + ".jpg";
        imgUri = Uri.parse("file://"+dir+"/"+fname);
        Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
        it.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
        startActivityForResult(it,100);
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case 100:
                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,imgUri);
                    sendBroadcast(it);
                    break;
                case 101:
                    imgUri = convertUri(data.getData());
                    break;
            }
            showImg();
        }
        else {
            Toast.makeText(this,"沒有拍到照片",Toast.LENGTH_LONG).show();
        }
    }
    void showImg(){
        int iw, ih, vw, vh;
        boolean needRotate;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUri.getPath(),options);
        iw = options.outWidth;
        ih = options.outHeight;
        vw = imv.getWidth();
        vh = imv.getHeight();

        int scaleFactor;
        if(iw<ih) {
            needRotate = false;
            scaleFactor = Math.min(iw / vw, ih / vh);
        }
        else{
            needRotate = true;
            scaleFactor = Math.min(ih/vw,iw/vh);
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath(),options);
        if(needRotate){
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
        }

        imv.setImageBitmap(bmp);

        new AlertDialog.Builder(this)
                .setTitle("圖檔資訊")
                .setMessage("圖檔路徑：" + imgUri.getPath() + "\n 原始尺寸：" + iw +"x"+ih +
                "\n 載入尺寸：" + bmp.getWidth() + "x" +bmp.getHeight() +
                "\n 顯示尺寸：" + vw + "x" + vh + (needRotate? "(旋轉)" : ""))
                .setNegativeButton("關閉",null)
                .show();
    }
    Uri convertUri(Uri uri){
        if(uri.toString().substring(0,7).equals("content")){
            String[] colName = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(uri,colName,null,null,null);
            cursor.moveToFirst();
            uri = Uri.parse("file://" + cursor.getString(0));
            cursor.close();
        }
        return uri;
    }
    public void onPick(View v){
        Intent it = new Intent(Intent.ACTION_GET_CONTENT);
        it.setType("image/*");
        startActivityForResult(it, 101);
    }
}
