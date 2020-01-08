package entrainement.recycleview.un;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExempleAdapter extends RecyclerView.Adapter<ExempleAdapter.leHolder> {
    private List<ExempleItem> liste;
    public ExempleAdapter(List<ExempleItem> liste) {
        this.liste = liste;
    }
    public void ConsListe(List<ExempleItem> liste) {
        this.liste = liste;
    }


    @NonNull
    @Override
    public leHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent , false);
        leHolder holder = new leHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull leHolder holder, int position) {
        ExempleItem item= liste.get(position);
            holder.nom.setText(item.getNom());
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
        private TextView prenom;
        private TextView appreciation;
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
            cardView.setBackgroundColor(Color.BLACK);
//            prenom= itemView.findViewById(R.id.TextView2);
//            appreciation= itemView.findViewById(R.id.TextView3);
        }
    }


}
