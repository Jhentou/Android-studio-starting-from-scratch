package com.example.lee.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
        {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    //The entry point to Google Play services, used by the Places API and Fused Location Provider.
    //指向Google Play服務的入口點(?)，由Places API和使用融合提供定位
    private GoogleApiClient mGoogleApiClient;

    //A request object to store parameters for requests to the FusedLocationProviderApi0
    private LocationRequest mLocationRequest;

    //The desired interval for location updates. Updates may be more or less frequent.
    //給予位置更新所需的時間間隔
    private static final long update_interval_in_milliseconds = 10000; //每1000毫秒為1秒

    //The fastest rate for active location updates. Exact. Updates will never be more frequent than this value.
    //裝置活動位置最快更新時間間隔
    private static final long fastest_update_interval_in_milliseconds = update_interval_in_milliseconds / 2;

    //給予GPS初始位置，暨南大學校門口
    private final LatLng mDefaultLocation = new LatLng(23.950569, 120.935720);     //默認初始地圖位置
    private static final int DEFAULT_ZOOM = 15;                                 //默認初始地圖大小
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;    //請求精確位至訪問權限
    private boolean mLocationPermissionGranted;                                    //位置許可授予

    //The geographical location where the device is currently located.
    //抓取該裝置的當前位置
    private Location mCurrentLocation;

    //Keys for storing activity state.
    //確認鑰匙存取狀態
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    LocationManager mgr;
    TextView txv, txv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieve location and camera position from saved instance state.
        //檢索位址與地圖畫面的位置保存並儲存應用行為顯示狀態
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //Retrieve the content view that renders the map.
        //顯示地圖上標示內容物
        setContentView(R.layout.activity_maps);

        //Build the Play services client for use by the Fused Location Provider and the Places API.
        //建構Play服務客戶端，提供給Fused Location Provider與Places API使用
        buildGoogleApiClient();
        mGoogleApiClient.connect();

        txv = (TextView) findViewById(R.id.txv);
        txv2 = (TextView) findViewById(R.id.txv2);
        mgr = (LocationManager)getSystemService(LOCATION_SERVICE);
    }

    //Get device location and nearby places when th activity is restored after a pause.
    //當從暫停狀態恢復活動時去獲得該裝置的位置與附近的地點
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            getDeviceLocation();
        }
        updateMarkers();
    }

    //Stop location updates when the activity is no longer if focus,to reduce battery consumption.
    //當不再以裝置作為中心點時，減少電池消耗(?)
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    //Saves the state of the map when the activity is paused.
    //當裝置停止活動時保持當前地圖的狀態
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }

    //Gets the device's current location and builds the map
    //when the Google Play services client is successfully connected.
    //獲得該裝置當前位置後，並在Google Play服務客戶端成功連結後建構出地圖
    @Override
    public void onConnected(Bundle connectionHint) {
        getDeviceLocation();
        //Build the map.
        //構築地圖
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //Handles failure to connect to the Google Play services client.
    //處理無法連接到Google Play客戶端
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Refer to the reference doc for ConnectionResult to see what error codes might
        //be returned in onConnectionFailed.
        //回傳錯誤的代碼
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    //Handles suspension of the connection to the Google Play services client.
    //暫停Google Play服務客戶端連的處理
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    //Handles the callback when location changes.
    //當位置改變時，處理回傳
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateMarkers();

     //顯示當前使用者裝置的經緯度位置

        String str = "定位提供者："+location.getProvider();
        str+= String.format("\n緯度：%.5f\n經度：%.5f",
                location.getLatitude(),
                location.getLongitude());
        txv.setText(str);

    //與暨南大學車道守衛室為目標的距離

        double JiNan_Latitude = 23.950569;
        double JiNan_Longitude = 120.935720;
        double GPSdis = GPSdistance(location.getLatitude(),location.getLongitude(),JiNan_Latitude,JiNan_Longitude)/1000;
        String str2 = "暨大校園門口距離：";
        str2+= String.format("%.1f 公里",GPSdis) ;
        txv2.setText(str2);

    }

     //以下是設置"我的位置"右上角的按鈕的程式碼

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        //Turn on the My Location layer and the related control on the map.
        //開啟地圖"我的位置"的UI功能
        updateLocationUI();
        //Add markers for nearby places.
        //把附近的地點添加標記
        updateMarkers();

    //設定文字視窗的程式碼

        //Use a custom info window adapter to handle multiple lines of text in the info window contents.
        //使用 activity_main.xml(自訂文字視窗) 呈現訊息創建的文字訊息
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            public View getInfoWindow(Marker arg0) {
                return null;
            }

            public View getInfoContents(Marker marker) {
                //Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.activity_main, null);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        }
        );

        //Set the map's camera position to the current location of the device.
        //將地圖畫面位置設備設定為當前的位置
        //If the previous state was saved, set the position to the saved state.
        //如果保存了之前的狀態，就拿出來做當前的位置
        //If the current location is unknown, use a default position and zoom value.
        //如果當前的位置未知不明，就會使用初始畫面呈現
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mCurrentLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }


    //注意以下是用GPS與網路去搜尋附近店家的程式碼


        //Builds a GoogleApiClient.
        //建立GoogleApiClient
        //Uses the addApi0 method to request the Google Places API and Fused Location Provider.
        //使用addApi0方式去請求Google Places API與Fused Location Provider
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        createLocationRequest();
    }


    //Sets up the location request.
    //設置裝置位置的請求
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        //Sets the desired interval for active location updates. This interval is
        //inexact. You may not receive updates at all if no location sources are available,or
        //you may receive them slower than requested. You may also receive updates faster than
        //requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(update_interval_in_milliseconds);

        //Sets the fastest rate for active location updates. This interval is exact, and your
        //application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(fastest_update_interval_in_milliseconds);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void getDeviceLocation() {
        //Request location permission, so that we can get the location of the device.
        //請求位置權限，以便我們可以獲取設備的位置
        //The result of the permission request is handled by a callback,
        //允許請求的結果回傳處理
        //onRequestPermissionsResult.
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        //Get the best and most recent location of the advice, which may be null in rare
        //cases when a location is not available.
        //獲取建議的最佳和最近的位置，在極少數期況下，當位置不可用顯示空值
        //Also request regular updates about the device location.
        //定時更新設備的位置
        if (mLocationPermissionGranted) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }


    //Handles the result of the request for location permissions.
    //處理位置權限請求的結果
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permission[],
                                                           @NonNull int[] grantResults){
        mLocationPermissionGranted = false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    //Adds markers for places nearby the device and turns the My Location feature on or off,
    //provided location permission has been granted.
    //為設備附近的地點添加標記，並在已授予的地點權限的情況下打開或關閉"我的位置"功能
    private void updateMarkers(){
        if(mMap == null){
            return;
        }

        if (mLocationPermissionGranted) {
            //Get the businesses and other points of interest located
            //nearest to the device's current location.
            //獲取距離設備當前位置附近的商店和其他景點
            @SuppressWarnings("MissingPermission")
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient,null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    for (PlaceLikelihood placelikelihood : likelyPlaces){
                        //Add a marker for each place near the device's current location, with an
                        //info window showing place information.
                        //在設備當前的位置附近每個地點添加標記，並在之前 activity_main.xml自訂視窗中顯示該地點的訊息
                        String attributions = (String) placelikelihood.getPlace().getAttributions();
                        String snippet = (String) placelikelihood.getPlace().getAddress();
                        if (attributions != null){
                            snippet = snippet + "\n" + attributions;
                        }

                        mMap.addMarker(new MarkerOptions()
                                .position(placelikelihood.getPlace().getLatLng())
                                .title((String) placelikelihood.getPlace().getName())
                                .snippet(snippet));
                    }

                    //Release the place likelihood buffer.
                    //釋放可能性來源的緩衝區
                    likelyPlaces.release();
                }
            });
        } else {
            mMap.addMarker(new MarkerOptions()
                .position(mDefaultLocation)
                .title(getString(R.string.default_info_title))
                .snippet(getString(R.string.default_info_snippet)));
        }

    }

    //Updates the map's UI settings based on whether the user has granted location permission.
    //根據用戶是否已授予位置權限更新地圖UI設置
    @SuppressWarnings("MissingPermission")
    private void updateLocationUI() {
        if(mMap == null){
            return;
        }

        if(mLocationPermissionGranted){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
           mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mCurrentLocation = null;
        }
    }

    //計算兩點位置經緯度距離的公式
    //參考：http://panyee.cnblogs.com/archive/2006/07/04/442771.html
    //球面三角學
    private final double EARTH_RADIUS = 6378137.0;

    private double GPSdistance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;      //這裡求出來是米
        return s;
    }



}





