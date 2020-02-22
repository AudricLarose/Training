package entrainement.timer.p7_go4lunch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Adaptateur extends RecyclerView.Adapter<Adaptateur.LeHolder> {
    List<Collegue> list= new ArrayList<>();
    Context context;
    URL urlWelcome = null;
    Bitmap bitmap;


    public Adaptateur(List<Collegue> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
    LeHolder holder= new LeHolder(view);
    return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeHolder holder, int position) {
        final Collegue collegue= list.get(position);
        holder.nom.setText(collegue.getNom());
        holder.choix.setText(collegue.getChoix());
        String photoUri= collegue.getPhoto();
        Picasso.get().load(photoUri).into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class LeHolder extends RecyclerView.ViewHolder{
        private TextView nom;
        private TextView choix;
        private ImageView photo;

        public LeHolder(@NonNull View itemView) {
            super(itemView);
            nom=itemView.findViewById(R.id.name);
            choix=itemView.findViewById(R.id.choice);
            photo=itemView.findViewById(R.id.photo);
        }
    }
}
