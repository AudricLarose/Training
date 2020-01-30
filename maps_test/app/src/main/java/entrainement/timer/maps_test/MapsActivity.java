package entrainement.timer.maps_test;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private static final LatLng Loc1= new LatLng(19.6 ,-25.8);
    private static final LatLng Loc2= new LatLng(-10.9 ,-89.4);
    private static final LatLng Loc3= new LatLng(-17.0  ,-126.3);
    private static final LatLng Loc4= new LatLng(6.3 ,-45.0);

    private Marker Loca1;
    private Marker Loca2;
    private Marker Loca3;
    private Marker Loca4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        LatLng creteil = new LatLng(48.783616, 2.46525);
        mMap.addMarker(new MarkerOptions().position(creteil).title("C'est Créteil !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(creteil,14));
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
