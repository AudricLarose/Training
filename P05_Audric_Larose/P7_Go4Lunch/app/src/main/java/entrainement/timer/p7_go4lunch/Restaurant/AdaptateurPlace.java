package entrainement.timer.p7_go4lunch.Restaurant;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Activities.ActivityDetails;
import entrainement.timer.p7_go4lunch.Collegue.Adaptateur;
import entrainement.timer.p7_go4lunch.Collegue.Collegue;
import entrainement.timer.p7_go4lunch.Collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.R;

public class AdaptateurPlace extends RecyclerView.Adapter <AdaptateurPlace.LeHolder> {
//    LiveData<List<Place>> placeList;
    private ExtendedServicePlace servicePlace;
    List<Place> listedeplaces;

    public AdaptateurPlace(List<Place> listedeplaces) {
        this.listedeplaces = listedeplaces;
        notifyDataSetChanged();
    }

    void updatelistplace(List<Place> liste){
        listedeplaces=new ArrayList<>();
        listedeplaces.addAll(liste);
    }

    //    public AdaptateurPlace(LiveData<List<Place>> placeList, LifecycleOwner owner) {
//
//        this.placeList = placeList;
//        this.placeList.observe(owner, new Observer<List<Place>>() {
//            @Override
//            public void onChanged(List<Place> places) {
//                AdaptateurPlace.this.notifyDataSetChanged();
//            }
//        });
//    }

    @NonNull
    @Override
    public LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowresto,parent,false);
        LeHolder leHolder= new LeHolder(view);
    return leHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeHolder holder, int position) {
        servicePlace= DI.getServicePlace();
        Place place= listedeplaces.get(position);
        holder.nom.setText(place.getnomPlace());
        holder.adresse.setText(place.getAdresse());
        holder.perso.setText(place.getquivient());
//        holder.etoile.setText(place.getnote());
        holder.distance.setText(place.getDistance());
        holder.horaire.setText(place.getHoraire());
        if (place.getnote()!=null) {
            holder.bar.setRating(Float.parseFloat(place.getnote()));
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityDetails.class);
                intent.putExtra("id",place.getId());
                intent.putExtra("nom",place.getnomPlace());
                intent.putExtra("adresse",place.getAdresse());
                intent.putExtra("phone",place.getTel());
                intent.putExtra("site",place.getSite());
                intent.putExtra("etoile",place.getnote());
                v.getContext().startActivity(intent);

            }
        });
//        holder.photo

    }

    @Override
    public int getItemCount() {
        if (listedeplaces!=null) {
            return listedeplaces.size();
        } else {
            return 0;
        }
    }

    public static class LeHolder extends RecyclerView.ViewHolder{
        private RelativeLayout relativeLayout;
    private TextView nom;
    private TextView adresse;
    private TextView perso;
    private TextView distance;
    private TextView horaire;
    private TextView etoile;
    private ImageView photo;
    private RatingBar bar;

        public LeHolder(@NonNull View itemView) {
            super(itemView);
            nom=itemView.findViewById(R.id.nomresto);
            adresse=itemView.findViewById(R.id.adresse);
            perso=itemView.findViewById(R.id.perso);
            distance=itemView.findViewById(R.id.distance);
            horaire=itemView.findViewById(R.id.horaire);
            etoile=itemView.findViewById(R.id.etoile);
            photo=itemView.findViewById(R.id.photoresto);
            relativeLayout=itemView.findViewById(R.id.relativeRowPlace);
            bar=itemView.findViewById(R.id.ratingbar);
        }
    }
}
