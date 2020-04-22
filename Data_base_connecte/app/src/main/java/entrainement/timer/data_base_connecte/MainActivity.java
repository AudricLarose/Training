package entrainement.timer.data_base_connecte;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button btn_ajout;
    private Button valider;
    private EditText titre;
    private EditText longtext;
    private ImageView image;
    private ImageView image2;
    private Uri imageURL;
    private StorageReference storageReference;
    private static final int  REQUESTCODEGALLERY=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_ajout = findViewById(R.id.Imageajout);
        valider = findViewById(R.id.valider);
        titre = findViewById(R.id.titre);
        longtext = findViewById(R.id.longtext);
        image = findViewById(R.id.imagetest);
        image2 = findViewById(R.id.imagetest2);

        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUESTCODEGALLERY);
            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String titleval=titre.getText().toString();
//                final String textlong=longtext.getText().toString();
                storageReference= FirebaseStorage.getInstance().getReference();
                StorageReference filepath =storageReference.child("poste").child(imageURL.getLastPathSegment());
//                filepath.putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
//                        //DatabaseReference newPost= mPostDataBase.push();
//                        //Map<String, String> dataSave = new HashMap<>();
//                        //dataSave.put("titre",titleval);
//                        //dataSave.put("text",textlong);
//                        //newPost.setValue(dataSave);
//                    }
//                });

                File localFile = null;
                try {
                    localFile = File.createTempFile("images", "jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filepath.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Successfully downloaded data to local file
//                                Glide.with(getApplicationContext()).load(url).into(imageView);
                                Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        // ...
                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==REQUESTCODEGALLERY)&&(resultCode==RESULT_OK)) {
            imageURL=data.getData();
            image.setImageURI(imageURL);
        }
    }
}
