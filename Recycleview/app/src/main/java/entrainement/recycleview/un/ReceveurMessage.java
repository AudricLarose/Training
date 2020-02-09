package entrainement.recycleview.un;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ReceveurMessage extends BroadcastReceiver {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            androidx.core.app.RemoteInput remoteInput= new RemoteInput.Builder("key_text_reply")
                    .setLabel("Ajouter").build();
            Intent Outintent = new Intent(context, ReceveurMessage.class);
            PendingIntent replypendingintent= PendingIntent.getBroadcast(context,0,Outintent,0);
            NotificationCompat.Action action =new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background, "Ajouter une tache",replypendingintent)
                    .addRemoteInput(remoteInput).build();
            Intent intent1= new Intent (context,MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent1,0);
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel= new NotificationChannel("channel1", " Reminder",NotificationManager.IMPORTANCE_LOW);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"channel1");
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setContentTitle("To-Do")
                    .setContentText("Acceder a la liste")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .addAction(action)
                    .setSmallIcon(R.mipmap.ic_launcher);
            notificationManager.notify(1,builder.build());        }
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String Texte = remoteInput.getCharSequence("key_text_reply").toString();
                Map<String, String> note= new HashMap<>();
                note.put("titre",  Texte);
                db.collection("notebook").document(Texte)
                        .set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
                                Toast.makeText(context,"Envoyé !",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Pas de Reseau",Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }

