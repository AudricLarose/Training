package entrainement.recycleview.un;

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

public class ExempleAdapter extends RecyclerView.Adapter<ExempleAdapter.leHolder> {
    private List<ExempleItem> liste;
    private ExempleItem item;

//    public ExempleAdapter(List<ExempleItem> liste, WeakReference<MainActivity> activity) {
//        this.liste = liste;
//        this.activity = activity;
//    }
//
public ExempleAdapter(List<ExempleItem> liste) {
       this.liste = liste;
}

    public void ConsListe(List<ExempleItem> liste) {
        this.liste = liste;
    }
//    private WeakReference<MainActivity> activity;
//    private moninterface listener;


    @NonNull
    @Override
    public leHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent , false);
        leHolder holder = new leHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final leHolder holder, int position) {
       final ExempleItem ligne = liste.get(position);
        holder.nom.setText(ligne.getNom());
//        holder.nom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeColor(ligne);
//                notifyDataSetChanged();
//            }
//        });

        switch (ligne.getColor()) {
            case 0:
                break;
            case 1:
                holder.nom.setBackgroundColor(Color.parseColor("#cc33ff"));
                break;
            case 2:
                holder.nom.setBackgroundColor(Color.parseColor("#993366"));
                break;
        }
        if (ligne.getColor()==3){
            ligne.setColor(0);
        }
    }
//    public interface moninterface{
//        public void autremethode ();
//    }
    private void changeColor(ExempleItem ligne){
       // this.activity.get().
//        this.listener.autremethode();
        int plus = ligne.getColor();
        ligne.setColor(plus+1);
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

        public leHolder(@NonNull View itemView) {
            super(itemView);
//            colors=new int[]{Color.GREEN,Color.BLUE,Color.BLACK,Color.DKGRAY,Color.MAGENTA,Color.YELLOW,
//                    };
//            Random random = new Random();
//            int colorArray = colors.length;
//            int randomnum= random.nextInt(colorArray);
            nom= itemView.findViewById(R.id.TextView1);
            cardView= itemView.findViewById(R.id.cardview);
//            prenom= itemView.findViewById(R.id.TextView2);
//            appreciation= itemView.findViewById(R.id.TextView3);
        }
    }


}
