package entrainement.timer.p7_go4lunch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Place;

public class Other {
    static ExtendedServicePlace servicePlace = DI.getServicePlace();
    static ExtendedServiceCollegue serviceCollegue = DI.getService();
    static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static void internetVerify(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
        } else {
            Toast.makeText(context, R.string.experience, Toast.LENGTH_LONG).show();
        }
    }

    public static <T> void dbconexion(Class<T> objet, String collection, Place place, Callback onFinish) {
        firebaseFirestore.collection(collection).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Place place = documentSnapshot.toObject(Place.class);
                            onFinish.onFinish(place);

                        }

                    }
                });
    }

    public static void updatemyliste(String id, List<Place> liste2place, int increment, String go_or_like) {
        List<Place> sauvegarde = liste2place;
        for (Place place : sauvegarde) {
            if (place.getId().equals(id)) {
                switch (go_or_like) {
                    case "go":
                        place.setquivient(String.valueOf(Integer.valueOf(place.getquivient()) + increment));
                        break;
                    case "like":
                        place.setnote(String.valueOf(Integer.valueOf(place.getnote()) + increment));
                        break;
                    default:
                }

            }
        }
    }

    public static void sendItToMyBDDPlease(String id, List<Place> liste, String restaurant_or_collegue) {
        Map<String, Object> note = new HashMap<>();
        for (Place object : liste) {
            if (object.getId().equals(id)) {
                switch (restaurant_or_collegue) {
                    case "restaurant":
                        note.put("quivient", object.getquivient());
                        note.put("note", object.getnote());
                        firebaseFirestore
                                .collection("restaurant")
                                .document(Me.getMonId())
                                .collection("Myplace")
                                .document(object.getId())
                                .update(note);
                        break;
                    default:
                        // code block
                }
            }
        }
    }

    public static void sendItToMyBDDPleaseatCollegue(String id, List<Collegue> liste, String resto) {
        Map<String, Object> note = new HashMap<>();
        for (Collegue collegue : liste) {
            if (collegue.getId().equals(id)) {
                collegue.setChoix(resto);
                String date = String.valueOf(SystemClock.elapsedRealtime());
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

    public static void decrement(String ancienresto) {
        List<Place> liste = servicePlace.generateListPlace();
        for (Place place : liste) {
            if (place.getnomPlace().equals(ancienresto)) {
                place.setquivient(String.valueOf(Integer.valueOf(place.getquivient()) - 1));
                sendItToMyBDDPlease(place.getId(), liste, "restaurant");
            }
        }
    }


    public interface Callback {
        void onFinish(Place place);
    }

    public interface ThegoodPlace {
        void GoodPlace(Place place);
    }

    public static void theGoodPlace(String id, ThegoodPlace thegoodPlace, String... nomplace) {
        List<Place> liste = servicePlace.generateListPlace();
        for (Place place : liste) {
            if (place.getId().equals(id) || (place.getnomPlace().equals(nomplace))) {
                thegoodPlace.GoodPlace(place);
            }
        }
    }
    public interface ThegoodCollegue {
        void GoodCollegue(Collegue collegue);
    }

    public static void theGoodCollegue(String id, ThegoodCollegue thegoodCollegue) {
        List<Collegue> liste = serviceCollegue.generateListCollegue();
        for (Collegue collegue: liste) {
            if (collegue.getId().equals(id)) {
                thegoodCollegue.GoodCollegue(collegue);
            }
        }
    }

    public static void removeCollegue(){
        theGoodCollegue(Me.getMonId(), new ThegoodCollegue() {
            @Override
            public void GoodCollegue(Collegue collegue) {
                List<Collegue> listecollegue=serviceCollegue.generateListCollegue();
                listecollegue.remove(collegue);
            }
        });
    }
}