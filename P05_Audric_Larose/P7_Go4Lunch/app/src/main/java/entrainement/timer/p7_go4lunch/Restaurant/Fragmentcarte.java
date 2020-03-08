package entrainement.timer.p7_go4lunch.Restaurant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.R;

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
    private EditText query;
    private RequestQueue queue;
    private Marker meU;
    private ImageButton localise;
    private Button search;
    private  PlacesClient placesClient;
    private FindCurrentPlaceRequest request;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_fragmentcarte, container, false);
        localise=rootView.findViewById(R.id.local);
        query=rootView.findViewById(R.id.edit_query);
        search=rootView.findViewById(R.id.buttonplay);
        extendedServicePlace= DI.getServicePlace();
        extendedServicePlace.SortPlaceDB();
        Places.initialize(getContext(), "AIzaSyApIPM8WUg0LDig8wUeSY8vvz3dj8mbgTc");
        placesClient = Places.createClient(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });


//
//    });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSearch(v.getContext(),query.getText().toString());
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }
    public void filterSearch(Context context, String query){
        if (query!=null) {
            Places.initialize(context, "AIzaSyApIPM8WUg0LDig8wUeSY8vvz3dj8mbgTc");
            placesClient = Places.createClient(context);
            Toast.makeText(context, query, Toast.LENGTH_SHORT).show();
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

            RectangularBounds bounds = RectangularBounds.newInstance(
                    new LatLng(48.82843, 2.398178),
                    new LatLng(48.875373804191334, 2.296692));

            FindAutocompletePredictionsRequest request1 = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(token)
                    .setQuery(query)
                    .build();

            placesClient.findAutocompletePredictions(request1).addOnSuccessListener((response) -> {
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    String hint=prediction.getFullText(null).toString();
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        List<Marker> markerList= new ArrayList<>();

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);
        request =FindCurrentPlaceRequest.newInstance(placeFields);

        mMap = googleMap;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mylocation();

        localise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylocation();
            }
        });




        if (getContext().checkSelfPermission(ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},1);
        } else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            extendedServicePlace.getPlace(getContext(),request,placesClient, mMap);

        }


    }
    private void mylocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    Me me = new Me();
                    double latitude =location.getLatitude();
                    double longitude =location.getLongitude();
                    LatLng latLng= new LatLng(latitude,longitude);
                    me.setLatlng_me(latLng);
                    me.setMy_latitude(latitude);
                    me.setMy_longitude(longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Hi!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  locationListener);

        }

    }

}
