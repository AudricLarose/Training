package entrainement.timer.p7_go4lunch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdaptateurPlace extends RecyclerView.Adapter <AdaptateurPlace.LeHolder> {
    List<Place> placeList = new ArrayList<>();

    public AdaptateurPlace(List<Place> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowresto,parent,false);
        LeHolder leHolder= new LeHolder(view);
    return leHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeHolder holder, int position) {
        Place place= placeList.get(position);
        holder.nom.setText(place.getName());
        holder.adresse.setText(place.getAdresse());
        holder.perso.setText(place.getPerso());
        holder.distance.setText(place.getDistance());
        holder.horaire.setText(place.getHoraire());
//        holder.etoile.setText((int) place.getEtoile());
//        holder.photo

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class LeHolder extends RecyclerView.ViewHolder{
    private TextView nom;
    private TextView adresse;
    private TextView perso;
    private TextView distance;
    private TextView horaire;
    private TextView etoile;
    private ImageView photo;

        public LeHolder(@NonNull View itemView) {
            super(itemView);
            nom=itemView.findViewById(R.id.nomresto);
            adresse=itemView.findViewById(R.id.adresse);
            perso=itemView.findViewById(R.id.perso);
            distance=itemView.findViewById(R.id.distance);
            horaire=itemView.findViewById(R.id.horaire);
            etoile=itemView.findViewById(R.id.etoile);
            photo=itemView.findViewById(R.id.photoresto);
        }
    }
}
