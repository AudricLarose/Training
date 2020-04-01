package entrainement.timer.p7_go4lunch.api.restaurant;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Bases.ActivityDetails;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Place;
import entrainement.timer.p7_go4lunch.runTest.FirebaseCollectionGrabber;
import entrainement.timer.p7_go4lunch.utils.Other;

public class ExtendedServicePlace implements InterfacePlace {
    private static final String TAG = "ExtendedServicePlace";
    float[] result = new float[1];
    private ExtendedServicePlace servicePlace;
    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private List<Place> listePlace = new ArrayList<>();
    private Marker marker;
    private Map<String, String> mMarker = new HashMap<>();
    private List<Place> liste2place = ListPlaceGenerator.generatePlace();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public Task<QuerySnapshot> call_all_retaurant() {
        Query db_my_restaurants = firebaseFirestore.collection("restaurant").document(Me.getMonId()).collection("Myplace").orderBy("quivient", Query.Direction.DESCENDING);
        Task<QuerySnapshot> query_my_restaurant = db_my_restaurants.get();
        return query_my_restaurant;
    }


    public Task<DocumentSnapshot> call_this_restaurant(String idRestaurant) {
        CollectionReference db_my_restaurants = firebaseFirestore.collection("restaurant").document(Me.getMonId()).collection("Myplace");
        Task<DocumentSnapshot> query_my_restaurant = db_my_restaurants.document(idRestaurant).get();
        return query_my_restaurant;
    }

    @Override
    public List<Place> getPlace(Context context, FindCurrentPlaceRequest request, PlacesClient placesClient, GoogleMap mMap, ProgressBar progressBar) {
        Address adressList = new Address(Locale.getDefault());
        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                servicePlace = DI.getServicePlace();
                FindCurrentPlaceResponse response = task.getResult();
                listePlace.clear();
                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                    String nomPlace = placeLikelihood.getPlace().getName();
                    try {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocationName(nomPlace, 1);
                        String fulladress = " ";
                        if (addressList != null && addressList.size() > 0) {
                            if (addressList.get(0).getSubLocality() != null) {
                                fulladress += addressList.get(0).getSubLocality();
                            }

                            double latitude = addressList.get(0).getLatitude();
                            double longitude = addressList.get(0).getLongitude();
                            List<com.google.android.libraries.places.api.model.Place.Type> type;
                            List photoPlace = placeLikelihood.getPlace().getPhotoMetadatas();
                            String adressePlace = placeLikelihood.getPlace().getAddress();
                            String phonePlace = addressList.get(0).getPhone();
                            String horairePlace = addressList.get(0).getThoroughfare();
                            String website = String.valueOf(placeLikelihood.getPlace().getWebsiteUri());
                            String idPlace = String.valueOf(latitude) + String.valueOf(longitude);
                            String typePlace = placeLikelihood.getPlace().getTypes().toString();
                            String quivient = "0";
                            String note = "0";
                            type = placeLikelihood.getPlace().getTypes();
                            LatLng latlongPlace = new LatLng(latitude, longitude);
                            Log.d(TAG, "getPlace: " + placeLikelihood.getPlace().getTypes().toString());
                            Object places = placeLikelihood.getPlace();

                            if (Me.getMy_latitude() != null) {
                                Location.distanceBetween(Me.getMy_latitude(), Me.getMy_longitude(), latitude, longitude, result);
                            }
                            servicePlace.addPlace(nomPlace, adressePlace, quivient, note, idPlace, longitude, latitude, "", typePlace, horairePlace, website, phonePlace,photoPlace);
                            progressBar.setVisibility(View.GONE);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                PlacethePlace(context, mMap);
                Exception exception = task.getException();
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());

                }
            }
        });
        return listePlace;
    }

    void addPlace(String nomplace, String adresse, String quivient, String note, String id, double longitude, double latitude, String distance, String type, String horairePlace, String website, String phonePlace, List photoPlace) {
        Map<String, String> listeplacemap = new HashMap<>();
        listeplacemap.put("nomPlace", nomplace);
        listeplacemap.put("Adresse", adresse);
        listeplacemap.put("quivient", quivient);
        listeplacemap.put("id", id);
        listeplacemap.put("note", note);
        listeplacemap.put("latitude", String.valueOf(latitude));
        listeplacemap.put("longitude", String.valueOf(longitude));
        listeplacemap.put("horaire", horairePlace);
        listeplacemap.put("phone", phonePlace);
        listeplacemap.put("site", website);
        listeplacemap.put("photo", String.valueOf(photoPlace));
        call_this_restaurant(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String idresto = documentSnapshot.getString("id");
                    if (idresto == null) {
                        if (distance != null) {
                                if (type.contains("FOOD")) {
                                    firebaseFirestore.collection("restaurant").document(Me.getMonId()).collection("Myplace").document(id).set(listeplacemap);
                            }
                        }
                    }
                }

            }
        });
    }

    void PlacethePlace(Context context, GoogleMap mMap) {
        Map<String, String> note = new HashMap<>();
        for (Place place : liste2place) {
            if (place.getLatitude() != null) {
                LatLng latlongPlace = new LatLng(Double.parseDouble(place.getLatitude()), Double.parseDouble(place.getLongitude()));
                eventPlace(place.getquivient(), mMap, latlongPlace, place.getnomPlace(), place.getAdresse(), place.getId());
            }
            note.put(place.getAdresse(), place.getnote());
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(context, ActivityDetails.class);
                    intent.putExtra("id", mMarker.get(marker.getId()));
                    intent.putExtra("nom", marker.getTitle());
                    intent.putExtra("adresse", marker.getSnippet());
                    intent.putExtra("etoile", note.get(marker.getSnippet()));
                    context.startActivity(intent);
                }
            });
        }
    }

    public void put_first_available_place_in_db(GoogleMap mMap, Context context) {
        PlacethePlace(context, mMap);
        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Place places = documentSnapshot.toObject(Place.class);
                        LatLng latLng = new LatLng(Double.valueOf(places.getLongitude()), Double.valueOf(places.getLatitude()));
                        if (places.getDistance() != null) {
                            if (Integer.valueOf(places.getDistance()) < 600) {
                                eventPlace(places.getquivient(), mMap, latLng, places.getnomPlace(), places.getAdresse(), places.getId());
                            }
                        }
                    }
                }
            }
        });
    }

    private void eventPlace(String whocome, GoogleMap mMap, LatLng latlongPlace, String nomPlace, String adressePlace, String id) {
        if (Integer.parseInt(whocome) >= 1) {
            marker = mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).snippet(adressePlace).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_white)));
            mMarker.put(marker.getId(), id);
        } else {
            marker = mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).snippet(adressePlace).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
            mMarker.put(marker.getId(), id);

        }
    }





    @Override
    public void getListOfPlace(Context context, GoogleMap map) {
        FirebaseCollectionGrabber.getCollection("restaurant", Place.class, new FirebaseCollectionGrabber.OnFinish<Place>() {
            @Override
            public void success(List<Place> objects) {
                liste2place.clear();
                liste2place.addAll(objects);
                PlacethePlace(context,map);
            }

            @Override
            public void error(Exception e) {
            }
        });
    }


    @Override
            public void saveMyPlace(String nom_restaurant, String id) {
        Other.updatemyliste(id,liste2place,1,"go");
        Other.sendItToMyBDDPlease(id,liste2place);

    }

    @Override
    public void unsaveMyPlace(String id) {
        Other.updatemyliste(id,liste2place,-1,"go");
        Other.sendItToMyBDDPlease(id,liste2place);
        Me.setMon_choix(" ");
        Me.setId_monchoix(" ");
    }

    public interface Increment{
        void onFinish();
    }
    @Override
    public List<Collegue> compareCollegueNPlace(String nomduResto, String idData, Context context, Increment... increments) {
        MutableLiveData<List<Collegue>> liveData = serviceCollegue.GetQuiVient();
        List<Collegue> tmp = new ArrayList<>();
        serviceCollegue.call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tmp.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        Collegue collegue = documentSnapshot.toObject(Collegue.class);
                        if (collegue.getChoix().equals(nomduResto)) {
                            tmp.add(new Collegue(collegue.getNom(), nomduResto, collegue.getPhoto()));
                        }
                        for (Place place : liste2place) {
                            if(place.getId().equals(idData)){
                                serviceCollegue.getcoworker(nomduResto);
                                int size = tmp.size();
                                place.setquivient(String.valueOf(size));
                                Other.sendItToMyBDDPlease(idData,liste2place);
                            }
                        }

                               liveData.setValue(tmp);

                    }
                }

                if (increments.length!=0) {
                    increments[0].onFinish();
                }
            }
        });
        return tmp;
    }
    @Override
    public void ilike(String idresto) {
        Other.updatemyliste(idresto,liste2place,1,"like");
        Other.sendItToMyBDDPlease(idresto,liste2place);
    }


    public void addMyChoice(String idrestaurant, Boolean come, Boolean like) {
        if (come) {
            Map<String, Object> note =  new HashMap<>();
            note.put("iCome", "true");
            Me.setBeNotified(true);
            firebaseFirestore.collection("restaurant")
                    .document(Me.getMonId())
                    .collection("Myplace")
                    .document(idrestaurant)
                    .collection("choice")
                    .document(Me.getMonNOm())
                    .set(note);
        }
        if (like) {
            Map<String, Object> note2 = new HashMap<>();
            note2.put("iLike", "true");
            firebaseFirestore.collection("restaurant")
                    .document(Me.getMonId())
                    .collection("Myplace")
                    .document(idrestaurant)
                    .collection("choice")
                    .document(Me.getMonNOm())
                    .update(note2);
        }
    }

    @Override
    public void unlike(String resto, String id) {
        Other.updatemyliste(id,liste2place,-1,"like");
        Other.sendItToMyBDDPlease(id,liste2place);

    }


    public List<Place> generateListPlace() {
        return liste2place;
    }
}
