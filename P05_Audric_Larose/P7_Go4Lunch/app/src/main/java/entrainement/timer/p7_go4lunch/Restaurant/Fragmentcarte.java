package entrainement.timer.p7_go4lunch.Restaurant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.Collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Fragmentcarte extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
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
    private ProgressBar progressBar;
    private ExtendedServiceCollegue serviceCollegue= DI.getService();
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
//        query=rootView.findViewById(R.id.edit_query);
//        search=rootView.findViewById(R.id.buttonplay);
        progressBar=(ProgressBar)rootView.findViewById(R.id.progress_bar);
        extendedServicePlace= DI.getServicePlace();
        extendedServicePlace.SortPlaceDB();
        Places.initialize(getContext(), "AIzaSyApIPM8WUg0LDig8wUeSY8vvz3dj8mbgTc");
        placesClient = Places.createClient(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        serviceCollegue.updateMyLikes();


//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
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
        extendedServicePlace= DI.getServicePlace();
//    localise.setOnClickListener(new View.OnClickListener() {
//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void onClick(View v) {

//            }

//
//    });
        // Inflate the layout for this fragment

//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                filterSearch(v.getContext(),query.getText().toString());
////                Places.initialize(v.getContext(), "AIzaSyApIPM8WUg0LDig8wUeSY8vvz3dj8mbgTc");
////                placesClient = Places.createClient(v.getContext());
////                Toast.makeText(v.getContext(), query.getText().toString(), Toast.LENGTH_SHORT).show();
////                AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
////
////                FindAutocompletePredictionsRequest request1 = FindAutocompletePredictionsRequest.builder()
////                        .setSessionToken(token)
////                        .setQuery(query.getText().toString())
////                        .build();
////
////                placesClient.findAutocompletePredictions(request1).addOnSuccessListener((response) -> {
////                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
////                        String hint=prediction.getFullText(null).toString();
////                        Log.i(TAG, "filterSearch: "+ prediction.getPlaceId());
////
////                    }
////                }).addOnFailureListener((exception) -> {
////                    if (exception instanceof ApiException) {
////                        ApiException apiException = (ApiException) exception;
////                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
////                    }
////                });
//
//            }
//        });

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
                    String placeId = prediction.getPlaceId();
                    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                    FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
                    placesClient.fetchPlace(request).addOnSuccessListener((response1) -> {
                        Place place = response1.getPlace();
                        Log.i(TAG, "Place found: " + place.getLatLng());
                        LatLng place_found=new LatLng(place.getLatLng().longitude,place.getLatLng().latitude);
                        mMap.addMarker(new MarkerOptions().position(place_found).title(place.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),3));

                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            int statusCode = apiException.getStatusCode();
                            // Handle error with given status code.
                            Log.e(TAG, "Place not found: " + exception.getMessage());
                        }
                    });
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

        mMap = googleMap;

        List<Marker> markerList= new ArrayList<>();
        Places.initialize(getContext(), "AIzaSyApIPM8WUg0LDig8wUeSY8vvz3dj8mbgTc");
        PlacesClient placesClient = Places.createClient(getContext());
//        List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);
        request =FindCurrentPlaceRequest.newInstance(placeFields);

        mMap = googleMap;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mylocation();

        localise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null){
                            double latitude =location.getLatitude();
                            double longitude =location.getLongitude();
                            LatLng latLng= new LatLng(latitude,longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Here !").icon(BitmapDescriptorFactory.fromResource(R.drawable.localisation)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

                        }
                    }
                });
                mylocation();
            }
        });
//        locationManager= (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
//        locationListener= new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.d(TAG, "onStatusChanged: " + location.toString() );
//                LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
//                if (mMap!=null) {
//                    mMap.addMarker(new MarkerOptions().position(me).title("C'est Créteil !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 14));
//                }
//                extendedServicePlace.getPlace(getContext(),request,placesClient,mMap);
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//            @Override
//            public void onProviderEnabled(String provider) {}
//            @Override
//            public void onProviderDisabled(String provider) {}
//        };




        if (getContext().checkSelfPermission(ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},1);
        } else {
            extendedServicePlace.getPlace(getContext(),request,placesClient, mMap, progressBar);
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
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Here !").icon(BitmapDescriptorFactory.fromResource(R.drawable.localisation)));
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