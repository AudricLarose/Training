package entrainement.timer.p7_go4lunch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Place;

public class Other {
    static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Me me=new Me();

    public static void internetVerify(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
        } else {
            Toast.makeText(context, R.string.experience, Toast.LENGTH_LONG).show();
        }
    }


    public interface Callback {
        void onFinish(Place place);
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

    public static  void sendItToMyBDDPlease( String id , List<Place>liste,String restaurant_or_collegue) {
        Me me = new Me();
        Map<String, Object> note = new HashMap<>();
        for (Place object : liste) {
            if (object.getId().equals(id)) {
                switch (restaurant_or_collegue) {
            case "restaurant":
                        note.put("quivient", object.getquivient());
                        note.put("note", object.getnote());
                        firebaseFirestore
                                .collection("restaurant")
                                .document(me.getMonId())
                                .collection("Myplace")
                                .document(object.getId())
                                .update(note);
                        break;
                        case "collegue":
                            note.put("beNotified", object.getquivient());
                            note.put("note", object.getnote());
                            firebaseFirestore
                                    .collection("collegue")
                                    .document(me.getId_monchoix())
                                    .update(note);
                            break;
                        default:
                            // code block
                    }
                }
        }
    }
}
