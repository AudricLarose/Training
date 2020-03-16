package entrainement.timer.p7_go4lunch.Collegue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Activities.ActivityDetails;
import entrainement.timer.p7_go4lunch.R;

import static entrainement.timer.p7_go4lunch.R.color.quantum_grey300;

public class Adaptateur extends RecyclerView.Adapter<Adaptateur.LeHolder>  {
    LiveData<List<Collegue>> list;
    List<Collegue> listNew;
    Context context;
    URL urlWelcome = null;

    public Adaptateur(List<Collegue> listNew) {
        this.listNew = listNew;
    }

    public Adaptateur(LiveData<List<Collegue>> list, LifecycleOwner owner) {
        this.list = list;
        this.list.observe(owner, new Observer<List<Collegue>>() {
            @Override
            public void onChanged(List<Collegue> collegues) {
                Adaptateur.this.notifyDataSetChanged();
            }
        });
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
        final Collegue collegue = list.getValue().get(position);
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
                    intent.putExtra("nom", collegue.getChoix());
                    intent.putExtra("adresse", " ");
                    v.getContext().startActivity(intent);
                }
            });
        }
        String photoUri = collegue.getPhoto();
        Picasso.get().load(photoUri).into(holder.photo);

    }

    @Override
    public int getItemCount() {
        if (list.getValue()!=null) {
            return list.getValue().size();
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
        private RelativeLayout relativeLayout;

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
    public void updateList( List<Collegue> listSearch){
        listNew= new ArrayList<>();
        listNew.addAll(listSearch);


    }
}
