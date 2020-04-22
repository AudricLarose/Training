package entrainement.timer.p7_go4lunch.utils.restaurant;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.Bases.ActivityAfterCheck;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Fragmentcarte<call> extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private ExtendedServicePlace extendedServicePlace;
    private ImageButton localise;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FindCurrentPlaceRequest request;
    private ProgressBar progressBar;
    private SearchView searchView = null;
    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private Marker marker;
    private RequestQueue queue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_fragmentcarte, container, false);
        localise = rootView.findViewById(R.id.local);
        extendedServicePlace = DI.getServicePlace();
        Places.initialize(getContext(), getString(R.string.pswd));
        placesClient = Places.createClient(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        serviceCollegue.updateMyLikes();
        extendedServicePlace = DI.getServicePlace();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        queue = Volley.newRequestQueue(getContext());

        return rootView;
    }


//    public void FilterSearch(Context context, String query) {
//        if (query != null) {
//            Places.initialize(context, getString(R.string.pswd));
//            placesClient = Places.createClient(context);
//            Toast.makeText(context, query, Toast.LENGTH_SHORT).show();
//            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
//            FindAutocompletePredictionsRequest request1 = FindAutocompletePredictionsRequest.builder()
//                    .setSessionToken(token)
//                    .setQuery(query)
//                    .build();
//
//            placesClient.findAutocompletePredictions(request1).addOnSuccessListener((response) -> {
//                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//                    String hint = prediction.getFullText(null).toString();
//                    String placeId = prediction.getPlaceId();
//                    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
//                    FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
//                    placesClient.fetchPlace(request).addOnSuccessListener((response1) -> {
//                        Place place = response1.getPlace();
//                        Log.i(TAG, "Place found: " + place.getLatLng());
//                        LatLng place_found = new LatLng(place.getLatLng().longitude, place.getLatLng().latitude);
//                        mMap.addMarker(new MarkerOptions().position(place_found).title(place.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
//                    }).addOnFailureListener((exception) -> {
//                        if (exception instanceof ApiException) {
//                            ApiException apiException = (ApiException) exception;
//                            int statusCode = apiException.getStatusCode();
//                            // Handle error with given status code.
//                            Log.e(TAG, "Place not found: " + exception.getMessage());
//                        }
//                    });
//                }
//            }).addOnFailureListener((exception) -> {
//                if (exception instanceof ApiException) {
//                    ApiException apiException = (ApiException) exception;
//                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//                }
//            });
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Other.FilterSearch(getContext(),query,mMap);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }

            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        Other.GPSOnVerify(getContext());
        List<Marker> markerList = new ArrayList<>();
        Places.initialize(getContext(), getString(R.string.pswd));
        PlacesClient placesClient = Places.createClient(getContext());
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng variable = mMap.getCameraPosition().target;
                if (getContext().checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
                } else {
                    String latl = variable.latitude + "," + variable.longitude;
                    Other.CallApiPlease(getContext(),latl, new Other.TheCalling() {
                        @Override
                        public void onFinish() {
         //                   extendedServicePlace.GetApiPlace(getContext(),mMap);
                        }
                    });
                    Toast.makeText(getContext(), variable.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);
        request = FindCurrentPlaceRequest.newInstance(placeFields);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        serviceCollegue.getListCollegue();


        localise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            Log.d(TAG, "coordonnées: " + latLng.toString());
                            mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.here)).icon(BitmapDescriptorFactory.fromResource(R.drawable.localisation)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        }
                    }
                });
                Other.mylocation(getActivity(),mMap,getContext());
            }
        });
        if (getContext().checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
        } else {
            Other.mylocation(getActivity(),mMap,getContext());

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = null;
            LocationListener locationListener = null;
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

}