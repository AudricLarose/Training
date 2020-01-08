package entrainement.recycleview.un;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import entrainement.fragment.premier.Model;
import entrainement.fragment.premier.R;

public class Adaptateur extends RecyclerView.Adapter<Adaptateur.ExempleViewHolder> {
    private ArrayList<Model> Laliste = new ArrayList<>();
    public Adaptateur(ArrayList<Model> laliste) {
        Laliste = laliste;
    }


    public static class ExempleViewHolder extends RecyclerView.ViewHolder{
        public TextView text1;
        public TextView text2;
        public TextView text3;

        public ExempleViewHolder(@NonNull View itemView) {
            super(itemView);
            text1=itemView.findViewById(R.id.TextView1);
            text2=itemView.findViewById(R.id.TextView2);
            text3=itemView.findViewById(R.id.TextView3);
        }
    }

    @NonNull
    @Override
    public ExempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carte, parent, false);
        ExempleViewHolder exempleViewHolder = new ExempleViewHolder(view);
        return exempleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExempleViewHolder holder, int position) {
        Model Adaptateur= Laliste.get(position);
        holder.text1.setText(Adaptateur.getNom());
        holder.text2.setText(Adaptateur.getPrenom());
        holder.text3.setText(Adaptateur.getAppreciation());
    }

    @Override
    public int getItemCount() {
        return Laliste.size();
    }


}
