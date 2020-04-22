package entrainement.recycleview.un;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;


import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import androidx.lifecycle.Observer;
import androidx.room.util.ViewInfo;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mrecycleview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button ajouter;
    private Button Vibrer;
    private Button Cancel;
    private Button importantbouton;
    private ExempleAdapter exempleAdapter;
    private ViewModel viewModel;
    private EditText editTextUsername;
    private ImportantFragment importantFragment;
//    private EditText editTextname;
//    private EditText editTextAppreciate;
    ArrayList<ExempleItem> exempleItems= new ArrayList<>();
    private List<ExempleItem> tache;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final int REQ_CODE8SPEECH_OUTPUT= 143;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel= ViewModelProviders.of(this).get(ViewModel.class);

        mrecycleview= findViewById(R.id.RecycleView);
        mrecycleview.setHasFixedSize(true);
//        mAdapter = new ExempleAdapter(exempleItems);
//        mAdapter.notifyDataSetChanged();
        layoutManager= new LinearLayoutManager(this);
        mrecycleview.setLayoutManager(layoutManager);
//        mrecycleview.setAdapter(mAdapter);
        ajouter= findViewById(R.id.ajouter);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   openDialog();
//               btnOpenMic();
            }
        });
        importantbouton=findViewById(R.id.ajoutplan);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.blue:
                        selectedFragment = new HomeFragment();
                        importantbouton.setVisibility(View.GONE);
                        exempleAdapter.verify(false, MainActivity.this);
                        break;
                    case R.id.orange:
                        selectedFragment = new ImportantFragment();
                        exempleAdapter.verify(true,MainActivity.this);
                        importantbouton.setVisibility(View.VISIBLE);
                        importantbouton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                importantFragment = new ImportantFragment();
                                ajouterplan();
                            }
                        });
                        break;

                    case R.id.violet:
                        selectedFragment = new quickFragment();
                        exempleAdapter.verify(false,MainActivity.this);
                        importantbouton.setVisibility(View.GONE);

                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;

            }
        });
        viewModel.SelectAllThoseDatas().observe(this, new Observer<List<ExempleItem>>(){

            @Override
            public void onChanged(List<ExempleItem> exempleItemss) {
                tache=  exempleItemss;
//                exempleAdapter = new ExempleAdapter(exempleItemss,new WeakReference<MainActivity>(MainActivity.this));
             exempleAdapter = new ExempleAdapter(exempleItemss);
                mrecycleview.setAdapter(exempleAdapter);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                areYousure(viewHolder);
            }
        }).attachToRecyclerView(mrecycleview);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notebook")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         List<String> listeArray=new ArrayList<>();
                        if (task.isSuccessful()) {
                            viewModel.deleteAllData();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String text = document.getString("titre");
                                listeArray.add(text);
//                                exempleItems.add(new ExempleItem(text,0));
//                                exempleAdapter = new ExempleAdapter(exempleItems);
                                viewModel.insertData(new ExempleItem(text,"0"));
//                                mrecycleview.setAdapter(exempleAdapter);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                androidx.core.app.RemoteInput remoteInput= new RemoteInput.Builder("key_text_reply")
                                        .setLabel("Ajouter").build();
                                Intent Outintent = new Intent(MainActivity.this, ReceveurMessage.class);
                                PendingIntent replypendingintent= PendingIntent.getBroadcast(MainActivity.this,0,Outintent,0);
                                NotificationCompat.Action action =new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background, "Ajouter une tache",replypendingintent)
                                        .addRemoteInput(remoteInput).build();
                                Intent intent= new Intent (MainActivity.this,MainActivity.class);
                                PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,intent,0);
                                NotificationManager notificationManager= (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);
                                NotificationChannel notificationChannel= new NotificationChannel("channel1", " Reminder",NotificationManager.IMPORTANCE_LOW);
                                NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this,"channel1");
                                notificationManager.createNotificationChannel(notificationChannel);
                                builder.setContentTitle("To-Do")
                                        .setContentText("Acceder a la liste")
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(listederoule(0,listeArray)+ " | "+ listederoule(1,listeArray)+ " | "+listederoule(2,listeArray)+ " | "+listederoule(3,listeArray)+ " | "+ listederoule(4,listeArray)+ " | "+listederoule(5,listeArray)))
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(false)
                                        .setOngoing(true)
                                        .addAction(action)
                                        .setSmallIcon(R.mipmap.ic_launcher);
                                notificationManager.notify(1,builder.build());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    public  String listederoule (int x,List<String> liste){
        if (x<=liste.size()-1){
            return   liste.get(x);
        } else {
            return " ";
        }
    }

    private void areYousure(RecyclerView.ViewHolder viewHolder) {
        AlertDialog alertDialog= surtcheck(viewHolder);
        alertDialog.show();

    }
    private AlertDialog surtcheck(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view= getLayoutInflater().from(this).inflate(R.layout.sure, null);
        builder.setView(view)
                .setTitle("Effacer ?")
                .setPositiveButton("Effacer !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExempleItem task = exempleAdapter.getPositionofTask(viewHolder.getAdapterPosition());
                        viewModel.deleteData(task);
                        deleteDataNote(task.getNom().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }

    public void ajouterplan() {
        android.app.AlertDialog alertDialog= dialogue();
        alertDialog.show();

    }

    private android.app.AlertDialog dialogue(){
        android.app.AlertDialog.Builder alertDialog= new android.app.AlertDialog.Builder(MainActivity.this);
        View view2= getLayoutInflater().from(MainActivity.this).inflate(R.layout.addinflated, null);
        editTextUsername= view2.findViewById(R.id.nom);
        alertDialog.setView(view2)
                .setTitle("Ajouter plan d'action")
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextUsername.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this,"Veuiller ajouter un nom",Toast.LENGTH_LONG).show();
                            ajouterplan();
                        } else {
                        String nom = editTextUsername.getText().toString();
                        Map<String, String> note= new HashMap<>();
                        note.put("titre",nom);
                        note.put("phase1", " ");
                        note.put("phase2", " ");
                        note.put("phase3", " ");
                        note.put("phase4", " ");
                        note.put("phase5", " ");
                        note.put("phase6", " ");
                        note.put("phase7", " ");
                        note.put("phase8", " ");
                        note.put("phase9", " ");
                        note.put("phase10", " ");

                        db.collection("important").document(nom)
                                .set(note)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        Toast.makeText(MainActivity.this,"Envoyé !",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this,"Pas de Reseau",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return alertDialog.create();
    }

    private void btnOpenMic(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"A toi ! ");

        try {
            startActivityForResult(intent,REQ_CODE8SPEECH_OUTPUT);
        }
        catch (ActivityNotFoundException tim){
            Toast.makeText(this,"Le micro a un probleme",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE8SPEECH_OUTPUT: {
                if (resultCode == RESULT_OK ){
                    ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String message =voiceInText.get(0);
                    message= message.substring(0,1).toUpperCase()+message.substring(1).toLowerCase();
                    viewModel.insertData(new ExempleItem(message,"1"));
                    dataGo(message);
                    Toast.makeText(MainActivity.this,voiceInText.get(0),Toast.LENGTH_LONG);

                }
                break;
            }
        }
    }
    @Override
    protected void onResume() {
//        exempleAdapter.notifyDataSetChanged();
        super.onResume();
    }
    public void  openDialog(){
        AlertDialog dial= alertDialog();
                dial.show();
    }
    private AlertDialog alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view= getLayoutInflater().from(this).inflate(R.layout.addinflated, null);

        builder.setView(view)
                .setTitle("Nouveau")
                .setPositiveButton("valide !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                        exempleAdapter.notifyDataSetChanged();
                    }
                });
        editTextUsername= view.findViewById(R.id.nom);
            return builder.create();

        }

    private void saveNote() {
        String nom = editTextUsername.getText().toString();        // je recupere le nom
        viewModel.insertData(new ExempleItem(nom, "0"));
        exempleAdapter.notifyDataSetChanged();
        dataGo(nom);
    }

    private void dataGo(String nom ) {
        Map<String, String> note= new HashMap<>();
        note.put("titre",nom);
        db.collection("notebook").document(nom)
                .set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {

                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"not cool",Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void deleteDataNote(String task){
        db.collection("notebook").document(task.toString()).delete();

    }


}
