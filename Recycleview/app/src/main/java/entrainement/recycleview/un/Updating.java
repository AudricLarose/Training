package entrainement.recycleview.un;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Update;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Updating extends AppCompatActivity {
    private String etape;
    private String layout;
    private EditText entrer;
    private Button bouton;
    private ExempleItem objet;
    private List<ExempleItem> items= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating);
        bouton=findViewById(R.id.btn_validation);
        entrer=findViewById(R.id.et_texteEncadre);
        Bundle extra = getIntent().getExtras();

        if ( extra!=null){
             etape= extra.getString("etape");
             layout= extra.getString("layout");
             bouton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (entrer.getText().toString().isEmpty()){
                         Snackbar.make(v,"Entrer quelque chose", Snackbar.LENGTH_SHORT).setAction("action",null).show();
                     }
                     String nom_champs = entrer.getText().toString();
                     String reference= "phase"+etape;
                     Map<String, Object> note = new HashMap<>();
                     note.put(reference,nom_champs);
                     FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                     firebaseFirestore.collection("important").document(layout)
                             .update(note);
                     Intent intent = new Intent(Updating.this,carteDisplay.class);
                     intent.putExtra("layout",layout);
                     startActivity(intent);
                     finish();
                 }
             });

        }
    }
}
