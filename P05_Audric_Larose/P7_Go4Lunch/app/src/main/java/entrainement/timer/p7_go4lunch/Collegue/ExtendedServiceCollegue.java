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
import android.widget.Toast;

import androidx.annotation.ArrayRes;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entrainement.timer.p7_go4lunch.Activities.ActivityDetails;
import entrainement.timer.p7_go4lunch.Activities.MainActivity;
import entrainement.timer.p7_go4lunch.BroadCaster_24h;
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
    private List<Collegue> collegues = ListCollegueGenerator.generateNeighbours();
    private RecyclerView.Adapter recyclerView;
    private Me me = new Me();
    private int nbrlikes;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public List<Collegue> generateListCollegue() {
        return collegues;
    }

    public Task<QuerySnapshot> call_all_collegue() {
        CollectionReference db_my_collegue = firebaseFirestore.collection("collegue");
        Task<QuerySnapshot> query_my_collegue = db_my_collegue.get();
        return query_my_collegue;
    }

    public Task<DocumentSnapshot> call_this_collegue(String idCollegue) {
        CollectionReference db_my_collegue = firebaseFirestore.collection("collegue");
        Task<DocumentSnapshot> query_my_collegue = db_my_collegue.document(idCollegue).get();
        return query_my_collegue;
    }

    @Override
    public MutableLiveData<List<Collegue>> getListCollegue() {
        call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    collegues.clear();
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
                                    String idmonchoix = documentSnapshot.getString("id_monchoix");
                                    collegues.add(new Collegue(collegueData, collegueChoix, colleguephoto, idmonchoix));
                                    tmp.add(new Collegue(collegueData, collegueChoix, colleguephoto, idmonchoix));
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
        call_this_collegue(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("choix") != null) {
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
                        boolean aBoolean = Boolean.valueOf(benotified);
                        me.setBeNotified(aBoolean);
                    }
                } else {
                    me.setMon_choix(" ");
                }
            }
        });
    }

    public void updateMyLikes() {
        List<String> likes = new ArrayList<>();
        DocumentReference reference = firebaseFirestore.collection("collegue").document(me.getMonId());
        reference.collection("ilike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String like = documentSnapshot.getString("id_restaurant");
                        likes.add(like);
                        me.setMyLikes(likes);
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

    @Override
    public void newCollegue(Context context, String id, String collegue, String photo, String mail) {
        Me me = new Me();
        me.setMonId(id);
        me.setMonNOm(collegue);
        me.setMaPhoto(photo);
        me.setMonMail(mail);
        Map<String, String> note = new HashMap<>();
        note.put("Nom", collegue);
        note.put("id", id);
        note.put("photo", photo);
        call_this_collegue(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                            note.put("date", documentSnapshot.getString("date"));
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
                            note.put("date", "0");
                        }
                    } else {
                        note.put("choix", " ");
                        note.put("adresse_choix", " ");
                        note.put("id_monchoix", " ");
                        note.put("note_choix", " ");
                        note.put("beNotified", " ");
                        note.put("date", "0");

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
                    note.put("date", "0");

                }
            }
        });


    }

    public List<String> getcoworker(String restaurant) {
        List<String> liste_who_come_with_me = new ArrayList<>();
        call_all_collegue().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.getString("choix").equals(restaurant)) {
                                liste_who_come_with_me.add(documentSnapshot.getString("Nom"));
                                me.setGetCoworker(liste_who_come_with_me);
                            }
                        }
                    }
                }

            }
        });
        return liste_who_come_with_me;
    }

    @Override
    public void updateNotify() {
        Map<String, Object> note = new HashMap<>();
        note.put("beNotified", me.getBeNotified().toString());
        firebaseFirestore.collection("collegue").document(me.getMonId()).update(note);
    }


    @Override
    public void addmychoice(String id, String resto, String adresse, String idRestaurant, String notechoix, String idAncienResto) {
        if (idAncienResto != null) {
        }
        Calendar calendar = Calendar.getInstance();
        String date = String.valueOf(SystemClock.elapsedRealtime());
        Me me = new Me();
        me.setId_monchoix(idRestaurant);
        MutableLiveData<List<Collegue>> mutableLiveData = DI.getService().getListCollegue();
        List<Collegue> liste_collegue = new ArrayList<>();
        Map<String, Object> note = new HashMap<>();
        note.put("date", date);
        note.put("choix", resto);
        note.put("adresse choix", adresse);
        note.put("id_monchoix", idRestaurant);
        note.put("note_choix", notechoix);
        me.setMon_choix(resto);
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
        List<String> names = new ArrayList<>();
        String restaurant_name = me.getMon_choix();
        names = me.getGetCoworker();
        if (names.contains(me.getMonNOm())) {
            names.remove(me.getMonNOm());
        }
        Intent intent = new Intent(context, ActivityDetails.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("channel1", context.getString(R.string.reminder), NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1");
        if (!names.isEmpty() && names != null) {
            builder.setContentTitle(context.getString(R.string.reminder))
                    .setContentText(context.getString(R.string.rendezvous1) + restaurant_name + context.getString(R.string.with) + names + context.getString(R.string.dontforget))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.rdv1) + restaurant_name + context.getString(R.string.with) + names + context.getString(R.string.dontforget1)))
                    .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
        } else {
            builder.setContentTitle(context.getString(R.string.reminder))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.rendezvous) + restaurant_name + context.getString(R.string.dontforgetit1)))
                    .setContentText(context.getString(R.string.rendezvous3)).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
        }
        notificationManager.notify(1, builder.build());

    }

    public void whenNotifyme(Context context, Boolean alarm, String restaurant) {
        Me me = new Me();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Broadcaster.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarm) {
            Boolean isNotified = me.getBeNotified();
            if (isNotified) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, 12);
//                calendar.set(Calendar.MINUTE, 00);
                intent.putExtra("restaurant", restaurant);
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pendingIntent);
                Toast.makeText(context, R.string.rendezvous2, Toast.LENGTH_SHORT).show();
            }
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }

}
