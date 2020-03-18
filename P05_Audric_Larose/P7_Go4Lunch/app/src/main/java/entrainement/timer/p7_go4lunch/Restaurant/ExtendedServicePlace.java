package entrainement.timer.p7_go4lunch.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
                            String whocome = howManyCome(nomPlace, idPlace);
                            servicePlace.addPlace(nomPlace, adressePlace, quivient,note, idPlace, longitude, latitude, distance,typePlace);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                PlacethePlace(context, mMap,progressBar);

                Exception exception = task.getException();
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());

                }
            }
        });
        return listePlace;
    }

    void PlacethePlace(Context context, GoogleMap mMap, ProgressBar progressBar) {
        Map<String, String> note = new HashMap<>();

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getString("id");
                                double latitude = 0;
                                double longitude = 0;
                                if (documentSnapshot.getString("latitude")!=null) {
                                    latitude = Double.parseDouble(documentSnapshot.getString("latitude"));
                                    longitude = Double.parseDouble(documentSnapshot.getString("longitude"));
                                }
                                LatLng latlongPlace = new LatLng(latitude, longitude);
                                String nomPlace = documentSnapshot.getString("nomPlace");
                                String adressePlace = documentSnapshot.getString("Adresse");
                                String notes = documentSnapshot.getString("note");
                                note.put(adressePlace,notes);
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Toast.makeText(context, mMarker.get(marker.getId()), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, ActivityDetails.class);
                                            intent.putExtra("id", mMarker.get(marker.getId()));
                                            intent.putExtra("nom",marker.getTitle());
                                            intent.putExtra("adresse",marker.getSnippet());
                                            intent.putExtra("etoile",note.get(marker.getSnippet()));
                                            context.startActivity(intent);
                                    }
                                });
                                DocumentReference doc = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id);

                                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot2, @Nullable FirebaseFirestoreException e) {
                                        String whocome = documentSnapshot2.getString("quivient");
                                            if (whocome != null) {
                                                if (Integer.parseInt(whocome) >= 1) {
                                                    marker= mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).snippet(adressePlace).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_white)));
                                                    mMarker.put(marker.getId(),id);
                                                } else {
                                                     marker=  mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).snippet(adressePlace).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
                                                    mMarker.put(marker.getId(),id);

                                                }
                                            }

                                    }
                                });
                            }
                        }
                    }
                });

    }

    public List<String> getPlaceInfo(String name){
        List<String> infoPlace=new ArrayList<>();
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").whereEqualTo("nomPlace",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        String etoile= documentSnapshot.getString("note");
                        String note= documentSnapshot.getString("quivient");
                        String id= documentSnapshot.getString("id");
                        infoPlace.add(etoile);
                        infoPlace.add(note);
                        infoPlace.add(id);
                    }
                }
            }
        });
        return infoPlace;
    }

    void SortPlaceDB() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Place> tmp = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String idplace = documentSnapshot.getString("id");
                                String quivient = documentSnapshot.getString("quivient");
                                String like = documentSnapshot.getString("note");
                                String distance= documentSnapshot.getString("distance");
                                if ((quivient != null) || (like != null) ||(distance!=null) ) {
                                    if ((Integer.parseInt(quivient) != 0) || (Integer.parseInt(like) != 0) ) {

                                    } else {
                                        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace).delete();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    void addPlace(String nomplace, String adresse, String quivient, String note, String id, double longitude, double latitude, String distance, String type) {
        Me me = new Me();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        Map<String, String> listeplacemap = new HashMap<>();
        listeplacemap.put("nomPlace", nomplace);
        listeplacemap.put("Adresse", adresse);
        listeplacemap.put("quivient", quivient);
        listeplacemap.put("id", id);
        listeplacemap.put("note", note);
        listeplacemap.put("latitude", String.valueOf(latitude));
        listeplacemap.put("longitude", String.valueOf(longitude));
        listeplacemap.put("distance", String.valueOf(distance));
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
            DocumentSnapshot documentSnapshot =task.getResult();
            String idresto= documentSnapshot.getString("id");
                if (idresto==null) {
                    if (Integer.parseInt(distance)<300){
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
});



    }

    @Override
    public MutableLiveData<List<Place>> getListOfPlace() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference =firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace");
        reference.orderBy("quivient").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Place> tmp = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String idplace = documentSnapshot.getString("id");

                                if (idplace!=null) {
                                    DocumentReference doc = firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idplace);
                                    doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                            if (documentSnapshot != null && documentSnapshot.exists()) {

                                                Log.d(TAG, "onEvent: " + documentSnapshot.getData());
                                                String nomPlace = documentSnapshot.getString("nomPlace");
                                                String adresse = documentSnapshot.getString("Adresse");
                                                String note = documentSnapshot.getString("note");
                                                String idplace = documentSnapshot.getString("id");
                                                String quivient = documentSnapshot.getString("quivient");
                                                String distance = documentSnapshot.getString("distance");
                                                tmp.add(new Place(nomPlace, adresse, "15:00", distance, quivient, note, idplace));
                                                liste_de_place.setValue(tmp);
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    }
                });
        return liste_de_place;
    }

    @Override
    public void saveMyPlace(String nom_restaurant, String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.getString("quivient") != null) {
                                    increment = Integer.parseInt(documentSnapshot.getString("note"));
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
    public void unsaveMyPlace(String nomCollegue, String photoCollegue, String nom_restaurant) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> note = new HashMap<>();
        note.put("nomCollegue", "");
        note.put("photoCollegue", "");
        firebaseFirestore
                .collection("restaurant").document(me.getMonId()).collection("Myplace")
                .document(nom_restaurant)
                .update(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public List<String> getMyPlace(String id) {
        List<String> myplace=new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference= firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace")
                .document(id);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        String nomPlace = documentSnapshot.getString("nomPlace");
                        String Adresse = documentSnapshot.getString("Adresse");
                        String note = documentSnapshot.getString("note");
                        String quivient = documentSnapshot.getString("quivient");
                        myplace.add(0, nomPlace);
                        myplace.add(1, Adresse);
                        myplace.add(2, note);
                        myplace.add(3, quivient);
                    }
                                });

        return myplace;
    }

    @Override
    public String howManyLke(String nom_restaurant,String iDrestaurant) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listedeCollegueSizer.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String nomCollegue = documentSnapshot.getString("Nom");
                        String idCollegue = documentSnapshot.getString("id");
                        DocumentReference documentReference = firebaseFirestore.collection("collegue").document(idCollegue).collection("ilike").document(iDrestaurant);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                String choixCollegue = documentSnapshot.getString("choix");
                                String photoCollegue = documentSnapshot.getString("photo");
                                if (choixCollegue.equals(nom_restaurant)) {
                                    listedeCollegueSizer.add(new Collegue(nomCollegue, photoCollegue, nom_restaurant));
                                    Map<String, Object> note = new HashMap<>();
                                    note.put("quivient", String.valueOf(listedeCollegueSizer.size()));
                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(iDrestaurant).update(note);
                                }
                            }
                        });

                    }
                }
            }
        });
        return String.valueOf(listedeCollegueSizer.size());
    }

    @Override
    public String howManyCome(String nom_restaurant, String iDrestaurant) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listedeCollegueSizer.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String nomCollegue = documentSnapshot.getString("Nom");
                        String idCollegue = documentSnapshot.getString("id");
                        DocumentReference documentReference = firebaseFirestore.collection("collegue").document(idCollegue);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                String choixCollegue = documentSnapshot.getString("choix");
                                String photoCollegue = documentSnapshot.getString("photo");
                                if (choixCollegue.equals(nom_restaurant)) {
                                    listedeCollegueSizer.add(new Collegue(nomCollegue, photoCollegue, nom_restaurant));
                                    Map<String, Object> note = new HashMap<>();
                                    note.put("quivient", String.valueOf(listedeCollegueSizer.size()));
                                    firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(iDrestaurant).update(note);
                                }
                            }
                        });

                    }
                }
            }
        });
        return String.valueOf(listedeCollegueSizer.size());
    }

    @Override
    public List<Collegue> compareCollegueNPlace(String nomduResto, String idData) {
        MutableLiveData<List<Collegue>> liveData = serviceCollegue.GetQuiVient();
        List<Collegue> tmp = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                String photoCollegue = documentSnapshot.getString("photo");
                                if (choixCollegue.equals(nomduResto)) {
                                    tmp.add(new Collegue(nomCollegue, photoCollegue, nomduResto));
                                }
                                Map<String,Object> note= new HashMap<>();
                                int size = tmp.size();
                                note.put("quivient",String.valueOf(size));
                                firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idData.trim()).update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: ");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: ");
                                    }
                                });

                                liveData.setValue(tmp);
                            }
                        });

                    }
                }

            }
        });
        return tmp;
    }

   // public int number_Of_collegue(String name_restaurant){
        //return compareCollegueNPlace(name_restaurant).size();
   // }

    public Map<String, String> Oneplace(String id) {
        Map<String, String> note = new HashMap<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            note.put("nom", task.getResult().getString("nomPlace"));
                            note.put("adresse", task.getResult().getString("Adresse"));
                            note.put("note", task.getResult().getString("note"));
                            note.put("quivient", task.getResult().getString("quivient"));
                        }
                    }
                });
        return note;
    }

    @Override
    public void ilike(String idresto) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(idresto).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public  void addMyChoice(String idrestaurant,Boolean come, Boolean like){
        Map<String,Object> note= null;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    if (come) {
            note = new HashMap<>();
            note.put("iCome","true");
            firebaseFirestore.collection("restaurant")
                    .document(me.getMonId())
                    .collection("Myplace")
                    .document(idrestaurant)
                    .collection("choice")
                    .document(me.getMonNOm())
                    .set(note);
        }

        if (like) {
            Map<String,Object> note2 = new HashMap<>();
            note2.put("iLike","true");
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
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue").document(me.getMonId()).collection("ilike").document(resto).delete();
        firebaseFirestore.collection("restaurant").document(me.getMonId()).collection("Myplace").document(resto).get()
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


}
