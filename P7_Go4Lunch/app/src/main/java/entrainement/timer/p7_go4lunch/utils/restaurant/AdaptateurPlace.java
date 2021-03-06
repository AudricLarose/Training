package entrainement.timer.p7_go4lunch.utils.restaurant;

import android.content.Intent;
import android.graphics.Color;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Bases.ActivityDetails;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;

public class AdaptateurPlace extends RecyclerView.Adapter<AdaptateurPlace.LeHolder> {
    List<Results> listedeplaces;

    public AdaptateurPlace(List<Results> listedeplaces) {
        this.listedeplaces = listedeplaces;
        notifyDataSetChanged();
    }

    void updatelistplace(List<Results> liste) {
        listedeplaces = new ArrayList<>();
        listedeplaces.addAll(liste);
    }

    @NonNull
    @Override
    public LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowresto, parent, false);
        return new LeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeHolder holder, int position) {
        Results place = listedeplaces.get(position);
        holder.nom.setText(place.getName());
        holder.adresse.setText(place.getVicinity());
        String number= "("+place.getWhocome()+")";
        holder.perso.setText(number);

        if (place.getGeometry()!=null && place.getGeometry().getLocation()!=null) {
            place.getGeometry().getLocation().setDistance(Other.getDistance(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng()));
            String meter = String.valueOf(place.getGeometry().getLocation().getDistance()) + "m";

            holder.distance.setText(meter);
        }
        if (place!= null) {
            if (place.getOpeningHours()!= null && place.getOpeningHours().getOpenNow()!=null) {
                if (place.getOpeningHours().getOpenNow() == true) {
                    holder.horaire.setText(R.string.open);
                    holder.horaire.setTextColor(Color.GREEN);
                } else {
                    holder.horaire.setText(R.string.fermé);
                    holder.horaire.setTextColor(Color.RED);
                }
            } else {
                holder.horaire.setText(R.string.unaivailable );
                holder.horaire.setTextColor(Color.BLUE);
            }
            String html = Other.getUrlimage(place.getPhotos().get(0).getPhotoReference(), String.valueOf(place.getPhotos().get(0).getWidth()));
            Picasso.get().load(html).into(holder.photo);
            if (place.getId() != null) {
                holder.bar.setRating(Float.parseFloat(String.valueOf(place.getRating()-1)));
            }
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Place", place);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listedeplaces != null) {
            return listedeplaces.size();
        } else {
            return 0;
        }
    }

    public static class LeHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayout;
        private TextView nom;
        private TextView adresse;
        private TextView perso;
        private TextView distance;
        private TextView horaire;
        private ImageView photo;
        private RatingBar bar;

        public LeHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nomresto);
            adresse = itemView.findViewById(R.id.adresse);
            perso = itemView.findViewById(R.id.perso);
            distance = itemView.findViewById(R.id.distance);
            horaire = itemView.findViewById(R.id.horaire);
            photo = itemView.findViewById(R.id.photoresto);
            relativeLayout = itemView.findViewById(R.id.relativeRowPlace);
            bar = itemView.findViewById(R.id.ratingbar);
        }
    }
}
