package entrainement.timer.timerwear;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class WRecycle extends RecyclerView.Adapter<WRecycle.Leholder> {
    private ArrayList<item> list;

    public WRecycle(ArrayList<item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Leholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listev, parent, false);
        Leholder leholder = new Leholder(view);
        return leholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Leholder holder, int position) {
        item item = list.get(position);
        final int valeur= item.getMinutes();
        holder.minutes.setText(toString().valueOf(valeur));
        holder.minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), timer.class);
                intent.putExtra("valeur",valeur);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Leholder extends RecyclerView.ViewHolder{
        public TextView minutes;
        public Leholder(@NonNull View itemView) {
            super(itemView);
            minutes= itemView.findViewById(R.id.minutes);
        }
    }
}
