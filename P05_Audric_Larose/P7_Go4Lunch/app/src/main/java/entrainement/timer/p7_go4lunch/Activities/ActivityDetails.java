package entrainement.timer.p7_go4lunch.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Collegue.AdaptateurQuiVient;
import entrainement.timer.p7_go4lunch.Collegue.Collegue;
import entrainement.timer.p7_go4lunch.Collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.Collegue.ViewModelCollegue;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.Restaurant.ExtendedServicePlace;

public class ActivityDetails extends AppCompatActivity {
    private TextView nom;
    private TextView adresse;
    private TextView etoile;
    private ImageView image;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Collegue> listedecollegues= new ArrayList<>();
    private ExtendedServiceCollegue serviceCollegue;
    private ExtendedServicePlace servicePlace;
    private ImageView buttonImage;
    private ImageView put_me_Out;
    private Button unlikebutton;
    private Me me;
    private ViewModelCollegue viewModelCollegue;
    private Button like;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        nom = findViewById(R.id.nomGrand);
        adresse = findViewById(R.id.adresseGrand);
        buttonImage = findViewById(R.id.checkGrand);
        like = findViewById(R.id.likeGrand);
        unlikebutton = findViewById(R.id.likenotGrand);
        put_me_Out = findViewById(R.id.checknotGrand);

        Me me = new Me();
        String monId = me.getMonId();
        String monNom=me.getMonNOm();
        String maPhoto=me.getMaPhoto();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        Bundle extra = getIntent().getExtras();
        serviceCollegue = DI.getService();
        servicePlace=DI.getServicePlace();

        if (extra != null) {
            String idData=extra.getString("id");
            String nomData = extra.getString("nom");
            String adresseData = extra.getString("adresse");
            nom.setText(nomData);
            adresse.setText(adresseData);
            listedecollegues = servicePlace.compareCollegueNPlace(nomData);
            buttonImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePlace.saveMyPlace(monNom,idData);
                    serviceCollegue.addmychoice(monId, nomData, adresseData);
                    listedecollegues.add(new Collegue(me.getMonNOm(), "mon choix", me.getMaPhoto()));
                    buttonImage.setVisibility(View.GONE);
                    put_me_Out.setVisibility(View.VISIBLE);
                }
            });
            put_me_Out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePlace.unsaveMyPlace(monNom, maPhoto, nomData);
                    serviceCollegue.addmychoice(monId, nomData, adresseData);
                    listedecollegues.remove(new Collegue(me.getMonNOm(), "mon choix", me.getMaPhoto()));
                    put_me_Out.setVisibility(View.GONE);
                    buttonImage.setVisibility(View.VISIBLE);
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePlace.ilike(idData, monId);
                    Toast.makeText(ActivityDetails.this, "Liké !", Toast.LENGTH_SHORT).show();
                    like.setVisibility(View.GONE);
                    unlikebutton.setVisibility(View.VISIBLE);
                }
            });

            unlikebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePlace.unlike(nomData, monId);
                    Toast.makeText(ActivityDetails.this, "Like enlevé !", Toast.LENGTH_SHORT).show();
                    unlikebutton.setVisibility(View.GONE);
                    like.setVisibility(View.VISIBLE);
                }
            });
        }
        viewModelCollegue=new ViewModelProvider(ActivityDetails.this).get(ViewModelCollegue.class);
        recyclerView = findViewById(R.id.RecycleGrand);
        recyclerView.setHasFixedSize(true);
        adapter= new AdaptateurQuiVient(viewModelCollegue.getwhocome(),this);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extra = getIntent().getExtras();
        String idData=extra.getString("id");
        String nomData = extra.getString("nom");
        nom.setText(nomData);
        listedecollegues = servicePlace.compareCollegueNPlace(nomData);

    }
}
