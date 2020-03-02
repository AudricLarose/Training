package entrainement.timer.p7_go4lunch.Restaurant;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Activities.ActivityDetails;
import entrainement.timer.p7_go4lunch.Collegue.ViewModelCollegue;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.R;


public class FragmentResto extends Fragment {
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Place> liste2Place= new ArrayList<>();
    private ExtendedServicePlace service;
    private ViewModelCollegue viewModelCollegue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        viewModelCollegue= new ViewModelProvider(requireActivity()).get(ViewModelCollegue.class);
//        service= DI.getServicePlace();
        View view= inflater.inflate(R.layout.fragment_fragment_resto, container, false);
//        recyclerView.setHasFixedSize(true);
////        liste2Place=service.getListOfPlace();
//        adapter= new AdaptateurPlace(viewModelCollegue.getPlace(),this);
//        layoutManager= new LinearLayoutManager(view.getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        Query query= firebaseFirestore.collection("restaurant");
//        recyclerView= (RecyclerView) view;
        recyclerView=view.findViewById(R.id.recycleContact);
        FirestoreRecyclerOptions <Place> options = new FirestoreRecyclerOptions.Builder<Place>()
                .setQuery(query,Place.class)
                .build();
         adapter= new FirestoreRecyclerAdapter<Place,FireHolder>(options) {

            @NonNull
            @Override
            public FireHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowresto,parent,false);
                return new FireHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FireHolder fireHolder, int i, @NonNull Place place) {
                fireHolder.nom.setText(place.getnomPlace());
                fireHolder.adresse.setText(place.getAdresse());
                fireHolder.perso.setText(place.getquivient());
                fireHolder.etoile.setText(place.getnote());
                fireHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ActivityDetails.class);
                        intent.putExtra("id",place.getId());
                        intent.putExtra("nom",place.getnomPlace());
                        intent.putExtra("adresse",place.getAdresse());
                        v.getContext().startActivity(intent);

                    }
                });
            }
        };
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;
    }

    private class FireHolder extends RecyclerView.ViewHolder{
        private RelativeLayout relativeLayout;
        private TextView nom;
        private TextView adresse;
        private TextView perso;
        private TextView distance;
        private TextView horaire;
        private TextView etoile;
        private ImageView photo;
        public FireHolder(@NonNull View itemView) {
            super(itemView);
            nom=itemView.findViewById(R.id.nomresto);
            adresse=itemView.findViewById(R.id.adresse);
            perso=itemView.findViewById(R.id.perso);
            distance=itemView.findViewById(R.id.distance);
            horaire=itemView.findViewById(R.id.horaire);
            etoile=itemView.findViewById(R.id.etoile);
            photo=itemView.findViewById(R.id.photoresto);
            relativeLayout=itemView.findViewById(R.id.relativeRowPlace);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
