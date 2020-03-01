package entrainement.timer.p7_go4lunch.Collegue;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Activities.ActivityDetails;
import entrainement.timer.p7_go4lunch.Broadcaster;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.R;

public class ExtendedServiceCollegue implements InterfaceCollegue {
    private MutableLiveData<List<Collegue>> listLiveData=new MutableLiveData<List<Collegue>>();
    private List<Collegue> addMe=new ArrayList<>();
    private static final String TAG = "ExtendedServiceCollegue";
    private RecyclerView.Adapter recyclerView;
    @Override
    public MutableLiveData<List<Collegue>> getListCollegue() {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<Collegue> tmp= new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                String collegueId= documentSnapshot.getString("id");
                                DocumentReference docRef = firebaseFirestore.collection("collegue").document(collegueId);
                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            System.err.println("Listen failed: " + e);
                                            return;
                                        }

                                        if (documentSnapshot != null && documentSnapshot.exists()) {
                                            Log.d(TAG, "onEvent: "  + documentSnapshot.getData());
                                            String collegueData = documentSnapshot.getString("Nom");
                                            String colleguephoto= documentSnapshot.getString("photo");
                                            String collegueChoix= documentSnapshot.getString("choix");
                                            tmp.add(new Collegue(collegueData,collegueChoix,colleguephoto));
                                            listLiveData.setValue(tmp);
                                        } else {
                                            System.out.print("Current data: null");
                                        }
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
        return listLiveData;
    }

    @Override
    public void getme(String id){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue").document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            Me me= new Me();
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("choix") != null) {
//                    if (documentSnapshot.getString("like").isEmpty()) {
//                        me.setMon_like("");
//                    } else {
//                        String like = documentSnapshot.getString("like");
//                        me.setMon_like(like);
//                    }
                    if (documentSnapshot.getString("choix").isEmpty()) {
                        me.setMon_choix(" ");
                    }else {
                        String choix = documentSnapshot.getString("choix");
                        me.setMon_choix(choix);
                    }
                } else {
                    me.setMon_choix(" ");
                }
            }
        });
 }
    @Override
    public void newCollegue(Context context,String id , String collegue,String photo){
        Me me= new Me();
        me.setMonId(id);
        me.setMonNOm(collegue);
        me.setMaPhoto(photo);

        Map<String, String> note= new HashMap<>();
        note.put("Nom", collegue);
        note.put("id",id);
        note.put("photo",photo);
        note.put("choix"," ");
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue").document(id).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void addmychoice(String id, String resto, String adresse) {
            MutableLiveData<List<Collegue>> mutableLiveData= DI.getService().getListCollegue();
            List<Collegue> liste_collegue = new ArrayList<>();
            Map<String, Object> note= new HashMap<>();
            note.put("choix", resto);
            note.put("adresse choix",adresse);
            FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
            firebaseFirestore.collection("collegue").document(id).update(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifyme (String restaurant, String id, Context context){
        Intent intent  = new Intent(context, ActivityDetails.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("channel1", "Rappel",NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channel1");
        builder.setContentTitle("Rappel").setContentText("Vous avez rendez vous a"+restaurant+ "ne l'oubliez pas ").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);

    }

    public void whenNotifyme (Context context, Boolean alarm){
        if (alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent  = new Intent(context, Broadcaster.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        } else {
            // annuler
        }
    }

}
