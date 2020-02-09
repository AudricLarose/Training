package entrainement.recycleview.un;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.eventbus.EventBus;
import com.google.firebase.database.collection.LLRBNode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static androidx.core.content.ContextCompat.startActivity;

public class ExempleAdapter extends RecyclerView.Adapter<ExempleAdapter.leHolder> {
    private List<ExempleItem> liste;
    private ExempleItem item;
    private Context context;
    public static boolean intentverify=false;

//    public ExempleAdapter(List<ExempleItem> liste, WeakReference<MainActivity> activity) {
//        this.liste = liste;
//        this.activity = activity;
//    }
//
public ExempleAdapter(List<ExempleItem> liste) {
       this.liste = liste;
}

public void verify(boolean intentverify, Context context) {this.intentverify = intentverify; this.context=context;}


    public void ConsListe(List<ExempleItem> liste) {
        this.liste = liste;
    }
//    private WeakReference<MainActivity> activity;
//    private moninterface listener;


    @NonNull
    @Override
    public leHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
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
                if (intentverify) {
                    Intent intent;
                    intent = new Intent(v.getContext(), carteDisplay.class);
                    intent.putExtra("layout", ligne.getNom());
                    v.getContext().startActivity(intent);
                }
            }
        });
    }
    private void changeColor(ExempleItem ligne){
        notifyDataSetChanged();
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
        private View cardView;
        private int[] colors;

        public leHolder(@NonNull final View itemView) {
            super(itemView);
            nom= itemView.findViewById(R.id.TextView1);
//            colors=new int[]{Color.GREEN,Color.BLUE,Color.BLACK,Color.DKGRAY,Color.MAGENTA,Color.YELLOW,
//                    };
//            Random random = new Random();
//            int colorArray = colors.length;
//            int randomnum= random.nextInt(colorArray);
            cardView= itemView.findViewById(R.id.cardview);
//            prenom= itemView.findViewById(R.id.TextView2);
//            appreciation= itemView.findViewById(R.id.TextView3);
        }
    }


}
