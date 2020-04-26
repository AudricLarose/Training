package entrainement.timer.p7_go4lunch.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Bases.ActivityDetails;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.ApiforOnePlace;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Inter;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.PlaceApi;
import entrainement.timer.p7_go4lunch.model.Results;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static java.lang.String.valueOf;

public class Other {
    private static final String TAG = "Other";
    public static int count = 0;
    private static PlacesClient placesClient;
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private FindCurrentPlaceRequest request;


    // Update the list of the modele
    public static void updatemyliste(String id, List<Results> liste2place, int increment, String go_or_like) {
        for (Results place : liste2place) {
            if (place.getId().equals(id)) {
                switch (go_or_like) {
                    case "go":
                        place.setWhocome(valueOf(Integer.valueOf(place.getWhocome()) + increment));
                        break;
                    case "like":
                        place.setLike(valueOf(Integer.valueOf(place.getLike()) + increment));
                        break;
                    default:
                }
            }
        }
    }

    public static List<Results> updatemylisteRest(String id, List<Results> liste2place, int increment) {
        for (Results place : liste2place) {
            if (place.getId().equals(id)) {
                place.setWhocome(valueOf(Integer.valueOf(place.getWhocome()) + increment));
            }
        }
        return liste2place;
    }

    public static List<Results> updatemylisteLike(String id, List<Results> liste2place, int increment) {
        for (Results place : liste2place) {
            if (place.getId().equals(id)) {
                place.setWhocome(valueOf(Integer.valueOf(place.getLike()) + increment));
            }
        }
        return liste2place;
    }

    // Send all the list of Result model on BDD
    public static void sendItToMyBDDPlease(String id, List<Results> liste) {
        Map<String, Object> note = new HashMap<>();
        for (Results object : liste) {
            if (object.getId().equals(id)) {
                note.put("name", object.getName());
                note.put("id", object.getId());
                note.put("whocome", object.getWhocome());
                note.put("like", object.getLike());
                note.put("lat",object.getGeometry().getLocation().getLat().toString());
                note.put("longi",object.getGeometry().getLocation().getLng().toString());
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore
                        .collection("restaurant")
                        .document(Me.getMyId())
                        .collection("Myplace")
                        .document(object.getId())
                        .set(note);
            }
        }
    }

    // Verify if user have GPS on
    public static void GPSOnVerify(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertGPSInactif(context);
        }
    }

    // Show Notification if GPS is off
    private static void AlertGPSInactif(Context context) {
        AlertDialog alertDialog = AlertGPS(context);
        alertDialog.show();
    }

    private static AlertDialog AlertGPS(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.mustGPSOn).setTitle("Alert GPS").setPositiveButton(R.string.GPSok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Other.GPSOnVerify(context);
            }
        });
        return builder.create();
    }

    // Verify if user have internet on
    public static Boolean internetIsOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(context, R.string.bad_internet, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public static void checkrealtime(Adapterinterf adapterinterf) {
        ExtendedServicePlace servicePlace = DI.getServicePlace();
        List<Results> listePlaceApi = servicePlace.generateListPlaceAPI();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(Me.getMyId()).collection("Myplace")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<Results> resultsBDD = null;
                        if (queryDocumentSnapshots != null) {
                            resultsBDD = queryDocumentSnapshots.toObjects(Results.class);
                            for (int i = 0; i < listePlaceApi.size(); i++) {
                                for (Results resultBDD : resultsBDD) {
                                    String resultBDDId = resultBDD.getId();
                                    String id = listePlaceApi.get(i).getId();
                                    if (resultBDDId.equals(id)) {
                                        listePlaceApi.get(i).setWhocome(resultBDD.getWhocome());
//                                        servicePlace.GetApiPlace(context,mMap);
                                    }
                                }
                            }
                            adapterinterf.onFinish(listePlaceApi);
                            adapterinterf.onRequest(resultsBDD);
                        }
                    }
                });
    }

    // Send all the list of Collegue model on BDD
    public static void sendItToMyBDDPleaseatCollegue(String id, List<Collegue> liste, String resto, String notechoix) {
        Map<String, Object> note = new HashMap<>();
        for (Collegue collegue : liste) {
            if (collegue.getId().equals(Me.getMyId())) {
                collegue.setId_mychoice(id);
                collegue.setAdresse_Choice(Me.getAdresschoice());
                collegue.setChoice(resto);
                collegue.setNote_choice(notechoix);
                collegue.setPhoto_choice(Me.getChoicePhoto());
                String date = valueOf(SystemClock.elapsedRealtime());
                note.put("date", date);
                note.put("choice", resto);
                note.put("adresse_Choice", Me.getAdresschoice());
                note.put("Id_mychoice", Me.getId_mychoice());
                note.put("note_choice", Me.getNoteChoice());
                note.put("photo_choice", Me.getChoicePhoto());
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore
                        .collection("collegue")
                        .document(Me.getMyId())
                        .update(note);
            }
        }
    }

    // Erase BDD at the beginning to update
    public static void EraseMyBddWisely() {
        ExtendedServicePlace servicePlace = DI.getServicePlace();
        List<Results> resultsList = servicePlace.generateListPlaceAPI();
        for (Results results : resultsList) {
            if (results.getWhocome().equals("0")) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("restaurant").document(Me.getMyId()).collection("Myplace").document(results.getId()).delete();
            }
        }
    }

    // decrement when someone is leaving a restaurant for an otherone
    public static void decrement(String ancienresto) {
        ExtendedServicePlace servicePlace = DI.getServicePlace();
        List<Results> resultsList = servicePlace.generateListPlaceAPI();
        for (Results place : resultsList) {
            if (place.getName().equals(ancienresto)) {
                place.setWhocome(valueOf(Integer.valueOf(place.getWhocome()) - 1));
                sendItToMyBDDPlease(place.getId(), resultsList);
            }
        }
    }

    // Disconnect to the app
    public static void DisconnectMePlease(Activity activity) {
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity, R.string.deconnection, Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                });
    }


    // Find the Place to display the place's details
    public static void go2theRightPlace(Activity activity) {
        String id_rest = Me.getId_mychoice();
        Intent intent = new Intent(activity, ActivityDetails.class);
        Bundle extra = new Bundle();
        String name_lunch = Me.getMy_choice();
        Other.theGoodPlace(id_rest, Me.getMy_choice(), Me.getChoicePhoto(), Double.valueOf(Me.getNoteChoice()) - 1, Me.getAdresschoice(), new ThegoodPlace() {
            @Override
            public void GoodPlace(Results results) {
                extra.putSerializable("Place", results);
            }
        });
        if (name_lunch != null && !name_lunch.trim().isEmpty()) {
            intent.putExtras(extra);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, R.string.nordv, Toast.LENGTH_LONG).show();
        }
    }

    public static <T> void getCollection(String collectionName, Class<T> tClass, OnFinish<T> onFinish, String... order) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collection = firebaseFirestore
                .collection(collectionName).document(Me.getMyId()).collection("Myplace");
        if (order.length != 0) {
            for (int i = 0; i < order.length; i++) {
                Query query = collection.orderBy(order[i], Query.Direction.DESCENDING);
                query.get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<T> objects = new ArrayList<>();
                                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                                for (DocumentSnapshot doc : docs) {
                                    T e = doc.toObject(tClass);
                                    objects.add(e);
                                }
                                onFinish.success(objects);
                            } else {
                                onFinish.error(task.getException());
                            }
                        });
            }
        } else {
            collection
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<T> objects = new ArrayList<>();
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                T e = doc.toObject(tClass);
                                objects.add(e);
                            }
                            onFinish.success(objects);
                        } else {
                            onFinish.error(task.getException());
                        }
                    });
        }
    }

    public static void theGoodPlace(String id, String name, String photo, Double etoile, String adresse, ThegoodPlace thegoodPlace) {
        Results place = new Results(id, name, photo, etoile, adresse);
        thegoodPlace.GoodPlace(place);
    }

    // refreshing the BDD at the beginning and see whocome and who like since the last connection
    public static void refreshing(Context context, Refreshing refreshing) {
        count = 0;
        ExtendedServicePlace servicePlace = DI.getServicePlace();
        List<Results> listPlace = servicePlace.generateListPlaceAPI();
        for (Results place : listPlace) {
            servicePlace.CompareCollegueNPlace(place, context, new ExtendedServicePlace.Increment() {
                @Override
                public void onFinish() {
                    count = count + 1;
                    if (count == listPlace.size() - 1) {
                        refreshing.onFinish();
                    }
                }
            });

        }
    }

    public static void initGlobalVerificationConnectionCheck(Context context) {
        context.registerReceiver(
                new ConnectivityChangeReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        context.registerReceiver(
                new initBroadcaster4GPS(),
                new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    public static List<Results> sortedByDistance(List<Results> listePlaceApi) {
        Collections.sort(listePlaceApi, new Comparator<Results>() {
            @Override
            public int compare(Results o1, Results o2) {
                if (o1.getGeometry().getLocation().getDistance() > o2.getGeometry().getLocation().getDistance()) {
                    return -1;
                } else {
                    return 1;
                }
            }

        });
        return listePlaceApi;
    }

    public static int getDistance(Double lat, Double longe) {
        float[] result = new float[1];
        Location.distanceBetween(Me.getMy_latitude(), Me.getMy_longitude(), lat, longe, result);
        int round = Math.round(result[0]);
        return round;
    }

    public static List<Results> sortedByLike(List<Results> listePlaceApi) {
        Collections.sort(listePlaceApi, new Comparator<Results>() {
            @Override
            public int compare(Results o1, Results o2) {
                if (Integer.valueOf(o1.getLike()) > Integer.valueOf(o2.getLike())) {
                    return -1;
                } else {
                    return 1;
                }
            }

        });
        return listePlaceApi;
    }

    public static List<Results> sortedByWhoCome(List<Results> listePlaceApi) {
        Collections.sort(listePlaceApi, new Comparator<Results>() {
            @Override
            public int compare(Results o1, Results o2) {
                if (Integer.valueOf(o1.getWhocome()) > Integer.valueOf(o2.getWhocome())) {
                    return -1;
                } else {
                    return 1;
                }
            }

        });
        return listePlaceApi;
    }

    public static String getUrlimage(String photo_reference, String width) {
        String url = "https://maps.googleapis.com/maps/api/place/photo?&photoreference=" + photo_reference + "&maxwidth=" + "300" + "&key=AIzaSyC85iU9E8o4rOCwv2UurWP31fGXaTRcL8c";
        return url;
    }

public interface Finishsuggest{
        void onFinish(List<entrainement.timer.p7_go4lunch.model.Place> placeList);
}
    public static void FilterSearch(Context context, String query, GoogleMap mMap , Finishsuggest finishsuggest) {
        ExtendedServicePlace extendedServicePlace = DI.getServicePlace();
        List<entrainement.timer.p7_go4lunch.model.Place> placeList = extendedServicePlace.generateSuggestion();
        placeList.clear();
        if (query != null) {
            Places.initialize(context, context.getString(R.string.pswd));
            placesClient = Places.createClient(context);
            Toast.makeText(context, query, Toast.LENGTH_SHORT).show();
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
            FindAutocompletePredictionsRequest request1 = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(token)
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setQuery(query)
                    .setCountry("FR")
                    .build();
            placesClient.findAutocompletePredictions(request1).addOnSuccessListener((response) -> {
                placeList.clear();
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    String placeId = prediction.getPlaceId();
                    List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
                    FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
                    placesClient.fetchPlace(request).addOnSuccessListener((response1) -> {
                        Place place = response1.getPlace();
//                        Log.i(TAG, "Place found: " + place.getName());
                        placeList.add(new entrainement.timer.p7_go4lunch.model.Place(place.getId(),place.getName(),place.getAddress()," ",""));
                        LatLng place_found = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        finishsuggest.onFinish(placeList);
                        mMap.addMarker(new MarkerOptions().position(place_found).title(place.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent intent = new Intent(context, ActivityDetails.class);
                                Bundle extra = new Bundle();
                                if (place.getPhotoMetadatas() ==null || place.getRating()==null){
                                    Other.theGoodPlace(place.getId(), place.getName(), " ", Double.valueOf(3), place.getAddress(), new Other.ThegoodPlace() {
                                        @Override
                                        public void GoodPlace(Results place) {
                                            extra.putSerializable("Place", place);
                                        }
                                    });
                                } else{
                                    Other.theGoodPlace(place.getId(), place.getName(), place.getPhotoMetadatas().get(0).toString(), place.getRating(), place.getAddress(), new Other.ThegoodPlace() {
                                        @Override
                                        public void GoodPlace(Results place) {
                                            extra.putSerializable("Place", place);
                                        }
                                    });
                                }

                                intent.putExtras(extra);
                                context.startActivity(intent);
                            }
                        });
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));

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

    public static void checkrealitimeCollegue(AdapterCollegueCB adapterCollegueCB) {
        ExtendedServiceCollegue extendedServiceCollegue = DI.getService();
        List<Collegue> listeCollegue = extendedServiceCollegue.generateListCollegue();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("collegue")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot
                                                queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<Collegue> resultsBDD = queryDocumentSnapshots.toObjects(Collegue.class);
                        for (int i = 0; i < listeCollegue.size(); i++) {
                            for (Collegue resultBDD : resultsBDD) {
                                String resultBDDId = resultBDD.getId();
                                String id = listeCollegue.get(i).getId();
                                if (resultBDDId.equals(id)) {
                                    listeCollegue.get(i).setName(resultBDD.getName());
                                    listeCollegue.get(i).setChoice(resultBDD.getChoice());
                                }
                            }
                        }
                        adapterCollegueCB.onFinish(listeCollegue);
                    }
                });
    }

    public static void hashFinder(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "entrainement.timer.p7_go4lunch",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public static void mylocation(Activity activity, GoogleMap mMap, Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Log.d(TAG, "coordonnées: " + latLng.toString());
                    Me.setLatlng_me(latLng);
                    Me.setMy_latitude(latitude);
                    Me.setMy_longitude(longitude);
                    Other other = new Other();
                    String latl = latitude + "," + longitude;
                    other.CallApiPlease(context, latl,mMap, new Other.TheCalling() {
                        @Override
                        public void onFinish() {


                        }
                    });
                    mMap.addMarker(new MarkerOptions().position(latLng).title(context.getString(R.string.here)).icon(BitmapDescriptorFactory.fromResource(R.drawable.localisation)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }
        });

    }

    //Get the Place With API Place
    public static void CallApiPlease(Context context, String location, GoogleMap map, TheCalling callback) {
        String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Inter intera = retrofit.create(entrainement.timer.p7_go4lunch.model.Inter.class);
        Call<String> call = intera.getplaces("ChIJN1t_tDeuEmsRUsoyG83frY4", "AIzaSyC85iU9E8o4rOCwv2UurWP31fGXaTRcL8c", location, "restaurant", "800", "cruise");
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Pas de reponses" + response.code());
                }
                String listeplace = response.body();
                String readable = response.body();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(readable);
                    Gson gson = new Gson();
                    ExtendedServicePlace servicePlace = DI.getServicePlace();
                    PlaceApi placeApi = gson.fromJson(readable, PlaceApi.class);
                    servicePlace.generateListPlaceAPI().clear();
                    servicePlace.generateListPlaceAPI().addAll(placeApi.getResults());
                    for (Results results : placeApi.getResults()) {
                        results.setLike("0");
                    }
                    refreshing(context, new Refreshing() {
                        @Override
                        public void onFinish() {
                            servicePlace.GetApiPlace(context, map);
                            Other.EraseMyBddWisely();
                            callback.onFinish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d(TAG, "onResponse: " + t.getMessage());
            }
        });
    }

    public static void CallApiOnOnePlacePlease(String location, Finish finish) {
        ExtendedServicePlace servicePlace = DI.getServicePlace();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/details/ ")
                .addConverterFactory(ScalarsConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Inter intera = retrofit.create(entrainement.timer.p7_go4lunch.model.Inter.class);
        Call<String> call = intera.getOnlyOnePlaceWithDetails(location, "AIzaSyC85iU9E8o4rOCwv2UurWP31fGXaTRcL8c", "id,name,geometry,website,formatted_phone_number,type,vicinity,photo");
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Pas de reponses" + response.code());
                }
                String readable = response.body();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(readable);
                    Gson gson = new Gson();
                    List<Results> listfor1 = servicePlace.generateListPlaceAPI();
                    ApiforOnePlace apiforOnePlace = gson.fromJson(obj.toString(), ApiforOnePlace.class);
                    finish.onFinish(apiforOnePlace.getResults());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d(TAG, "onResponse: " + t.getMessage());
            }
        });
    }

    public interface AdapterCollegueCB {
        void onFinish(List<Collegue> listresultcollegue);
    }

    public interface Finish {
        void onFinish(Results apiforOnePlaces);
    }

    public interface TheCalling {
        void onFinish();
    }

    public interface Adapterinterf {
        void onFinish(List<Results> listePlaceApi);
        void onRequest(List<Results> request);
    }

    public interface OnFinish<T> {
        void success(List<T> objects);

        void error(Exception e);
    }


    public interface ThegoodPlace {
        void GoodPlace(Results results);
    }


    public interface Refreshing {
        void onFinish();
    }
}