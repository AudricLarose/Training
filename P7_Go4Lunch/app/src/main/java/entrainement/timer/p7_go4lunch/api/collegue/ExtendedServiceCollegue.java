package entrainement.timer.p7_go4lunch.api.collegue;

import android.app.AlarmManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Bases.ActivityDetails;
import entrainement.timer.p7_go4lunch.utils.BroadCaster_24h;
import entrainement.timer.p7_go4lunch.utils.Broadcaster;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.utils.Other;

public class ExtendedServiceCollegue implements InterfaceCollegue {
    private MutableLiveData<List<Collegue>> listLiveData = new MutableLiveData<List<Collegue>>();
    private MutableLiveData<List<Collegue>> quivient = new MutableLiveData<List<Collegue>>();
    private List<Collegue> quivient_array = new ArrayList<>();
    private static final String TAG = "ExtendedServiceCollegue";
    private List<Collegue> collegues = ListCollegueGenerator.generateNeighbours();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Map<String,Object> collegueParResto;


    public List<Collegue> generateListCollegue() {
        return collegues;
    }

    public Task<QuerySnapshot> call_all_collegue() {
        CollectionReference db_my_collegue = firebaseFirestore.collection("collegue");
        Task<QuerySnapshot> query_my_collegue = db_my_collegue.get();
        return query_my_collegue;
    }

//    public void addCollegueesToResto(String place_id, List<Collegue> collegues) {
  //      collegueParResto.put(place_id, collegues);
   // }

    //public void addCollegueeToResto(String place_id, Collegue collegue) {
      //  collegueParResto.put(place_id, Collections.singletonList(collegue));
    //}


   // public List<Collegue> quiVientPourResto(String place_id) {
   //     return collegueParResto.get(place_id);
   // }

   // public Integer combientPourResto(String place_id) {
   //     return collegueParResto.get(place_id).size();
   // }

    public Task<DocumentSnapshot> call_this_collegue(String idCollegue) {
        CollectionReference db_my_collegue = firebaseFirestore.collection("collegue");
        Task<DocumentSnapshot> query_my_collegue = db_my_collegue.document(idCollegue).get();
        return query_my_collegue;
    }

// get the new Collegue
    @Override
    public void newCollegue(Context context, String id, String collegue, String photo, String mail) {
        Me.setMonId(id);
        Me.setMonNOm(collegue);
        Me.setMaPhoto(photo);
        Me.setMonMail(mail);
        Map<String, String> note = new HashMap<>();
        note.put("Nom", collegue);
        note.put("id", id);
        note.put("photo", photo);
        call_this_collegue(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task != null) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.getString("choix") == null) {
                            note.put("choix", " ");
                            note.put("adresse_choix", " ");
                            note.put("id_monchoix", " ");
                            note.put("note_choix", " ");
                            note.put("beNotified", "false");
                            note.put("date", "0");
                            firebaseFirestore.collection("collegue").document(id).set(note);
                    } else {
                            note.put("adresse choix", documentSnapshot.getString("adresse choix"));
                            note.put("choix", documentSnapshot.getString("choix"));
                            note.put("beNotified", documentSnapshot.getString("beNotified"));
                            note.put("date", documentSnapshot.getString("date"));
                            firebaseFirestore.collection("collegue").document(id).set(note);
                        }
                    } else {
                    note.put("choix", " ");
                    note.put("adresse_choix", " ");
                    note.put("id_monchoix", " ");
                    note.put("note_choix", " ");
                    note.put("beNotified", "false");
                    note.put("date", "0");
                    firebaseFirestore.collection("collegue").document(id).set(note);
                }
                }
        });
    }

    @Override
    public void getme(String id) {
        for (Collegue collegue : collegues) {
            if (collegue.getId().equals(id)) {
                Me.setMon_choix(collegue.getChoix());
                Me.setId_monchoix(collegue.getId_monchoix());
                Me.setNoteChoix(collegue.getNote_choix());
                boolean aBoolean = Boolean.valueOf(collegue.getBeNotified());
                Me.setBeNotified(aBoolean);
            }
        }
    }

    @Override
    public void getListCollegue() {
        call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    collegues.clear();
                    List<Collegue> tmp = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String collegueId = documentSnapshot.getString("id");
                        DocumentReference docRef = firebaseFirestore.collection("collegue").document(collegueId);
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Collegue collegue = documentSnapshot.toObject(Collegue.class);
                            collegues.add(collegue);
                            getme(Me.getMonId());
                        } else {
                            System.out.print("Current data: null");
                        }
                    }
                }
            }
        });
    }

    @Override
    public MutableLiveData<List<Collegue>> GetQuiVient() {
        quivient_array.clear();
        quivient.setValue(quivient_array);
        return quivient;
    }

    public void updateMyLikes() {
        List<String> likes = new ArrayList<>();
        DocumentReference reference = firebaseFirestore.collection("collegue").document(Me.getMonId());
        reference.collection("iLike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String like = documentSnapshot.getString("id_restaurant");
                        likes.add(like);
                        Me.setMyLikes(likes);
                    }
                }
            }
        });
    }


    public List<String> GetCoworkerMethod(String restaurant) {
        List<String> liste_who_come_with_me = new ArrayList<>();
        List<Collegue> listedecollegue = collegues;
        for (Collegue collegue : listedecollegue) {
            if (collegue.getChoix().equals(restaurant)) {
                liste_who_come_with_me.add(collegue.getNom());
                Me.setGetCoworker(liste_who_come_with_me);
            }
        }
        return liste_who_come_with_me;
    }

    @Override
    public void updateNotify() {
        Map<String, Object> note = new HashMap<>();
        note.put("beNotified", Me.getBeNotified().toString());
        firebaseFirestore.collection("collegue").document(Me.getMonId()).update(note);
    }


    @Override
    public void addMyChoice(String id, String resto, String adresse, String idRestaurant, String notechoix, String idAncienResto) {
        if (idAncienResto != null) {
        }
        Me.setMon_choix(resto);
        Me.setAdressechoix(adresse);
        Me.setId_monchoix(idRestaurant);
        Other.sendItToMyBDDPleaseatCollegue(id, collegues, resto);
    }

    @Override
    public void twentyFourHourLast(Context context, boolean b) {
        if (b) {
            Intent intent = new Intent(context, BroadCaster_24h.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() * 864 * 100000, pendingIntent);
            Toast.makeText(context, R.string.twentyfourhourcommand, Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifyme(Context context) {
//        List<String> names = new ArrayList<>();
//        if (Me.getMonNOm()!= null){
//            String restaurant_name = Me.getMon_choix();
//        names = Me.getGetCoworker();
//
//        if (names.contains(Me.getMonNOm())) {
//            names.remove(Me.getMonNOm());
//        }
//        Intent intent = new Intent(context, ActivityDetails.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("channel1", context.getString(R.string.reminder), NotificationManager.IMPORTANCE_DEFAULT);
//        notificationManager.createNotificationChannel(notificationChannel);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1");
//        if (names.size() != 0) {
//            builder.setContentTitle(context.getString(R.string.reminder))
//                    .setContentText(context.getString(R.string.rendezvous1) + restaurant_name + context.getString(R.string.with) + names + context.getString(R.string.dontforget))
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.rdv1) + restaurant_name + context.getString(R.string.with) + names + context.getString(R.string.dontforget1)))
//                    .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
//        } else {
//            builder.setContentTitle(context.getString(R.string.reminder))
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.rendezvous) + restaurant_name + context.getString(R.string.dontforgetit1)))
//                    .setContentText(context.getString(R.string.rendezvous3)).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
//        }
//        notificationManager.notify(1, builder.build());
//    }

}
    public void whenNotifyme(Context context, Boolean alarm, String restaurant) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, Broadcaster.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        if (alarm) {
//            Boolean isNotified = Me.getBeNotified();
//            if (isNotified) {
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(System.currentTimeMillis());
////                calendar.set(Calendar.HOUR_OF_DAY, 12);
////                calendar.set(Calendar.MINUTE, 00);
//                intent.putExtra("restaurant", restaurant);
//                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pendingIntent);
//                Toast.makeText(context, R.string.rendezvous2, Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            alarmManager.cancel(pendingIntent);
//        }
    }

    public void dontaddMyChoice(String monId, String name, String vicinity, String id, String etoileData, String id_monchoix) {
        Me.setMon_choix(" ");
        Me.setAdressechoix(" ");
        Me.setId_monchoix(" ");
        Other.sendItToMyBDDPleaseatCollegue(id, collegues, " ");
    }
}
