package entrainement.timer.recycleviewfordata;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private Button bouton_ajouter;
    private EditText nom;
    private EditText longt;
    private StorageReference storageReference;
    private ImageView photo;
    private static final int REQUESTCODEGALLERY= 101;
    private Button getPhoto;
    private Uri imageURL;
    private String lenom;
    private ProgressBar progress;
    String Storage_Path = "All_Image_Uploads/";
    public static final String Database_Path = "All_Image_Uploads_Database";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Case_Item> liste = new ArrayList<>();
//        liste.add(new Case_Item("nom"));
//        liste.add(new Case_Item("nom"));
//        liste.add(new Case_Item("nom"));
//        liste.add(new Case_Item("nom"));
//        liste.add(new Case_Item("nom"));
//        liste.add(new Case_Item("nom"));
//        liste.add(new Case_Item("nom"));
//        nom= findViewById(R.id.nom);
        bouton_ajouter= findViewById(R.id.btn_ajouter);
        progress=findViewById(R.id.progress_circular);
        bouton_ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicknshow();
            }
        });
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        adapter = new Adaptateur(liste, MainActivity.this);
        db = FirebaseFirestore.getInstance();
        db.collection("collection1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
//                        String titre = documentSnapshot.getString("titre");
                        String photo = documentSnapshot.getString("photo");
                        String longlong = documentSnapshot.getString("longtext");
                        liste.add(new Case_Item(photo,longlong));
                    }
                    adapter = new Adaptateur(liste, MainActivity.this);
                    layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        adapter.notifyDataSetChanged();
    }
    public AlertDialog AlertDial (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view= getLayoutInflater().from(this).inflate(R.layout.ajoutitem,null);
//        nom=view.findViewById(R.id.nom);
        longt=view.findViewById(R.id.longtext);
        builder.setView(view)
                .setTitle("Ajout d'un nouvel Item")
                .setPositiveButton("valider ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (longt.getText().toString() .isEmpty()) {
                            Toast.makeText(MainActivity.this, "Faut remplir les champs ", Toast.LENGTH_LONG).show();
                            clicknshow();
                        } else {
                            progress.setVisibility(View.VISIBLE);
//                            lenom = nom.getText().toString();
                            final String longtext = longt.getText().toString();
                            storageReference = FirebaseStorage.getInstance().getReference();
                            final StorageReference filepath = storageReference.child(imageURL.getLastPathSegment());
                            filepath.putFile(imageURL)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    // Get a URL to the uploaded content
                                                    String lien = uri.toString().trim();
                                                    Map<String, String> note = new HashMap<>();
                                                    note.put("titre", " ");
                                                    note.put("photo", lien);
                                                    note.put("longtext", longtext);
                                                    db.collection("collection1").document().set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progress.setVisibility(View.GONE);
                                                            Toast.makeText(MainActivity.this, "yoouuhouu!", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            // ...
                                            Toast.makeText(MainActivity.this, "Pas de connexion Internet", Toast.LENGTH_LONG).show();
                                        }
                                    });
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        getPhoto=view.findViewById(R.id.bouton);
        getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTCODEGALLERY);
            }
        });
        photo=view.findViewById(R.id.photo_test);
        return builder.create();
    }

    public void clicknshow (){
        AlertDialog alertDial = AlertDial();
        alertDial.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==REQUESTCODEGALLERY)&&(resultCode==RESULT_OK)) {
            imageURL=data.getData();
            photo.setImageURI(imageURL);
        }
    }

}
