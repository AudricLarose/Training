package entrainement.timer.p7_go4lunch.utils.restaurant;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Bases.ActivityDetails;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Place;

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
        float[] result = new float[1];
        Place place= listedeplaces.get(position);
        holder.nom.setText(place.getnomPlace());
        holder.adresse.setText(place.getAdresse());
        holder.perso.setText(place.getquivient());
        Double latitude = Double.parseDouble(place.getLatitude());
        Double longitude = Double.parseDouble(place.getLongitude());
        Location.distanceBetween(Me.getMy_latitude(), Me.getMy_longitude(), latitude, longitude, result);
        int round = Math.round(result[0]);
        String distance = String.valueOf(round);
        holder.distance.setText(distance);
        holder.horaire.setText(place.getHoraire());
        if (place.getnote()!=null) {
            holder.bar.setRating(Float.parseFloat(place.getnote()));
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityDetails.class);
                Bundle bundle= new Bundle();
                bundle.putString("id",place.getId());
                bundle.putString("nom",place.getnomPlace());
                bundle.putString("adresse",place.getAdresse());
                bundle.putString("phone",place.getPhone());
                bundle.putString("site",place.getSite());
                bundle.putString("etoile",place.getnote());
                bundle.putString("photo",place.getPhoto());
                bundle.putSerializable("Place",place);
                intent.putExtras(bundle);
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
