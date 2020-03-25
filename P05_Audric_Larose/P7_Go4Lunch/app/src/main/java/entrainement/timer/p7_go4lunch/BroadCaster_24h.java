package entrainement.timer.p7_go4lunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BroadCaster_24h extends BroadcastReceiver {
    private Me me=new Me();
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Map<String,Object> note= new HashMap<>();
            note.put("choix","");
            note.put("adresse choix","");
            note.put("id_choix","");
            note.put("note_choix","");
            Toast.makeText(context, "Choix, effacé !", Toast.LENGTH_SHORT).show();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            if (me.getMonId()!=null){
                firebaseFirestore.collection("collegue").document(me.getMonId()).update(note);

            }
   //     }

    }
}
