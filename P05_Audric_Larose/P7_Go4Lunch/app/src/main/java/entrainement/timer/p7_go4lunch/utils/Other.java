package entrainement.timer.p7_go4lunch.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Constants;
import entrainement.timer.p7_go4lunch.model.Inter;
import entrainement.timer.p7_go4lunch.model.Location;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Place;
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
    static ExtendedServicePlace servicePlace = DI.getServicePlace();
    static ExtendedServiceCollegue serviceCollegue = DI.getService();
    static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    static List<Results> listePlaceApi = servicePlace.generateListPlaceAPI();



    public static void updatemyliste(String id, List<Results> liste2place, int increment, String go_or_like) {
        List<Results> sauvegarde = liste2place;
        for (Results place : sauvegarde) {
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

    public static void sendItToMyBDDPlease(String id, List<Results> liste) {
        Map<String, Object> note = new HashMap<>();
        for (Results object : liste) {
            if (object.getId().equals(id)) {
                note.put("nom", object.getName());
                note.put("id", object.getId());
                note.put("whocome", object.getWhocome());
                note.put("like", object.getLike());
                firebaseFirestore
                        .collection("restaurant")
                        .document(Me.getMonId())
                        .collection("Myplace")
                        .document(object.getId())
                        .set(note);
            }
        }
    }
 public static void GPSOnVerify(Context context){
     LocationManager locationManager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
     if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
         AlertGPSInactif(context);
     }
 }

    private static void AlertGPSInactif(Context context) {
        AlertDialog alertDialog= AlertGPS(context);
        alertDialog.show();
    }
    private static AlertDialog AlertGPS(Context context){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setMessage("Vous devez absoulement allumer votre GPS pour pouvoir profiter de cette Application").setTitle("Alert GPS").setPositiveButton("J'ai bien activé mon GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Other.GPSOnVerify(context);
            }
        });
        return  builder.create();
    }

    public static void internetVerify(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ) {
        } else {
            Toast.makeText(context, R.string.experience, Toast.LENGTH_LONG).show();
        }
    }

    public static void checkrealtime(Adapterinterf adapterinterf) {
        firebaseFirestore.collection("restaurant").document(Me.getMonId()).collection("Myplace")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<Results> resultsBDD = queryDocumentSnapshots.toObjects(Results.class);
                        for (int i = 0; i < listePlaceApi.size(); i++) {
                            for (Results resultBDD : resultsBDD) {
                                String resultBDDId = resultBDD.getId();
                                String id = listePlaceApi.get(i).getId();
                                if (resultBDDId.equals(id)) {
                                    listePlaceApi.get(i).setWhocome(resultBDD.getWhocome());
                                }
                            }
                        }
                        adapterinterf.onFinish(listePlaceApi);
                    }
                });
    }

    public static void sendItToMyBDDPleaseatCollegue(String id, List<Collegue> liste, String resto) {
        Map<String, Object> note = new HashMap<>();
        for (Collegue collegue : liste) {
            if (collegue.getId().equals(id)) {
                collegue.setChoix(resto);
                String date = valueOf(SystemClock.elapsedRealtime());
                note.put("date", date);
                note.put("choix", Me.getMon_choix());
                note.put("adresse choix", Me.getAdressechoix());
                note.put("id_monchoix", Me.getId_monchoix());
                note.put("note_choix", Me.getNoteChoix());
                firebaseFirestore
                        .collection("collegue")
                        .document(id)
                        .update(note);
            }
        }
    }

    public static void EraseMyBddWisely() {
        List<Results> resultsList = servicePlace.generateListPlaceAPI();
        for (Results results : resultsList) {
            if (results.getWhocome().equals("0")) {
                firebaseFirestore.collection("restaurant").document(Me.getMonId()).collection("Myplace").document(results.getId()).delete();
            }
        }
    }

    public static void decrement(String ancienresto) {
        List<Results> resultsList = servicePlace.generateListPlaceAPI();
        for (Results place : resultsList) {
            if (place.getName().equals(ancienresto)) {
                place.setWhocome(valueOf(Integer.valueOf(place.getWhocome()) - 1));
                sendItToMyBDDPlease(place.getId(), resultsList);
            }
        }
    }

    public static <T> void getCollection(String collectionName, Class<T> tClass, OnFinish<T> onFinish, String... order) {
        CollectionReference collection = firebaseFirestore
                .collection(collectionName).document(Me.getMonId()).collection("Myplace");
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

    public static void theGoodPlace(String id, ThegoodPlace thegoodPlace, String... nomplace) {
        List<Results> liste = servicePlace.generateListPlaceAPI();
        for (Results place : liste) {
            if (place.getId().equals(id) || (place.getName().equals(nomplace))) {
                thegoodPlace.GoodPlace(place);
            }
        }
    }

    public static void refreshing(Context context, Refreshing refreshing) {
        count = 0;
        List<Results> listPlace = servicePlace.generateListPlaceAPI();
        for (Results place : listPlace) {
            servicePlace.compareCollegueNPlace(place, context, new ExtendedServicePlace.Increment() {
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

    public static void SortedByDistance() {
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
    }

    public static void SortedByLike() {
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
    }

    public static void SortedByWhoCome() {
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
    }

    public static String getUrlimage(String photo_reference,String width){
        String url="https://maps.googleapis.com/maps/api/place/photo?&photoreference="+photo_reference+"&maxwidth="+"300"+"&key=AIzaSyC85iU9E8o4rOCwv2UurWP31fGXaTRcL8c";
        return url;
    }
    public static void CallApiPlease(Context context, String location, TheCalling callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(ScalarsConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Inter intera = retrofit.create(entrainement.timer.p7_go4lunch.model.Inter.class);
        Call<String> call = intera.getplaces("ChIJN1t_tDeuEmsRUsoyG83frY4", "AIzaSyC85iU9E8o4rOCwv2UurWP31fGXaTRcL8c", location, "restaurant", "3000", "cruise");
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Pas de reponses" + response.code());
                }
                String listeplace = response.body();
                String readable = response.body().toString();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(readable);
                    Gson gson = new Gson();
                    PlaceApi placeApi = gson.fromJson(readable, PlaceApi.class);
                    servicePlace.generateListPlaceAPI().clear();
                    servicePlace.generateListPlaceAPI().addAll(placeApi.getResults());
                    for (Results results : placeApi.getResults()) {
                        results.setLike("0");
                        results.setWhocome("0");
                    }
                    refreshing(context, new Refreshing() {
                        @Override
                        public void onFinish() {
                            Other.EraseMyBddWisely();
                            callback.onFinish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.getMessage());
            }
        });
    }

    public interface TheCalling {
        void onFinish();
    }

    public interface Adapterinterf {
        void onFinish(List<Results> listePlaceApi);
    }

    public interface OnFinish<T> {
        void success(List<T> objects);

        void error(Exception e);
    }


    public interface Callback {
        void onFinish(Place place);
    }

    public interface ThegoodPlace {
        void GoodPlace(Results results);
    }

    public interface ThegoodCollegue {
        void GoodCollegue(Collegue collegue);
    }

    public interface Refreshing {
        void onFinish();
    }
}