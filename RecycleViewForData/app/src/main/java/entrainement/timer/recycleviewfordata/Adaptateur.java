package entrainement.timer.recycleviewfordata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.zip.Inflater;

public class Adaptateur extends RecyclerView.Adapter<Adaptateur.LeHolder> {
    private List<Case_Item> liste;
    private Context context;

    public Adaptateur(List<Case_Item> liste, Context context) {
        this.liste = liste;
        this.context= context;
    }

    @NonNull
    @Override
    public LeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_layout, parent, false);
        LeHolder holder = new LeHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeHolder holder, int position) {
        Case_Item case_item= liste.get(position);
//        holder.titre.setText(case_item.getTitre());
        Glide.with(context).load(case_item.getUrl()).into(holder.photos);
        holder.legende.setText(case_item.getTextlong());
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public static class LeHolder extends RecyclerView.ViewHolder{
//        private TextView titre;
        private ImageView photos;
        private TextView legende;
        public LeHolder(@NonNull View itemView) {
            super(itemView);
//            titre=itemView.findViewById(R.id.titre);
            photos=itemView.findViewById(R.id.image);
            legende=itemView.findViewById(R.id.legende);

        }
    }
}
