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
import androidx.annotation.Nullable;

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
    private Me me = new Me();
    private Marker marker;
    private Map<String, String> mMarker = new HashMap<>();
    private List<Place> liste2place = ListPlaceGenerator.generatePlace();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public Task<QuerySnapshot> call_all_retaurant() {
        Query db_my_restaurants = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").orderBy("quivient", Query.Direction.DESCENDING);
        Task<QuerySnapshot> query_my_restaurant = db_my_restaurants.get();
        return query_my_restaurant;
    }

    public Task<QuerySnapshot> sorted_list_of_restaurant(String sorted) {
        Query db_my_restaurants = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").orderBy(sorted, Query.Direction.DESCENDING);
        Task<QuerySnapshot> query_my_restaurant = db_my_restaurants.get();
        return query_my_restaurant;
    }

    public Task<DocumentSnapshot> call_this_restaurant(String idRestaurant) {
        CollectionReference db_my_restaurants = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace");
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
//                            String distance = String.valueOf(Math.round(result[0]));
                            type = placeLikelihood.getPlace().getTypes();
                            LatLng latlongPlace = new LatLng(latitude, longitude);
                            Log.d(TAG, "getPlace: " + placeLikelihood.getPlace().getTypes().toString());
                            Object places = placeLikelihood.getPlace();

                            if (me.getMy_latitude() != null) {
                                Location.distanceBetween(me.getMy_latitude(), me.getMy_longitude(), latitude, longitude, result);
                            }
                            servicePlace.addPlace(nomPlace, adressePlace, quivient, note, idPlace, longitude, latitude, "", typePlace, horairePlace, website, phonePlace);
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
//        listeplacemap.put("distance", distance);
        call_this_restaurant(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String idresto = documentSnapshot.getString("id");
                    if (idresto == null) {
                        if (distance != null) {
//                            if (Integer.parseInt(distance) < 600) {
                                if (type.contains("FOOD")) {
                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).set(listeplacemap);
//                                }
                            }
                        }
                    }
                }

            }
        });
    }

    void PlacethePlace(Context context, GoogleMap mMap) {
        Map<String, String> note = new HashMap<>();
//        Place place = documentSnapshot.toObject(Place.class);
        for (Place place : liste2place) {
            if (place.getLatitude() != null) {
                LatLng latlongPlace = new LatLng(Double.parseDouble(place.getLatitude()), Double.parseDouble(place.getLongitude()));
                eventPlace(place.getquivient(), mMap, latlongPlace, place.getnomPlace(), place.getAdresse(), place.getId());
            }
            //            String id = documentSnapshot.getString("id");
//                String nomPlace = documentSnapshot.getString("nomPlace");
//                String adressePlace = documentSnapshot.getString("Adresse");
//                String notes = documentSnapshot.getString("note");
//                String whocome = documentSnapshot.getString("quivient");
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

//        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
////            if ((documentSnapshot.getString("latitude") != null) && documentSnapshot.getString("longitude") != null && (me.getMy_longitude() != null) && (me.getMy_latitude() != null)) {
//////                Double latitude = Double.valueOf(documentSnapshot.getString("latitude"));
//////                Double longitude = Double.valueOf(documentSnapshot.getString("longitude"));
////
//////                String idRestaurant = documentSnapshot.getString("id");
////
//////                note.put("distance", distance);
//////                firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idRestaurant).update(note);
////            }
//
////            if (place.getId() != null) {
////                DocumentReference doc = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(place.getId());
////                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
////                    @Override
////                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot2, @Nullable FirebaseFirestoreException e) {
////                    }
////                });
////
////
////            }
//                    }
//                }
//            }
//        });
    }

    public void put_first_available_place_in_db(GoogleMap mMap, Context context) {
        PlacethePlace(context, mMap);
//        setTheDistance();
        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                        String idplace = documentSnapshot.getString("id");
//                        String nomPlace = documentSnapshot.getString("nomPlace");
//                        String adresse = documentSnapshot.getString("Adresse");
//                        String quivient = documentSnapshot.getString("quivient");
//                        String distance = documentSnapshot.getString("distance");
//                        String longitude = documentSnapshot.getString("latitude");
//                        String latitude = documentSnapshot.getString("longitude");
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

//    void setTheDistance() {
//        Map<String, Object> note = new HashMap<>();
//        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                        if ((documentSnapshot.getString("latitude") != null) && documentSnapshot.getString("longitude")!=null && (me.getMy_longitude()!= null) &&(me.getMy_latitude()!= null)) {
//                            Double latitude = Double.valueOf(documentSnapshot.getString("latitude"));
//                            Double longitude = Double.valueOf(documentSnapshot.getString("longitude"));
//                            float[] result = ExtendedServicePlace.this.result;
//                            Location.distanceBetween(me.getMy_latitude(), me.getMy_longitude(), latitude, longitude, result);
//                            String distance = String.valueOf(Math.round(result[0]));
//                            String idRestaurant = documentSnapshot.getString("id");
//
//                            note.put("distance", distance);
//                            firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idRestaurant).update(note);
//                        }
//                    }
//                }
//            }
//        });
//    }


    void cleanNDisplay(Context context, GoogleMap map) {
        Place place = new Place();
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


    private void eventPlace(String whocome, GoogleMap mMap, LatLng latlongPlace, String nomPlace, String adressePlace, String id) {
        if (Integer.parseInt(whocome) >= 1) {
            marker = mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).snippet(adressePlace).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_white)));
            mMarker.put(marker.getId(), id);
        } else {
            marker = mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).snippet(adressePlace).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
            mMarker.put(marker.getId(), id);

        }
    }


    public void SortPlaceDB() {
//        boolean sorting = true;
//        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
////                            setTheDistance();
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        Place place = documentSnapshot.toObject(Place.class);
//                        String idplace = place.getId();
//                        String quivient = place.getquivient();
//                        String like = place.getnote();
//                        String distance = place.getDistance();
//                        if ((quivient != null) || (like != null)) {
//                                     if (Integer.valueOf(quivient) >= 1) {
//                                } else {
//                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace).delete();
//                                }
//                        } else {
//                            firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace).delete();
//                        }
//                    }
//                }
//            }
//        });
    }


    @Override
    public void getListOfPlace(Context context, GoogleMap map) {
        cleanNDisplay(context,map);

////        setTheDistance();
//        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    liste2place.clear();
////                            List<Place> tmp = new ArrayList<>();
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        Place places = documentSnapshot.toObject(Place.class);
////                                String idplace = documentSnapshot.getString("id");
////                                String nomPlace = documentSnapshot.getString("nomPlace");
////                                String adresse = documentSnapshot.getString("Adresse");
////                                String note = documentSnapshot.getString("note");
////                                String quivient = documentSnapshot.getString("quivient");
////                                String distance = documentSnapshot.getString("distance");
////                                String latitude = documentSnapshot.getString("latitude");
////                                String longitude = documentSnapshot.getString("longitude");
////                                LatLng latLng=new LatLng(Double.valueOf(longitude),Double.valueOf(latitude)) ;
////                                String phone = documentSnapshot.getString("phone");
////                                String horaire = documentSnapshot.getString("horaire");
////                                String website = documentSnapshot.getString("site")
//                        if (places.getDistance() != null && places.getnomPlace() != null && places.getAdresse() != null && places.getId() != null) {
////                                        tmp.add(new Place(nomPlace, adresse, horaire, distance, quivient, note, idplace, phone, website));
//                        cleanNDisplay();
////                            DocumentReference doc = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(places.getId());
////                            doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
////                                @Override
////                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
////                                    if (documentSnapshot != null && documentSnapshot.exists()) {
////                                        cleanNDisplay();
////                                    }
////                                }
////                            });
//
//
//                        }
//
//                    }
//                }
//            }
//        });
    }
//
//    public void cleanNDisplayc() {
//        call_all_retaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    liste2place.clear();
//                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
////                                String idplace = documentSnapshot.getString("id");
////                                String nomPlace = documentSnapshot.getString("nomPlace");
////                                String adresse = documentSnapshot.getString("Adresse");
////                                String note = documentSnapshot.getString("note");
////                                String quivient = documentSnapshot.getString("quivient");
////                                String distance = documentSnapshot.getString("distance");
////                                String phone = documentSnapshot.getString("phone");
////                                String horaire = documentSnapshot.getString("horaire");
////                                String website = documentSnapshot.getString("site");
////                                liste2place.add(new Place(nomPlace, adresse, horaire, distance, quivient, note, idplace, phone, website));
//                        Place place = documentSnapshot.toObject(Place.class);
//                        liste2place.add(place);
//
//                    }
//                }
//            }
//        });
//
//    }

    @Override
            public void saveMyPlace(String nom_restaurant, String id) {
        Other.updatemyliste(id,liste2place,1,"go");
        Other.sendItToMyBDDPlease(id,liste2place,"restaurant");
//        }
//        sauvegarde.get(sauvegarde.indexOf(id));
//        call_this_restaurant(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    if (documentSnapshot.exists()) {
//                        if (documentSnapshot.getString("quivient") != null) {
//                            increment = Integer.parseInt(documentSnapshot.getString("quivient"));
//                        } else {
//                            increment = 0;
//                        }
//                        increment = increment + 1;
//                        String stringincrement = String.valueOf(increment);
//                        Map<String, Object> note = new HashMap<>();
//                        note.put("quivient", stringincrement);
//                        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).update(note);
//                    } else {
//                        increment = increment + 1;
//                        String stringincrement = String.valueOf(increment);
//                        Map<String, Object> note = new HashMap<>();
//                        note.put("quivient", stringincrement);
//                        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).set(note);
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
    }

    @Override
    public void unsaveMyPlace(String id) {
        Other.updatemyliste(id,liste2place,-1,"go");
        Other.sendItToMyBDDPlease(id,liste2place,"restaurant");

        me.setMon_choix(" ");
        me.setId_monchoix(" ");

//        Map<String, Object> note = new HashMap<>();
//        note.put("choix", " ");
//
//        firebaseFirestore
//                .collection("collegue").document(me.getMonId())
//                .update(note)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//
//                    }
//                });
    }

    @Override
    public List<Collegue> compareCollegueNPlace(String nomduResto, String idData, Context context) {
////        MutableLiveData<List<Collegue>> liveData = serviceCollegue.GetQuiVient();
        List<Collegue> tmp = new ArrayList<>();
        //                        DocumentReference documentReference = firebaseFirestore.collection("collegue").document(idCollegue);
        serviceCollegue.call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tmp.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        String idCollegue = documentSnapshot.getString("id");
//                        String nomCollegue = documentSnapshot.getString("Nom");
//                        String choixCollegue = documentSnapshot.getString("choix");
//                        String photoCollegue = documentSnapshot.getString("photo");
                        Collegue collegue = documentSnapshot.toObject(Collegue.class);
                        if (collegue.getChoix().equals(nomduResto)) {
                            tmp.add(new Collegue(collegue.getNom(), nomduResto, collegue.getPhoto()));
                        }
                        for (Place place : liste2place) {
                            if(place.getId().equals(idData)){
                                serviceCollegue.getcoworker(nomduResto);
                                int size = tmp.size();
                                place.setquivient(String.valueOf(size));
                                Other.sendItToMyBDDPlease(idData,liste2place,"restaurant");
                            }
                        }
//                                syncMyPlace(idData, String.valueOf(size));
//                        if (idData != null) {
//                            firebaseFirestore.collection("restaurant")
//                                    .document(me.getMonId())
//                                    .collection("Myplace")
//                                    .document(idData.trim())
//                                    .update(note);
//                        }
//                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                                String nomCollegue = documentSnapshot.getString("Nom");
//                                String choixCollegue = documentSnapshot.getString("choix");
//                                String photoCollegue = documentSnapshot.getString("photo");
//                                if (choixCollegue.equals(nomduResto)) {
//                                    tmp.add(new Collegue(nomCollegue, nomduResto, photoCollegue));
//                                }
//                                serviceCollegue.getcoworker(nomduResto);
//                                Map<String, Object> note = new HashMap<>();
//                                int size = tmp.size();
//                                note.put("quivient", String.valueOf(size));
////                                syncMyPlace(idData, String.valueOf(size));
//                                if (idData != null) {
//                                    firebaseFirestore.collection("restaurant")
//                                            .document(me.getMonId())
//                                            .collection("Myplace")
//                                            .document(idData.trim())
//                                            .update(note);
//                                }
////                                Toast.makeText(context, me.getMonNOm()+" va au " + me.getMon_choix(), Toast.LENGTH_SHORT).show();
////                                liveData.setValue(tmp);
//                            }
//                        });
                    }
                }
            }
        });
        return tmp;
    }
//    public void syncMyPlace(String idRestaurant, String quivient) {
//        Map<String, Object> note = new HashMap<>();
//        note.put("quivient", quivient);
//        serviceCollegue.call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                        String idCollegue = documentSnapshot.getString("id");
//                        firebaseFirestore.collection("restaurant").document(idCollegue).collection("Myplace").document(idRestaurant).update(note);
//                    }
//                }
//            }
//        });
//    }
    @Override
    public void ilike(String idresto) {
        Other.updatemyliste(idresto,liste2place,1,"like");
        Other.sendItToMyBDDPlease(idresto,liste2place,"restaurant");

//        call_this_restaurant(idresto).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    if (documentSnapshot.exists()) {
//                        if (documentSnapshot.getString("note") != null) {
//                            increment = Integer.parseInt(documentSnapshot.getString("note"));
//                        } else {
//                            increment = 0;
//                        }
//                        increment = increment + 1;
//                        String stringincrement = String.valueOf(increment);
//                        Map<String, Object> note = new HashMap<>();
//                        Map<String, Object> notelike = new HashMap<>();
//                        note.put("note", stringincrement);
//                        notelike.put("id_restaurant", idresto);
//                        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idresto).update(note);
//                        firebaseFirestore.collection("collegue").document(me.getMonId()).collection("ilike").document(idresto).set(notelike);
//                        serviceCollegue.updateMyLikes();
//                    } else {
//                        increment = increment + 1;
//                        String stringincrement = String.valueOf(increment);
//                        Map<String, Object> note = new HashMap<>();
//                        Map<String, Object> notelike = new HashMap<>();
//                        note.put("note", stringincrement);
//                        notelike.put("id_restaurant", idresto);
//                        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idresto).set(note);
//                        firebaseFirestore.collection("collegue").document(me.getMonId()).collection("ilike").document(idresto).set(notelike);
//                        serviceCollegue.updateMyLikes();
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
    }


    public void addMyChoice(String idrestaurant, Boolean come, Boolean like) {
        Map<String, Object> note = null;
        if (come) {
            note = new HashMap<>();
            note.put("iCome", "true");
            me.setBeNotified(true);
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
        Other.updatemyliste(id,liste2place,-1,"like");
        Other.sendItToMyBDDPlease(id,liste2place,"restaurant");


//        firebaseFirestore.collection("collegue").document(me.getMonId()).collection("ilike").document(resto).delete();
//        Task<DocumentSnapshot> db = call_this_restaurant(resto)
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            if (documentSnapshot.exists()) {
//                                if (documentSnapshot.getString("note") != null) {
//                                    increment = Integer.parseInt(documentSnapshot.getString("note"));
//                                    increment = increment - 1;
//                                    String stringincrement = String.valueOf(increment);
//                                    Map<String, Object> note = new HashMap<>();
//                                    note.put("note", stringincrement);
//                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(resto).update(note);
//                                }
//                            }
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });

    }


    public List<Place> generateListPlace() {
        return liste2place;
    }
}
