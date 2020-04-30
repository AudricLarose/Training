package entrainement.timer.p7_go4lunch.utils.collegue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.model.Collegue;


public class AdaptateurQuiVient extends RecyclerView.Adapter<AdaptateurQuiVient.LeHolder> {
    LiveData<List<Collegue>> quivientliste;

    public AdaptateurQuiVient(LiveData<List<Collegue>> quivientliste, LifecycleOwner owner) {
        this.quivientliste = quivientliste;
        this.quivientliste.observe(owner, new Observer<List<Collegue>>() {
            @Override
            public void onChanged(List<Collegue> collegues) {
                AdaptateurQuiVient.this.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public AdaptateurQuiVient.LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowquivient, parent, false);
        AdaptateurQuiVient.LeHolder leHolder = new AdaptateurQuiVient.LeHolder(view);
        return leHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptateurQuiVient.LeHolder holder, int position) {
        Collegue collegue = quivientliste.getValue().get(position);
        holder.nom.setText(collegue.getName());
        String photoUri = collegue.getPhoto();
        Picasso.get().load(photoUri).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        if (quivientliste != null) {
            return quivientliste.getValue().size();
        } else {
            return 0;
        }
    }

    public static class LeHolder extends RecyclerView.ViewHolder {
        private TextView nom;
        private CircularImageView photo;

        public LeHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nomRecyGrand);
            photo = itemView.findViewById(R.id.photoRecyGrand);

        }
    }
}


