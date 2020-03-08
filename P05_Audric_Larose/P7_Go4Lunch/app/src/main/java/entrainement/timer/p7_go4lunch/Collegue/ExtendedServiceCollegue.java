package entrainement.timer.p7_go4lunch.Collegue;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
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
import entrainement.timer.p7_go4lunch.Activities.MainActivity;
import entrainement.timer.p7_go4lunch.Broadcaster;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.R;

public class ExtendedServiceCollegue implements InterfaceCollegue {
    private MutableLiveData<List<Collegue>> listLiveData = new MutableLiveData<List<Collegue>>();
    private MutableLiveData<List<Collegue>> quivient = new MutableLiveData<List<Collegue>>();
    private List<Collegue> quivient_array = new ArrayList<>();
    private List<Collegue> addMe = new ArrayList<>();
    private static final String TAG = "ExtendedServiceCollegue";
    private RecyclerView.Adapter recyclerView;
    private Me me = new Me();

    @Override
    public MutableLiveData<List<Collegue>> getListCollegue() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Collegue> tmp = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String collegueId = documentSnapshot.getString("id");
                                DocumentReference docRef = firebaseFirestore.collection("collegue").document(collegueId);
                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            System.err.println("Listen failed: " + e);
                                            return;
                                        }

                                        if (documentSnapshot != null && documentSnapshot.exists()) {
                                            Log.d(TAG, "onEvent: " + documentSnapshot.getData());
                                            String collegueData = documentSnapshot.getString("Nom");
                                            String colleguephoto = documentSnapshot.getString("photo");
                                            String collegueChoix = documentSnapshot.getString("choix");
                                            tmp.add(new Collegue(collegueData, collegueChoix, colleguephoto));
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
    public void getme(String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue").document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                    } else {
                        String choix = documentSnapshot.getString("choix");
                        String id_choix = documentSnapshot.getString("id_monchoix");
                        String adresse = documentSnapshot.getString("adresse choix");
                        String note = documentSnapshot.getString("note_choix");
                        String benotified = documentSnapshot.getString("beNotified");
                        me.setMon_choix(choix);
                        me.setId_monchoix(id_choix);
                        me.setAdressechoix(adresse);
                        me.setNoteChoix(note);
                        me.setBeNotified(Boolean.getBoolean(benotified));
                    }
                } else {
                    me.setMon_choix(" ");
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

    @Override
    public void newCollegue(Context context, String id, String collegue, String photo, String mail) {

        Me me = new Me();
        me.setMonId(id);
        me.setMonNOm(collegue);
        me.setMaPhoto(photo);
        me.setMonMail(mail);

        Map<String, String> note = new HashMap<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        note.put("Nom", collegue);
        note.put("id", id);
        note.put("photo", photo);


        firebaseFirestore.collection("collegue").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task != null) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.getString("choix") != null) {
                        if ((!documentSnapshot.getString("choix").isEmpty())) {
                            note.put("choix", documentSnapshot.getString("choix"));
                            note.put("adresse choix", documentSnapshot.getString("adresse choix"));
                            note.put("id_monchoix", documentSnapshot.getString("id_monchoix"));
                            note.put("note_choix", documentSnapshot.getString("note_choix"));
                            note.put("beNotified", documentSnapshot.getString("beNotified"));
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
                        } else {

                            note.put("choix", " ");
                            note.put("adresse_choix", " ");
                            note.put("id_monchoix", " ");
                            note.put("note_choix", " ");
                            note.put("beNotified", " ");
                        }
                    } else {
                        note.put("choix", " ");
                        note.put("adresse_choix", " ");
                        note.put("id_monchoix", " ");
                        note.put("note_choix", " ");
                        note.put("beNotified", " ");
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
                } else {
                    note.put("choix", " ");
                    note.put("adresse_choix", " ");
                    note.put("id_monchoix", " ");
                    note.put("note_choix", " ");
                    note.put("beNotified", " ");
                }
            }
        });


    }

    @Override
    public void updateNotify() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> note = new HashMap<>();
        note.put("beNotified", me.getBeNotified().toString());
        firebaseFirestore.collection("collegue").document(me.getMonId()).update(note);
    }


    @Override
    public void addmychoice(String id, String resto, String adresse, String idRestaurant, String notechoix) {
        Me me = new Me();
        me.setId_monchoix(idRestaurant);
        MutableLiveData<List<Collegue>> mutableLiveData = DI.getService().getListCollegue();
        List<Collegue> liste_collegue = new ArrayList<>();
        Map<String, Object> note = new HashMap<>();
        note.put("choix", resto);
        note.put("adresse choix", adresse);
        note.put("id_monchoix", idRestaurant);
        note.put("note_choix", notechoix);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
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
    public void notifyme(String restaurant, Context context) {
        Intent intent = new Intent(context, ActivityDetails.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("channel1", "Rappel", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1");
        builder.setContentTitle("Rappel").setContentText("Vous avez rendez vous a " + restaurant + ", ne l'oubliez pas ").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
        notificationManager.notify(1, builder.build());

    }

    public void whenNotifyme(Context context, Boolean alarm, String restaurant) {
        Me me = new Me();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Broadcaster.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (alarm) {
            Boolean isNotified = me.getBeNotified();
            if (isNotified) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 00);
                calendar.set(Calendar.MINUTE, 30);
                intent.putExtra("restaurant", restaurant);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() * 12000, pendingIntent);
            }
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }

}
