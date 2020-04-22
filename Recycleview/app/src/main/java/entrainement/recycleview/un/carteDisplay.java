package entrainement.recycleview.un;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class carteDisplay extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "carteDisplay";
    private List<ExempleItem> liste = new ArrayList<>();
   private int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_carte_display);
        recyclerView= findViewById(R.id.RecycleCard);
        Bundle extra= getIntent().getExtras();
        if (extra!=null) {
            final String layout_name = extra.getString("layout");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CardAdapteur cardAdapteur= new CardAdapteur(layout_name);

//                                    liste.add(new ExempleItem(etape2, "2"));
//                                    liste.add(new ExempleItem(etape3, "3"));
//                                    liste.add(new ExempleItem(etape4, "4"));
//                                    liste.add(new ExempleItem(etape5, "5"));
//                                    liste.add(new ExempleItem(etape6, "6"));
//                                    liste.add(new ExempleItem(etape7, "7"));
//                                    liste.add(new ExempleItem(etape8, "8"));
//                                    liste.add(new ExempleItem(etape9, "9"));
//                                    liste.add(new ExempleItem(etape10, "10"));
            db.collection("important").document(layout_name)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e!=null){
                                Log.d(TAG, "onEvent: error");
                            }
                            if (documentSnapshot!=null && documentSnapshot.exists()){
                                Map<String,Object> document =documentSnapshot.getData();
                            String etape1 = document.get("phase1").toString();
                            String etape2 = document.get("phase2").toString();
                            String etape3 = document.get("phase3").toString();
                            String etape4 = document.get("phase4").toString();
                            String etape5 = document.get("phase5").toString();
                            String etape6 = document.get("phase6").toString();
                            String etape7 = document.get("phase7").toString();
                            String etape8 = document.get("phase8").toString();
                            String etape9 = document.get("phase9").toString();
                            String etape10 = document.get("phase10").toString();
                            liste.add(new ExempleItem(etape1, "1"));
                                    liste.add(new ExempleItem(etape2, "2"));
                                    liste.add(new ExempleItem(etape3, "3"));
                                    liste.add(new ExempleItem(etape4, "4"));
                                    liste.add(new ExempleItem(etape5, "5"));
                                    liste.add(new ExempleItem(etape6, "6"));
                                    liste.add(new ExempleItem(etape7, "7"));
                                    liste.add(new ExempleItem(etape8, "8"));
                                    liste.add(new ExempleItem(etape9, "9"));
                                    liste.add(new ExempleItem(etape10, "10"));
                            Log.d(TAG, "onSuccess: ");
                            recyclerView.setHasFixedSize(true);
                            adapter= new CardAdapteur(liste,carteDisplay.this);
                            layoutManager= new GridLayoutManager(carteDisplay.this, 2);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                                androidx.core.app.RemoteInput remoteInput= new RemoteInput.Builder("key_text_reply")
                                        .setLabel("Ajouter").build();
                                Intent Outintent = new Intent(carteDisplay.this, ReceveurMessage.class);
                                PendingIntent replypendingintent= PendingIntent.getBroadcast(carteDisplay.this,0,Outintent,0);
                                NotificationCompat.Action action =new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background, "Ajouter une tache",replypendingintent)
                                        .addRemoteInput(remoteInput).build();
                                Intent intent= new Intent (carteDisplay.this,carteDisplay.class);
                                intent.putExtra("layout",layout_name);
                                PendingIntent pendingIntent=PendingIntent.getActivity(carteDisplay.this,0,intent,0);
                                NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationChannel notificationChannel= new NotificationChannel("channel1", " Reminder",NotificationManager.IMPORTANCE_LOW);
                                NotificationCompat.Builder builder=new NotificationCompat.Builder(carteDisplay.this,"channel1");
                                notificationManager.createNotificationChannel(notificationChannel);
                                builder.setContentTitle(layout_name).setContentText("Plan d'attaque")
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(etape1+" | "+etape2+" | "+etape3+" | "+etape4+" | "+etape5+" | "+etape6))
                                        .setContentIntent(pendingIntent)
                                        .setOngoing(true)
                                        .addAction(action)
                                        .setAutoCancel(false)
                                        .setSmallIcon(R.mipmap.ic_launcher);
                                notificationManager.notify(2,builder.build());

                            }
                            else {
                                Toast.makeText(carteDisplay.this,"",Toast.LENGTH_SHORT).show();                            }
                        }
                    });
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            Map<String,Object> document =documentSnapshot.getData();
//                            String etape1 = document.get("phase1").toString();
//                            String etape2 = document.get("phase2").toString();
//                            String etape3 = document.get("phase3").toString();
//                            String etape4 = document.get("phase4").toString();
//                            String etape5 = document.get("phase5").toString();
//                            String etape6 = document.get("phase6").toString();
//                            String etape7 = document.get("phase7").toString();
//                            String etape8 = document.get("phase8").toString();
//                            String etape9 = document.get("phase9").toString();
//                            String etape10 = document.get("phase10").toString();
//                            liste.add(new ExempleItem(etape1, "1"));
//                                    liste.add(new ExempleItem(etape2, "2"));
//                                    liste.add(new ExempleItem(etape3, "3"));
//                                    liste.add(new ExempleItem(etape4, "4"));
//                                    liste.add(new ExempleItem(etape5, "5"));
//                                    liste.add(new ExempleItem(etape6, "6"));
//                                    liste.add(new ExempleItem(etape7, "7"));
//                                    liste.add(new ExempleItem(etape8, "8"));
//                                    liste.add(new ExempleItem(etape9, "9"));
//                                    liste.add(new ExempleItem(etape10, "10"));
//                            Log.d(TAG, "onSuccess: ");
//                            recyclerView.setHasFixedSize(true);
//                            adapter= new CardAdapteur(liste,carteDisplay.this);
//                            layoutManager= new GridLayoutManager(carteDisplay.this, 2);
//                            recyclerView.setLayoutManager(layoutManager);
//                            recyclerView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//                        }
//                    });
        }
    }

}
