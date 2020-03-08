package entrainement.timer.p7_go4lunch.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
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

public class ExtendedServicePlace implements InterfacePlace {
    private static final String TAG = "ExtendedServicePlace";
    private ExtendedServicePlace servicePlace;
    private ExtendedServiceCollegue serviceCollegue=DI.getService();
    private List<Place> listePlace= new ArrayList<>();
    private int increment=0;
    private String liked="0";
    private MutableLiveData<List<Place>> liste_de_place=new MutableLiveData<List<Place>>();
    private List<Collegue>listedeCollegue= new ArrayList<>();
    private List<Collegue>listedeCollegueSizer= new ArrayList<>();
    private Me me= new Me();
    float[] result = new float[1];


    @Override
    public List<Place> getPlace(Context context,FindCurrentPlaceRequest request, PlacesClient placesClient,GoogleMap mMap) {
        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                servicePlace= DI.getServicePlace();
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
                            String idPlace = String.valueOf(latitude)+ String.valueOf(longitude);
                            List typePlace = placeLikelihood.getPlace().getTypes();
                            Log.d(TAG, "getPlace: "+ placeLikelihood.getPlace().getTypes());
                          //  listePlace.add(new Place(nomPlace, adressePlace, phonePlace, notePlace));
                            LatLng latlongPlace = new LatLng(latitude, longitude);
                           String notePlace = "0";
                            String quivient="0";
                            if(me.getMy_latitude()!=null) {
                                Location.distanceBetween(me.getMy_latitude(), me.getMy_longitude(), latitude, longitude, result);
                            }
                            String distance= String.valueOf(result[0]);
                            String whocome= howManyCome(nomPlace,idPlace);
                            servicePlace.addPlace(nomPlace,adressePlace,notePlace,quivient,idPlace,longitude,latitude,distance);


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                PlacethePlace(context,mMap);

                Exception exception = task.getException();
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());

                }
            }
        });
//        listePlace.add(new Place("hey","tornado","0626445243",3));
        return listePlace;
    }

    void PlacethePlace(Context context, GoogleMap mMap){
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: task.getResult()){
                                String id= documentSnapshot.getString("id");
                                double latitude = Double.parseDouble(documentSnapshot.getString("latitude"));
                                double longitude = Double.parseDouble(documentSnapshot.getString("longitude"));
                                LatLng latlongPlace = new LatLng(latitude, longitude);
                                String nomPlace= documentSnapshot.getString("nomPlace");
                                String adressePlace= documentSnapshot.getString("Adresse");
                                DocumentReference doc= firebaseFirestore.collection("restaurant").document(id);

                                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot2, @Nullable FirebaseFirestoreException e) {
                                        String whocome= documentSnapshot2.getString("quivient");

                                        if (mMap != null) {
                                            if (whocome != null) {
                                                if(Integer.parseInt(whocome)>=1){
                                                    mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                                } else {
                                                    mMap.addMarker(new MarkerOptions().position(latlongPlace).title(nomPlace).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                                                }
                                            }
                                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(Marker marker) {
                                                    Intent intent = new Intent(context, ActivityDetails.class);
                                                    intent.putExtra("nom",nomPlace);
                                                    intent.putExtra("adresse",adressePlace);
                                                    context.startActivity(intent);
                                                    return false;
                                                }
                                            });
                                        }

                                    }
                                });
                            }
                        }
                    }
                });

    }

    void SortPlaceDB(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<Place> tmp= new ArrayList<>();
                            for ( QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String idplace= documentSnapshot.getString("id");
                                        String quivient = documentSnapshot.getString("quivient");
                                        String like= documentSnapshot.getString("note");
                                        if ((Integer.parseInt(quivient)!=0)||(Integer.parseInt(like)!=0)){

                                            } else {
                                            firebaseFirestore.collection("restaurant").document(idplace).delete();
                                        }
                                        }
                                    }
                        }
                });
    }

    void addPlace(String nomplace, String adresse, String note, String quivient, String id, double longitude, double latitude, String distance){
        Me me = new Me();
        Map<String,String> listeplacemap= new HashMap<>();
        listeplacemap.put("nomPlace",nomplace);
        listeplacemap.put("Adresse", adresse);
        listeplacemap.put("note",note);
        listeplacemap.put("quivient",quivient);
        listeplacemap.put("id",id);
        listeplacemap.put("latitude", String.valueOf(latitude));
        listeplacemap.put("longitude", String.valueOf(longitude));
        listeplacemap.put("distance", String.valueOf(distance));
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(id).set(listeplacemap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    @Override
    public MutableLiveData<List<Place>> getListOfPlace() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<Place> tmp= new ArrayList<>();
                            for ( QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String idplace= documentSnapshot.getString("id");
                                DocumentReference doc= firebaseFirestore.collection("restaurant").document(idplace);
                                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        if (documentSnapshot!=null && documentSnapshot.exists()){

                                            Log.d(TAG, "onEvent: "+documentSnapshot.getData());
                                            String nomPlace= documentSnapshot.getString("nomPlace");
                                            String adresse= documentSnapshot.getString("Adresse");
                                            String note= documentSnapshot.getString("note");
                                            String idplace=documentSnapshot.getString("id");
                                            String quivient=documentSnapshot.getString("quivient");
                                            String distance=documentSnapshot.getString("distance");
                                            tmp.add(new Place(nomPlace,adresse,"15:00",distance,quivient,note,idplace));
                                            liste_de_place.setValue(tmp);
                                        }
                                    }
                                });

                            }
                        }
                    }
                });
        return liste_de_place;
    }

    @Override
    public void saveMyPlace(String nom_restaurant, String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot= task.getResult();
                            if (documentSnapshot.exists()){
                                if (documentSnapshot.getString("quivient")!=null) {
                                    increment = Integer.parseInt(documentSnapshot.getString("note"));
                                } else {
                                    increment = 0;
                                }
                                increment = increment + 1;
                                String stringincrement= String.valueOf(increment);
                                Map<String,Object> note = new HashMap<>();
                                note.put("quivient",stringincrement);
                                firebaseFirestore.collection("restaurant").document(id).update(note);
                            } else  {
                                increment = increment + 1;
                                String stringincrement= String.valueOf(increment);
                                Map<String,Object> note = new HashMap<>();
                                note.put("quivient",stringincrement);
                                firebaseFirestore.collection("restaurant").document(id).set(note);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        Map<String,Object> note= new HashMap<>();
//        note.put("nomCollegue",nomCollegue);
//        note.put("photoCollegue",photoCollegue);
//        firebaseFirestore
//                .collection("restaurant")
//                .document(id)
//                .update(note)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });
    }

    @Override
    public void unsaveMyPlace(String nomCollegue, String photoCollegue, String nom_restaurant) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String,Object> note= new HashMap<>();
        note.put("nomCollegue","");
        note.put("photoCollegue","");
        firebaseFirestore
                .collection("restaurant")
                .document(nom_restaurant)
                .update(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public List<Collegue> getMyPlace(String nom_restaurant) {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("restaurant")
                .document(nom_restaurant)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String nomCollegue= documentSnapshot.getString("nomCollegue");
                            String photoCollegue= documentSnapshot.getString("photoCollegue");
                            listedeCollegue.add(new Collegue(nomCollegue,photoCollegue,nom_restaurant));

                        }
                    }
                });
        return listedeCollegue;
    }

    @Override
    public String howManyLke(String nom_restaurant) {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("restaurant")
                .document(nom_restaurant)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            liked= documentSnapshot.getString("note");
                        }
                    }
                });
        return liked;
    }

    @Override
    public String howManyCome(String nom_restaurant, String iDrestaurant) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    listedeCollegueSizer.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        String nomCollegue= documentSnapshot.getString("Nom");
                        String idCollegue= documentSnapshot.getString("id");
                        DocumentReference documentReference=firebaseFirestore.collection("collegue").document(idCollegue);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                String choixCollegue = documentSnapshot.getString("choix");
                                String photoCollegue= documentSnapshot.getString("photo");
                                if (choixCollegue.equals(nom_restaurant)){
                                    listedeCollegueSizer.add(new Collegue(nomCollegue,photoCollegue,nom_restaurant));
                                    Map<String,Object> note = new HashMap<>();
                                    note.put("quivient",String.valueOf(listedeCollegueSizer.size()));
                                    firebaseFirestore.collection("restaurant").document(iDrestaurant).update(note);
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
    public List<Collegue> compareCollegueNPlace(String nomduResto) {
        MutableLiveData <List<Collegue>> liveData= serviceCollegue.GetQuiVient();
        List<Collegue> tmp = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    tmp.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String idCollegue= documentSnapshot.getString("id");
                        DocumentReference documentReference=firebaseFirestore.collection("collegue").document(idCollegue);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                String nomCollegue= documentSnapshot.getString("Nom");
                                String choixCollegue = documentSnapshot.getString("choix");
                                String photoCollegue= documentSnapshot.getString("photo");
                                if (choixCollegue.equals(nomduResto)){
                                    tmp.add(new Collegue(nomCollegue,photoCollegue,nomduResto));

                                }
                                liveData.setValue(tmp);
                            }
                        });

                    }
                }

            }
        });
        return tmp;
    }

public Map<String,String> Oneplace(String id){
Map<String,String> note=new HashMap<>();
FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
firebaseFirestore.collection("restaurant")
        .document(id)
        .get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()){
            note.put("nom",task.getResult().getString("nomPlace"));
            note.put("adresse",task.getResult().getString("Adresse"));
            note.put("note",task.getResult().getString("note"));
            note.put("quivient",task.getResult().getString("quivient"));
        }
    }
});
return note;
}
    @Override
    public void ilike(String resto, String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(resto).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot= task.getResult();
                            if (documentSnapshot.exists()){
                                if (documentSnapshot.getString("note")!=null) {
                                     increment = Integer.parseInt(documentSnapshot.getString("note"));
                                } else {
                                     increment = 0;
                                }
                                increment = increment + 1;
                                String stringincrement= String.valueOf(increment);
                                Map<String,Object> note = new HashMap<>();
                                note.put("note",stringincrement);
                                firebaseFirestore.collection("restaurant").document(resto).update(note);
                            } else  {
                                increment = increment + 1;
                                String stringincrement= String.valueOf(increment);
                                Map<String,Object> note = new HashMap<>();
                                note.put("note",stringincrement);
                                firebaseFirestore.collection("restaurant").document(resto).set(note);
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
    public void unlike(String resto, String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("restaurant").document(resto).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot= task.getResult();
                            if (documentSnapshot.exists()){
                                if (documentSnapshot.getString("increment")!=null) {
                                    increment = Integer.parseInt(documentSnapshot.getString("increment"));
                                increment = increment - 1;
                                String stringincrement= String.valueOf(increment);
                                Map<String,Object> note = new HashMap<>();
                                note.put("increment",stringincrement);
                                firebaseFirestore.collection("restaurant").document(resto).update(note);
                                } else {
                                    increment = 0;
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
