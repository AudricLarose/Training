package entrainement.timer.p7_go4lunch.utils.collegue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Bases.ActivityDetails;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Place;
import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;

public class Adaptateur extends RecyclerView.Adapter<Adaptateur.LeHolder>  {
    List<Collegue> liste ;


    public Adaptateur(List<Collegue> liste) {
        this.liste = liste;
        notifyDataSetChanged();
    }


    public void updateList(List<Collegue> listSearch){
        liste=new ArrayList<>();
        liste.addAll(listSearch);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        LeHolder holder = new LeHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull LeHolder holder, int position) {
          final Collegue collegue = liste.get(position);

        holder.nom.setText(collegue.getNom());
        if ((collegue.getChoix() == null) || (collegue.getChoix().trim().isEmpty())) {
            holder.nom.setTextColor(Color.parseColor("#979797"));
            holder.choice.setVisibility(View.GONE);
            holder.choixnot.setVisibility(View.VISIBLE);
        } else {
            holder.choice.setVisibility(View.VISIBLE);
            holder.choixnot.setVisibility(View.GONE);
            holder.choix.setText(collegue.getChoix());
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ActivityDetails.class);
                    Bundle extra = new Bundle();
                    Other.theGoodPlace(collegue.getId_monchoix(), new Other.ThegoodPlace() {
                        @Override
                        public void GoodPlace(Results place) {
                            extra.putSerializable("Place",place);
                        }
                    });
                    intent.putExtras(extra);
                    v.getContext().startActivity(intent);
                }
            });
        }
        String photoUri = collegue.getPhoto();
        Picasso.get().load(photoUri).into(holder.photo);

    }

    @Override
    public int getItemCount() {
        if (liste!=null) {
            return liste.size();
        } else {
            return 0;
        }
    }

    public static class LeHolder extends RecyclerView.ViewHolder {
        private TextView nom;
        private TextView choix;
        private TextView choice;
        private CircularImageView photo;
        private TextView choixnot;
        private ConstraintLayout relativeLayout;

        public LeHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.name);
            choix = itemView.findViewById(R.id.choice);
            choice = itemView.findViewById(R.id.introduce);
            choixnot = itemView.findViewById(R.id.introducenot);
            photo = itemView.findViewById(R.id.photo);
            relativeLayout = itemView.findViewById(R.id.relatident);
        }
    }


}
