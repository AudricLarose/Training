package entrainement.timer.p7_go4lunch.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.model.Me;

public class BroadCaster_24h extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Map<String,Object> note= new HashMap<>();
            note.put("choix","");
            note.put("adresse choix","");
            note.put("id_choix","");
            note.put("note_choix","");
            Toast.makeText(context, R.string.choice, Toast.LENGTH_SHORT).show();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            if (Me.getMyId()!=null){
                firebaseFirestore.collection("collegue").document(Me.getMyId()).update(note);

            }
        }

    }
}
