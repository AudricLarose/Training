package entrainement.timer.p7_go4lunch.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Activities.ActivityDetails;
import entrainement.timer.p7_go4lunch.Collegue.Collegue;
import entrainement.timer.p7_go4lunch.Collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.R;

public class ExtendedServicePlace implements InterfacePlace {
    private static final String TAG = "ExtendedServicePlace";
    private ExtendedServicePlace servicePlace;
    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private List<Place> listePlace = new ArrayList<>();
    private int increment = 0;
    private String liked = "0";
    private MutableLiveData<List<Place>> liste_de_place = new MutableLiveData<List<Place>>();
    private List<Collegue> listedeCollegue = new ArrayList<>();
    private List<Collegue> listedeCollegueSizer = new ArrayList<>();
    private Me me = new Me();
    private Marker marker;
    float[] result = new float[1];
    private List<com.google.android.libraries.places.api.model.Place.Type> type;
    private Map<String, String> mMarker = new HashMap<>();
    private List<Place> liste2place = ListPlaceGenerator.generatePlace();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//    private DocumentReference documentReference =firebaseFirestore.collection("restaurant").


    @Override
    public List<Place> getPlace(Context context, FindCurrentPlaceRequest request, PlacesClient placesClient, GoogleMap mMap, ProgressBar progressBar) {
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
                            String adressePlace = placeLikelihood.getPlace().getAddress();
                            String phonePlace = addressList.get(0).getPhone();
                            String horairePlace = addressList.get(0).getThoroughfare();
                            List photoPlace = placeLikelihood.getPlace().getPhotoMetadatas();
                            String website = String.valueOf(placeLikelihood.getPlace().getWebsiteUri());
                            double latitude = addressList.get(0).getLatitude();
                            double longitude = addressList.get(0).getLongitude();
                            String idPlace = String.valueOf(latitude) + String.valueOf(longitude);
                            String typePlace = placeLikelihood.getPlace().getTypes().toString();
                            Log.d(TAG, "getPlace: " + placeLikelihood.getPlace().getTypes().toString());

                            //  listePlace.add(new Place(nomPlace, adressePlace, phonePlace, notePlace));
                            type = placeLikelihood.getPlace().getTypes();
                            LatLng latlongPlace = new LatLng(latitude, longitude);
                            String quivient = "0";
                            String note = "0";
                            if (me.getMy_latitude() != null) {
                                Location.distanceBetween(me.getMy_latitude(), me.getMy_longitude(), latitude, longitude, result);
                            }
                            String distance = String.valueOf(Math.round(result[0]));
                            //  String whocome = howManyCome(nomPlace, idPlace);
                            servicePlace.addPlace(nomPlace, adressePlace, quivient, note, idPlace, longitude, latitude, distance, typePlace, horairePlace, website, phonePlace);
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
    public void put_first_available_place_in_db(GoogleMap mMap, Context context){
        PlacethePlace(context,mMap);
        setTheDistance();
        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot:task.getResult()) {
                        String idplace = documentSnapshot.getString("id");
                        String nomPlace = documentSnapshot.getString("nomPlace");
                        String adresse = documentSnapshot.getString("Adresse");
                        String note = documentSnapshot.getString("note");
                        String quivient = documentSnapshot.getString("quivient");
                        String distance = documentSnapshot.getString("distance");
                        String longitude = documentSnapshot.getString("latitude");
                        String latitude = documentSnapshot.getString("longitude");
                        LatLng latLng=new LatLng(Double.valueOf(longitude),Double.valueOf(latitude)) ;
                        String phone = documentSnapshot.getString("distance");
                        String horaire = documentSnapshot.getString("horaire");
                        String website = documentSnapshot.getString("site");
                        if (!distance.trim().isEmpty()&&distance!=null) {
                            if (Integer.valueOf(distance)<600) {
                                eventPlace(quivient , mMap,latLng,nomPlace,adresse,idplace);
                            }
                        }
                    }
                }
            }
        });
    }
   public Task<QuerySnapshot> call_all_retaurant(){
         Query db_my_restaurants=firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").orderBy("quivient", Query.Direction.DESCENDING);
         Task<QuerySnapshot> query_my_restaurant = db_my_restaurants.get();
         return query_my_restaurant;
    }
    public Task<QuerySnapshot> sorted_list_of_restaurant(String sorted){
        Query db_my_restaurants=firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").orderBy(sorted, Query.Direction.DESCENDING);
        Task<QuerySnapshot> query_my_restaurant = db_my_restaurants.get();
        return query_my_restaurant;
    }

    public Task<DocumentSnapshot> call_this_restaurant(String idRestaurant){
        CollectionReference db_my_restaurants=firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace");
        Task<DocumentSnapshot> query_my_restaurant = db_my_restaurants.document(idRestaurant).get();
        return query_my_restaurant;
    }

    void setTheDistance() {
        Map<String, Object> note = new HashMap<>();
        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if ((documentSnapshot.getString("latitude") != null) && documentSnapshot.getString("longitude")!=null && (me.getMy_longitude()!= null) &&(me.getMy_latitude()!= null)) {
                            Double latitude = Double.valueOf(documentSnapshot.getString("latitude"));
                            Double longitude = Double.valueOf(documentSnapshot.getString("longitude"));
                            float[] result = ExtendedServicePlace.this.result;
                            Location.distanceBetween(me.getMy_latitude(), me.getMy_longitude(), latitude, longitude, result);
                            String distance = String.valueOf(Math.round(result[0]));
                            String idRestaurant = documentSnapshot.getString("id");

                            note.put("distance", distance);
                            firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idRestaurant).update(note);
                        }
                    }
                }
            }
        });
    }

    void PlacethePlace(Context context, GoogleMap mMap) {
        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            successPlace(task, mMap, context, firebaseFirestore);
                        }
                    }
                });

    }

    @VisibleForTesting
    public void successPlace(@NonNull Task<QuerySnapshot> task, GoogleMap mMap, Context context, FirebaseFirestore firebaseFirestore) {
        Map<String, String> note = new HashMap<>();
        for (DocumentSnapshot documentSnapshot : task.getResult()) {

            String id = documentSnapshot.getString("id");
            if (documentSnapshot.getString("latitude") != null) {
                Double latitude = Double.parseDouble(documentSnapshot.getString("latitude"));
                Double longitude = Double.parseDouble(documentSnapshot.getString("longitude"));
                LatLng latlongPlace = new LatLng(latitude, longitude);
                String nomPlace = documentSnapshot.getString("nomPlace");
                String adressePlace = documentSnapshot.getString("Adresse");
                String notes = documentSnapshot.getString("note");
                String whocome = documentSnapshot.getString("quivient");

                note.put(adressePlace, notes);
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
                if (id != null) {
                    DocumentReference doc = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id);
                    doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot2, @Nullable FirebaseFirestoreException e) {
//                            eventPlace(whocome, mMap, latlongPlace, nomPlace, adressePlace, id);
                        }
                    });
                }
            }
        }
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


    void SortPlaceDB() {
        Task<QuerySnapshot> db=call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            setTheDistance();
                            List<Place> tmp = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String idplace = documentSnapshot.getId();
                                String quivient = documentSnapshot.getString("quivient");
                                String like = documentSnapshot.getString("note");
                                String distance = documentSnapshot.getString("distance");
                                if ((quivient != null) || (like != null)) {
                                    if (distance != null) {
                                        if (Integer.valueOf(distance)<600) {
                                        } else if (Integer.valueOf(quivient)>=1) {
                                        } else {
                                            firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace).delete();
                                        }
                                    } else {
                                        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace).delete();
                                    }
                                } else {
                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace).delete();
                                }
                            }
                        }
                    }
                });
    }

    void addPlace(String nomplace, String adresse, String quivient, String note, String id, double longitude, double latitude, String distance, String type, String horairePlace, String website, String phonePlace) {
        Me me = new Me();
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
        listeplacemap.put("distance", distance);
        call_this_restaurant(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String idresto = documentSnapshot.getString("id");
                    if (idresto == null) {
                        if (distance != null) {
                            if (Integer.parseInt(distance) < 300) {
                                if (type.contains("FOOD")) {
                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).set(listeplacemap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

            }
        });


    }


    @Override
    public void getListOfPlace() {
//        setTheDistance();
                    call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            liste2place.clear();
                            List<Place> tmp = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String idplace = documentSnapshot.getString("id");
                                String nomPlace = documentSnapshot.getString("nomPlace");
                                String adresse = documentSnapshot.getString("Adresse");
                                String note = documentSnapshot.getString("note");
                                String quivient = documentSnapshot.getString("quivient");
                                String distance = documentSnapshot.getString("distance");
                                String latitude = documentSnapshot.getString("latitude");
                                String longitude = documentSnapshot.getString("longitude");
                                LatLng latLng=new LatLng(Double.valueOf(longitude),Double.valueOf(latitude)) ;
                                String phone = documentSnapshot.getString("distance");
                                String horaire = documentSnapshot.getString("horaire");
                                String website = documentSnapshot.getString("site");
                                if (distance != null && nomPlace != null && adresse != null) {
                                        tmp.add(new Place(nomPlace, adresse, horaire, distance, quivient, note, idplace, phone, website));
                                        if (idplace != null) {
                                            DocumentReference doc = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace);
                                            doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                                        Log.d(TAG, "onEvent: " + documentSnapshot.getData());
                                                        cleanNDisplay();
                                                    }
                                                }
                                            });
                                        }

                                }

                            }
                        }
                    }
                });
    }

    public void cleanNDisplay() {
        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            liste2place.clear();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String idplace = documentSnapshot.getString("id");
                                String nomPlace = documentSnapshot.getString("nomPlace");
                                String adresse = documentSnapshot.getString("Adresse");
                                String note = documentSnapshot.getString("note");
                                String quivient = documentSnapshot.getString("quivient");
                                String distance = documentSnapshot.getString("distance");
                                String phone = documentSnapshot.getString("phone");
                                String horaire = documentSnapshot.getString("horaire");
                                String website = documentSnapshot.getString("site");
                                liste2place.add(new Place(nomPlace, adresse, horaire, distance, quivient, note, idplace, phone, website));
                            }
                        }
                    }
                });
    }

    @Override
    public void saveMyPlace(String nom_restaurant, String id) {
        me.setMon_choix(nom_restaurant);
       call_this_restaurant(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.getString("quivient") != null) {
                                    increment = Integer.parseInt(documentSnapshot.getString("quivient"));
                                } else {
                                    increment = 0;
                                }
                                increment = increment + 1;
                                String stringincrement = String.valueOf(increment);
                                Map<String, Object> note = new HashMap<>();
                                note.put("quivient", stringincrement);
                                firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).update(note);
                            } else {
                                increment = increment + 1;
                                String stringincrement = String.valueOf(increment);
                                Map<String, Object> note = new HashMap<>();
                                note.put("quivient", stringincrement);
                                firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).set(note);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void unsaveMyPlace(String id) {
        me.setMon_choix(" ");
        Map<String, Object> note = new HashMap<>();
        note.put("choix", " ");
        firebaseFirestore
                .collection("collegue").document(me.getMonId())
                .update(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });
    }

    @Override
    public List<Collegue> compareCollegueNPlace(String nomduResto, String idData, Context context) {
        MutableLiveData<List<Collegue>> liveData = serviceCollegue.GetQuiVient();
        List<Collegue> tmp = new ArrayList<>();
        serviceCollegue.call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tmp.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String idCollegue = documentSnapshot.getString("id");
                        DocumentReference documentReference = firebaseFirestore.collection("collegue").document(idCollegue);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                String nomCollegue = documentSnapshot.getString("Nom");
                                String choixCollegue = documentSnapshot.getString("choix");
                                String  photoCollegue = documentSnapshot.getString("photo");
                                if (choixCollegue.equals(nomduResto)) {
                                    tmp.add(new Collegue(nomCollegue,nomduResto,photoCollegue));
                                }
                                serviceCollegue.getcoworker(nomduResto);
                                Map<String, Object> note = new HashMap<>();
                                int size = tmp.size();
                                note.put("quivient", String.valueOf(size));
//                                syncMyPlace(idData, String.valueOf(size));
                                if (idData != null) {
                                    firebaseFirestore.collection("restaurant")
                                            .document(me.getMonId())
                                            .collection("Myplace")
                                            .document(idData.trim())
                                            .update(note);
                                }
//                                Toast.makeText(context, me.getMonNOm()+" va au " + me.getMon_choix(), Toast.LENGTH_SHORT).show();
                                liveData.setValue(tmp);
                            }
                        });

                    }
                }

            }
        });
        return tmp;
    }

    public void syncMyPlace(String idRestaurant, String quivient) {
        Map<String, Object> note = new HashMap<>();
        note.put("quivient", quivient);
        serviceCollegue.call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String idCollegue = documentSnapshot.getString("id");
                        firebaseFirestore.collection("restaurant").document(idCollegue).collection("Myplace").document(idRestaurant).update(note);
                    }
                }
            }
        });
    }


    @Override
    public void ilike(String idresto) {
       call_this_restaurant(idresto).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.getString("note") != null) {
                                    increment = Integer.parseInt(documentSnapshot.getString("note"));
                                } else {
                                    increment = 0;
                                }
                                increment = increment + 1;
                                String stringincrement = String.valueOf(increment);
                                Map<String, Object> note = new HashMap<>();
                                Map<String, Object> notelike = new HashMap<>();
                                note.put("note", stringincrement);
                                notelike.put("id_restaurant", idresto);
                                firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idresto).update(note);
                                firebaseFirestore.collection("collegue").document(me.getMonId()).collection("ilike").document(idresto).set(notelike);
                                serviceCollegue.updateMyLikes();
                            } else {
                                increment = increment + 1;
                                String stringincrement = String.valueOf(increment);
                                Map<String, Object> note = new HashMap<>();
                                Map<String, Object> notelike = new HashMap<>();
                                note.put("note", stringincrement);
                                notelike.put("id_restaurant", idresto);
                                firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idresto).set(note);
                                firebaseFirestore.collection("collegue").document(me.getMonId()).collection("ilike").document(idresto).set(notelike);
                                serviceCollegue.updateMyLikes();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void addMyChoice(String idrestaurant, Boolean come, Boolean like) {
        Map<String, Object> note = null;
        if (come) {
            note = new HashMap<>();
            note.put("iCome", "true");
            firebaseFirestore.collection("restaurant")
                    .document(me.getMonId())
                    .collection("Myplace")
                    .document(idrestaurant)
                    .collection("choice")
                    .document(me.getMonNOm())
                    .set(note);
        }
        if (like) {
            Map<String, Object> note2 = new HashMap<>();
            note2.put("iLike", "true");
            firebaseFirestore.collection("restaurant")
                    .document(me.getMonId())
                    .collection("Myplace")
                    .document(idrestaurant)
                    .collection("choice")
                    .document(me.getMonNOm())
                    .update(note2);
        }
    }

    @Override
    public void unlike(String resto, String id) {
        firebaseFirestore.collection("collegue").document(me.getMonId()).collection("ilike").document(resto).delete();
        Task<DocumentSnapshot> db=call_this_restaurant(resto)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.getString("note") != null) {
                                    increment = Integer.parseInt(documentSnapshot.getString("note"));
                                    increment = increment - 1;
                                    String stringincrement = String.valueOf(increment);
                                    Map<String, Object> note = new HashMap<>();
                                    note.put("note", stringincrement);
                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(resto).update(note);
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    public List<Place> generateListPlace() {
        return liste2place;
    }
}
