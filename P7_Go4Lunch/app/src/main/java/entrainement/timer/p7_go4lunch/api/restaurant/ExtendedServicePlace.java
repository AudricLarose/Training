package entrainement.timer.p7_go4lunch.api.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Bases.ActivityDetails;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.model.ApiforOnePlace;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;

public class ExtendedServicePlace implements InterfacePlace {
    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private Map<String, String> mMarker = new HashMap<>();
    private List<Results> listPlaceApi = ListPlaceArray.generatePlaceArray();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<ApiforOnePlace> list2PlaceApi= ListPlaceArray.PLACE_GEN1;

    // Generate the list of Place and placing  Markers  in function of the size list
    public void GetApiPlace(Context context, GoogleMap mMap) {
        ExtendedServicePlace servicePlace = DI.getServicePlace();
        List<Results> resultsList = servicePlace.generateListPlaceAPI();
        for (Results results : resultsList) {
            PlacethePlace(context, mMap);
        }
    }

    // Put info on the place
    void PlacethePlace(Context context, GoogleMap mMap) {
        for (Results place : listPlaceApi) {
            eventPlace(mMap, place);
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(context, ActivityDetails.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("id", mMarker.get(marker.getId()));
                    for (Results results : listPlaceApi) {
                        if (results.getId().equals(mMarker.get(marker.getId()))) {
                            bundle.putSerializable("Place", results);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }

                }
            });
        }
    }

    // Place the Marker
    private void eventPlace(GoogleMap mMap, Results place) {
        if (place.getGeometry().getLocation().getLat() != null) {
            LatLng latLng = new LatLng(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng());
            Marker marker;
            if (Integer.parseInt(place.getWhocome()) >= 1) {
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()).snippet(place.getVicinity()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_white)));
                mMarker.put(marker.getId(), place.getId());
            } else {
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()).snippet(place.getVicinity()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
                mMarker.put(marker.getId(), place.getId());
            }
        }
    }

    // increment the variable "whocome" and put the value to the BDD
    @Override
    public void saveMyPlace(Results results) {
        Other.updatemyliste(results.getId(), listPlaceApi, 1, "go");
        Other.sendItToMyBDDPlease(results.getId(), listPlaceApi);
    }

    // Decrement the variable "whocome" and put the value to th BDD
    @Override
    public void unsaveMyPlace(Results results) {
        Other.updatemyliste(results.getId(), listPlaceApi, -1, "go");
        Other.sendItToMyBDDPlease(results.getId(), listPlaceApi);
    }

    // Return the number collegue in fonction of the size number of people who said they come in this restaurant
    @Override
    public List<Collegue> CompareCollegueNPlace(Results results, Context context, Increment... increments) {
        MutableLiveData<List<Collegue>> liveData = serviceCollegue.GetQuiVient();
        List<Collegue> tmp = new ArrayList<>();
        serviceCollegue.call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tmp.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Collegue collegue = documentSnapshot.toObject(Collegue.class);
                        if (collegue.getChoice().equals(results.getName())) {
                            tmp.add(new Collegue(collegue.getName(), results.getName(), collegue.getPhoto()));
                        }
                        getCoworkerName(tmp);
                        liveData.setValue(tmp);
                    }
                    results.setWhocome(String.valueOf(tmp.size()));
                }
                RefreshingBDD(increments);
            }
        });
        return tmp;
    }



    // Call back of CompareNPlace
    public interface Increment {
        void onFinish();
    }

    // Loops compareCOllegueNPlace many time to set all value update
    private void RefreshingBDD(Increment[] increments) {
        if (increments.length != 0) {
            increments[0].onFinish();
        }
    }

    // To set the Name of Coworker Depending on the Method CompareNPlace
    private void getCoworkerName(List<Collegue> tmp) {
        for (Results results : listPlaceApi) {
            if (results.getId().equals(results.getId())) {
                serviceCollegue.GetCoworkerMethod(results.getName());
            }
        }
    }

    // increment the variable "like" and put the value to the BDD
    @Override
    public void iLike(Results results) {
        Other.updatemyliste(results.getId(), listPlaceApi, 1, "like");
        Other.sendItToMyBDDPlease(results.getId(), listPlaceApi);
    }

    // add the name of me choice or my like to the BDD
    public void addMyChoice(String idrestaurant, Boolean come, Boolean like) {
        if (come) {
            Map<String, Object> note = new HashMap<>();
            note.put("iCome", "true");
            Me.setBeNotified(true);
            firebaseFirestore.collection("restaurant")
                    .document(Me.getMyId())
                    .collection("Myplace")
                    .document(idrestaurant)
                    .collection("choice")
                    .document(Me.getMyName())
                    .set(note);
        }
        if (like) {
            Map<String, Object> note2 = new HashMap<>();
            note2.put("iLike", "true");
            firebaseFirestore.collection("restaurant")
                    .document(Me.getMyId())
                    .collection("Myplace")
                    .document(idrestaurant)
                    .collection("choice")
                    .document(Me.getMyName())
                    .update(note2);
        }
    }
    // Decrement the variable "like" and put the value to the BDD
    @Override
    public void unLike(Results results) {
        Other.updatemyliste(results.getId(), listPlaceApi, -1, "like");
        Other.sendItToMyBDDPlease(results.getId(), listPlaceApi);

    }
    // Return the liste static
    public List<Results> generateListPlaceAPI() {
        return listPlaceApi;
    }
    public List<ApiforOnePlace> generateListPlaceAPIForOnePlace() {
        return list2PlaceApi;
    }


}
