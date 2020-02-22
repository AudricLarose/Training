package entrainement.timer.maps_test;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entrainement.timer.maps_test.Util.Constants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private static final LatLng Loc1= new LatLng(19.6 ,-25.8);
    private static final LatLng Loc2= new LatLng(-10.9 ,-89.4);
    private static final LatLng Loc3= new LatLng(-17.0  ,-126.3);
    private static final LatLng Loc4= new LatLng(6.3 ,-45.0);
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Marker Loca1;
    private Marker Loca2;
    private Marker Loca3;
    private Marker Loca4;
    private RequestQueue queue;
    private Marker meU;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        queue= Volley.newRequestQueue(this);
        getEarthQuakes();

        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onStatusChanged: " + location.toString() );
                LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(me).title("C'est Créteil !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me,14));
                Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    String fulladress= " ";

               if (addressList!=null && addressList.size()>0){
                   if (addressList.get(0).getSubLocality()!=null){
                       fulladress+=addressList.get(0).getSubLocality();
                   }
                   Toast.makeText(getApplicationContext(),addressList.get(0).getAddressLine(0),Toast.LENGTH_LONG).show();
                   Log.d(TAG, "onLocationChanged: "+ addressList.get(0).toString());
               }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

    }

    private void getEarthQuakes() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray features = response.getJSONArray("features");
                    for (int i =0 ; i<features.length();i++){
                        JSONObject propreties= features.getJSONObject(i).getJSONObject("properties");
                        Log.d(TAG, "onResponse: " + propreties.getString("place"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Marker> markerList= new ArrayList<>();
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        Loca1=mMap.addMarker(new MarkerOptions()
        .position(Loc1)
                .title("hasard"));
        Loca1.setTag(0);
        markerList.add(Loca1);
        Loca2=mMap.addMarker(new MarkerOptions()
                .position(Loc2)
                .title("hasard")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Loca2.setTag(0);
        markerList.add(Loca2);
        Loca3=mMap.addMarker(new MarkerOptions()
                .position(Loc3)
                .title("hasard")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        Loca3.setTag(0);
        markerList.add(Loca3);

        mMap.setOnMarkerClickListener(this);

        for (Marker list : markerList){
            LatLng latLng= new LatLng(list.getPosition().latitude,list.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,2));

        }


        // Add a marker in Sydney and move the camera

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();
        if (clickCount!=null){
            clickCount= clickCount+1;
            marker.setTag(clickCount);
            Toast.makeText(this,marker.getTitle()+" a été cliqué " + clickCount+ " fois", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
