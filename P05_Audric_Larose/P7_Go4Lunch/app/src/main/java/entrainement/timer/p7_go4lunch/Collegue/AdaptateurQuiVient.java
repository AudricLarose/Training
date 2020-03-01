package entrainement.timer.p7_go4lunch.Collegue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.R;


public class AdaptateurQuiVient extends RecyclerView.Adapter <AdaptateurQuiVient.LeHolder> {
        List<Collegue> quivientliste = new ArrayList<>();


        public AdaptateurQuiVient(List<Collegue> quivientliste) {
            this.quivientliste = quivientliste;

        }

        @NonNull
        @Override
        public AdaptateurQuiVient.LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowquivient,parent,false);
            AdaptateurQuiVient.LeHolder leHolder= new AdaptateurQuiVient.LeHolder(view);
            return leHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptateurQuiVient.LeHolder holder, int position) {
            Collegue collegue = quivientliste.get(position);
            holder.nom.setText(collegue.getNom());
            String photoUri= collegue.getPhoto();
//            Picasso.get().load(photoUri).into(holder.photo);
        }

        @Override
        public int getItemCount() {
            return quivientliste.size();
        }

      public static class LeHolder extends RecyclerView.ViewHolder {
            private RelativeLayout relativeLayout;
            private TextView nom;
            private TextView choix;
            private ImageView photo;

            public LeHolder(@NonNull View itemView) {
                super(itemView);
                nom=itemView.findViewById(R.id.nomRecyGrand);
                choix=itemView.findViewById(R.id.iscomingRecyGrand);
                photo=itemView.findViewById(R.id.photoRecyGrand);

            }
        }
    }


