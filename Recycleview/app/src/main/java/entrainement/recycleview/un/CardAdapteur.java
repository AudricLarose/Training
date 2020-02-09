package entrainement.recycleview.un;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Update;

import com.google.common.eventbus.EventBus;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.getSystemServiceName;
import static androidx.core.content.ContextCompat.startActivity;

public class CardAdapteur extends RecyclerView.Adapter<CardAdapteur.leHolder> {
    private List<ExempleItem> liste=new ArrayList<>();
    private ExempleItem item;
    private static String layout_name;
    private Context context;
    private EditText editTextUsername;
    public static boolean intentverify=false;
    public  Activity activity;

    public CardAdapteur(List<ExempleItem> liste,Activity activity) {
        this.liste = liste;
        this.activity=activity;


    }

    public CardAdapteur(String layout_name) {
        this.layout_name = layout_name;
    }

    @NonNull
    @Override
    public leHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_carte_display_card, parent , false);
        leHolder holder = new leHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final leHolder holder, int position) {
        final ExempleItem ligne = liste.get(position);
        holder.nom.setText(ligne.getNom());
        holder.nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Updating.class);
                intent.putExtra("etape",ligne.getEtape());
                intent.putExtra("layout",layout_name);
                v.getContext().startActivity(intent);
                activity.finish();
//                clickme(ligne);
            }
        });
        holder.etape.setText(ligne.getEtape());
        String nom_champs = holder.nom.getText().toString();
        if (!nom_champs.equals(" ")){
            holder.relativeLayout.setBackgroundResource(R.color.bleuTurquoise);
            holder.boutnvisible.setVisibility(View.GONE);
            holder.etape.setTextColor(Color.WHITE);
            holder.none_etape.setTextColor(Color.WHITE);
            holder.viewview.setBackgroundColor(Color.WHITE);
        }
    }

    private void clickme(ExempleItem ligne) {
        AlertDialog alertDialog = dialog(ligne);
        alertDialog.show();
    }

    private AlertDialog dialog (final ExempleItem ligne){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.addinflated,null);
        editTextUsername= view.findViewById(R.id.nom);
        builder.setView(view)
                .setTitle("Choisissez une tache")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nom_champs = editTextUsername.getText().toString();
                        String reference= "phase"+ligne.getEtape();
                        Map<String, Object> note = new HashMap<>();
                        note.put(reference,nom_champs);
                        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("important").document(layout_name)
                                .update(note);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public ExempleItem getPositionofTask(int position){
        return liste.get(position);
    }
    @Override
    public int getItemCount() {
        return liste.size();
    }

    public static class leHolder extends RecyclerView.ViewHolder{
        private TextView nom ;
        private TextView etape;
        private TextView none_etape;
        private RelativeLayout relativeLayout;
        private TextView boutnvisible;
        private View viewview;

        public leHolder(@NonNull final View itemView) {
            super(itemView);
            etape= itemView.findViewById(R.id.etape);
            none_etape= itemView.findViewById(R.id.none_etape);
            nom= itemView.findViewById(R.id.process);
            viewview= itemView.findViewById(R.id.viewview);
            boutnvisible= itemView.findViewById(R.id.boutonvisible);
            relativeLayout= itemView.findViewById(R.id.RelativeCase);

                 nom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intentverify){
                        Intent intent = new Intent(itemView.getContext(), carteDisplay.class);
                        v.getContext().startActivity(intent);}
                }
            });
        }
    }


}
