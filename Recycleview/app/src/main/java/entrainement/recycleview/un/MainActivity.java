package entrainement.recycleview.un;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import androidx.lifecycle.Observer;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mrecycleview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button ajouter;
    private Button Vibrer;
    private Button Cancel;
    private ExempleAdapter exempleAdapter;
    private ViewModel viewModel;
    private EditText editTextUsername;
//    private EditText editTextname;
//    private EditText editTextAppreciate;
    ArrayList<ExempleItem> exempleItems= new ArrayList<>();
    private List<ExempleItem> tache;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final int REQ_CODE8SPEECH_OUTPUT= 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel= ViewModelProviders.of(this).get(ViewModel.class);
//        exempleItems.add(new ExempleItem("Audric", "larose " ,"trop bien"));
//        exempleItems.add(new ExempleItem("mickaem", "larose " ,"trop bien"));
//        exempleItems.add(new ExempleItem("Audric", "larose " ,"trop bien"));
        mrecycleview= findViewById(R.id.RecycleView);
        mrecycleview.setHasFixedSize(true);
        mAdapter = new ExempleAdapter(exempleItems);
        layoutManager= new LinearLayoutManager(this);
        mrecycleview.setLayoutManager(layoutManager);
        mrecycleview.setAdapter(mAdapter);

        ajouter= findViewById(R.id.ajouter);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDialog();
                btnOpenMic();
            }
        });

        Vibrer = findViewById(R.id.viber);
        Cancel = findViewById(R.id.cancel);
        final long[] pattern = {2000, 1000};
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Vibrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(pattern,0);
                Toast.makeText(MainActivity.this,"ca vibre !!!!",Toast.LENGTH_SHORT).show();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"fini !!",Toast.LENGTH_SHORT).show();
                vibrator.cancel();
            }
        });
        viewModel.SelectAllThoseDatas().observe(this, new Observer<List<ExempleItem>>(){

            @Override
            public void onChanged(List<ExempleItem> exempleItemss) {
                tache=  exempleItemss;
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
                viewModel.deleteData(exempleAdapter.getPositionofTask(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(mrecycleview);

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

                    viewModel.insertData(new ExempleItem(message));
                    Toast.makeText(MainActivity.this,voiceInText.get(0),Toast.LENGTH_LONG);

                }
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        mAdapter.notifyDataSetChanged();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mAdapter.notifyDataSetChanged();
        super.onPause();
    }



    public void  openDialog(){
        AlertDialog dial= alertDialog();
                dial.show();
    }
    private AlertDialog alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.addinflated, null);
        builder.setView(view)
                .setTitle("Nouveau")
                .setPositiveButton("valide !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                    }
                })
                .setNegativeButton("Annule mec", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                    }
                });
        editTextUsername= view.findViewById(R.id.nom);
//        editTextname= view.findViewById(R.id.prenom);
//        editTextAppreciate= view.findViewById(R.id.apreciation);
            return builder.create();

        }

    private void saveNote() {
        Toast.makeText(MainActivity.this,"api ouvert",Toast.LENGTH_SHORT).show();
        String nom = editTextUsername.getText().toString();        // je recupere le nom
//        String prenom = editTextUsername.getText().toString();
//        String appreciate= editTextAppreciate.getText().toString();
//        exempleItems.add(new ExempleItem(nom,prenom,appreciate));
        viewModel.insertData(new ExempleItem(nom));
//        exempleItems.add(new ExempleItem(nom));

//        Map<String, Object> note= new HashMap<>();
//        note.put("titre",nom);
//        note.put("description",prenom);
//        db.collection("notebook")
//                .document("my first note").set(note)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(MainActivity.this,"cool",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this,"not cool",Toast.LENGTH_SHORT).show();
//
//                    }
//                });

    }


}
