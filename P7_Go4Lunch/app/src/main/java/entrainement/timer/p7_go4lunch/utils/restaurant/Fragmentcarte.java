package entrainement.timer.p7_go4lunch.utils.restaurant;

import android.Manifest;
import android.app.SearchManager;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import entrainement.timer.p7_go4lunch.AdaptateurSuggestion;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
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
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private RequestQueue queue;
//    private RecyclerView recyclerView;
//    private RecyclerView.LayoutManager layoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleCursorAdapter mAdapter;
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
//        recyclerView = rootView.findViewById(R.id.RecycleviewMapSuggestion);
        extendedServicePlace = DI.getServicePlace();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        layoutManager = new LinearLayoutManager(rootView.getContext());
        queue = Volley.newRequestQueue(getContext());
        List<entrainement.timer.p7_go4lunch.model.Place> placeList = servicePlace.generateSuggestion();
        AdaptateurSuggestion adaptateurSuggestion = new AdaptateurSuggestion(placeList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adaptateurSuggestion);
//        if (placeList.isEmpty()) {
//            recyclerView.setVisibility(View.GONE);
//        }
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItem sortedmenu = menu.findItem(R.id.sortedmenu);

        if (sortedmenu != null) {
            SearchView searchView2 = (SearchView) sortedmenu.getActionView();
            searchView2.setVisibility(View.INVISIBLE);
        }
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    recyclerView.setVisibility(View.VISIBLE);

                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Other.FilterSearch(getContext(), query, mMap, new Other.Finishsuggest() {
                        @Override
                        public void onFinish(List<entrainement.timer.p7_go4lunch.model.Place> placeList) {
//                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Other.FilterSearch(getContext(), newText, mMap, new Other.Finishsuggest() {
                        @Override
                        public void onFinish(List<entrainement.timer.p7_go4lunch.model.Place> placeList) {

                        }
                    });
                    return true;
                }

            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);
        Other.GPSOnVerify(getContext());
        Places.initialize(getContext(), getString(R.string.pswd));
//        recyclerView.setVisibility(View.GONE);
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Other.internetIsOn(getContext());
                LatLng variable = mMap.getCameraPosition().target;
                if (getContext().checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
                } else {
                    String latl = variable.latitude + "," + variable.longitude;
//                    Other.CallApiPlease(getContext(), latl, mMap, new Other.TheCalling() {
//                        @Override
//                        public void onFinish() {
//                        }
//                    });

                }
            }
        });
        Other.checkrealtime(new Other.Adapterinterf() {
            @Override
            public void onFinish(List<Results> listePlaceApi) {

            }

            @Override
            public void onRequest(List<Results> request) {
                if (request!=null && !request.isEmpty()&&request.get(0).getLat()!=null) {
                    for (int i = 0; i < request.size(); i++) {
                        double latitude = Double.parseDouble(request.get(i).getLat());
                        double longitude = Double.parseDouble(request.get(i).getLongi());
                        LatLng latLng = new LatLng(latitude, longitude);
                        extendedServicePlace.eventPlace(mMap, request.get(i), latLng);
                    }
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
                Other.mylocation(getActivity(), mMap, getContext());
            }
        });
        if (getContext().checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
        } else {
            Other.mylocation(getActivity(), mMap, getContext());

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