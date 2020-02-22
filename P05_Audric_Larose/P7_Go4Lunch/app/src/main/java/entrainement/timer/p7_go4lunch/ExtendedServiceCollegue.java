package entrainement.timer.p7_go4lunch;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedServiceCollegue implements InterfaceCollegue {
    private List<Collegue> listLiveData=new ArrayList<>();
    private static final String TAG = "ExtendedServiceCollegue";
    private RecyclerView.Adapter recyclerView;
    @Override
    public List<Collegue> getListCollegue() {
        recyclerView = new Adaptateur(listLiveData);
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                String collegueData = documentSnapshot.getString("Nom");
                                String colleguephoto= documentSnapshot.getString("photo");
                                String collegueChoix= documentSnapshot.getString("choix");
                                listLiveData.add(new Collegue(collegueData,collegueChoix,colleguephoto));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listLiveData.add(new Collegue("non","gnegne"));
                        Log.d(TAG, "onFailure: ");     
                    }
                });
        return listLiveData;
    }

    @Override
    public void addCollegue(Context context,String id , String collegue,String photo){
        Map<String, String> note= new HashMap<>();
        note.put("Nom", collegue);
        note.put("id",id);
        note.put("choix","Le OkMaloukoolKat");
        note.put("photo",photo);
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("collegue").document(id).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Vous avez bien été ajouté a la base de donnée", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
