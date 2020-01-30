package entrainement.timer.texxtwatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adaptateur extends RecyclerView.Adapter<Adaptateur.LeHolder> {
    List<Obj_Texte> liste;

    public Adaptateur(List<Obj_Texte> liste) {
        this.liste = liste;
    }

    @NonNull
    @Override
    public LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        LeHolder leHolder= new LeHolder(view);
        return leHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeHolder holder, int position) {
        Obj_Texte texte= liste.get(position);
        holder.Htitre.setText(texte.getTitre());
        holder.Htexte.setText(texte.getTexte());

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public static class LeHolder extends RecyclerView.ViewHolder{
        private TextView Htitre;
        private TextView Htexte;

        public LeHolder(@NonNull View itemView) {
            super(itemView);
            Htexte=itemView.findViewById(R.id.texte);
            Htitre=itemView.findViewById(R.id.title);
        }
    }
}
