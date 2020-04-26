package entrainement.timer.p7_go4lunch;

import android.annotation.SuppressLint;
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
import entrainement.timer.p7_go4lunch.model.Place;

public class AdaptateurSuggestion extends RecyclerView.Adapter<AdaptateurSuggestion.LeHolder> {
    List<Place> listedeplaces;

    public AdaptateurSuggestion(List<Place> listedeplaces) {
        this.listedeplaces = listedeplaces;
        notifyDataSetChanged();
    }

    void updatelistplace(List<Place> liste) {
        listedeplaces = new ArrayList<>();
        listedeplaces.addAll(liste);
    }

    @NonNull
    @Override
    public AdaptateurSuggestion.LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowresto, parent, false);
        return new AdaptateurSuggestion.LeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptateurSuggestion.LeHolder holder, int position) {
        Place place = listedeplaces.get(position);
        holder.nom.setText(place.getnomPlace());
        holder.adresse.setText(place.getAdresse());
//        place.get().getLocation().setDistance(Other.getDistance(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng()));
//        holder.distance.setText(String.valueOf(place.getGeometry().getLocation().getDistance()));
//        if (place!= null) {
//            if (place.getOpeningHours() != null) {
//                if (place.getOpeningHours().getOpenNow() == true) {
//                    holder.horaire.setText(R.string.open);
//                    holder.horaire.setTextColor(Color.GREEN);
//                } else {
//                    holder.horaire.setText(R.string.fermé);
//                    holder.horaire.setTextColor(Color.RED);
//                }
//            }
//            String html = Other.getUrlimage(place.getPhotos().get(0).getPhotoReference(), String.valueOf(place.getPhotos().get(0).getWidth()));
//            Picasso.get().load(html).into(holder.photo);
//            if (place.getId() != null) {
//                holder.bar.setRating(Float.parseFloat(String.valueOf(place.getRating() / 5)));
//            }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
