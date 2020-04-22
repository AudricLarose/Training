package entrainement.timer.listeentraniement;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adaptateur extends RecyclerView.Adapter<adaptateur.leHolder> {
    List<item> liste;

    public adaptateur(List<item> liste) {
        this.liste = liste;
    }

    @NonNull
    @Override
    public leHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main, parent, false);
        leHolder holder= new leHolder(view);
        return holder;
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull leHolder holder, int position) {
        item autre= liste.get(position);
        holder.nom.setText(autre.getNom());
    }


    @Override
    public int getItemCount() {
        return liste.size();
    }

    public static class leHolder extends RecyclerView.ViewHolder{
        private TextView nom;

        public leHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.TextView1);
        }
    }
}
