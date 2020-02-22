package entrainement.timer.p7_go4lunch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;


public class Fragmentcarte extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private static final LatLng Loc1= new LatLng(19.6 ,-25.8);
    private static final LatLng Loc2= new LatLng(-10.9 ,-89.4);
    private static final LatLng Loc3= new LatLng(-17.0  ,-126.3);
    private static final LatLng Loc4= new LatLng(6.3 ,-45.0);
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ExtendedServicePlace extendedServicePlace;
    private Marker Loca1;
    private Marker Loca2;
    private Marker Loca3;
    private Marker Loca4;
    private RequestQueue queue;
    private Marker meU;
    private Button localise;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Places.initialize(getContext(), "AIzaSyApIPM8WUg0LDig8wUeSY8vvz3dj8mbgTc");
        PlacesClient placesClient = Places.createClient(getContext());
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        View rootView = inflater.inflate(R.layout.fragment_fragmentcarte, container, false);
    localise=rootView.findViewById(R.id.local);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        extendedServicePlace= DI.getServicePlace();
//    localise.setOnClickListener(new View.OnClickListener() {
//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void onClick(View v) {
            locationManager= (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            locationListener= new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "onStatusChanged: " + location.toString() );
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(me).title("C'est Créteil !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me,14));
                    extendedServicePlace.getPlace(getContext(),request,placesClient);
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
            if (getContext().checkSelfPermission(ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                    getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                }
//            }
//
//    });
        // Inflate the layout for this fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Marker> markerList= new ArrayList<>();
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

    }
}
