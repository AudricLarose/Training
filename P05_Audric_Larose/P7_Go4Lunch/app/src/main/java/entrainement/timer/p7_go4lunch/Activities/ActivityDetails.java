package entrainement.timer.p7_go4lunch.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
    private RelativeLayout buttonImage;
    private RelativeLayout put_me_Out;
    private CardView unlikebutton;
    private Me me;
    private ViewModelCollegue viewModelCollegue;
    private CardView like;
    private String idData;
    private String etoileData;
    private boolean everything_is_here=false;
    private ProgressBar progressBar;


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

        RatingBar etoiles = (RatingBar) findViewById(R.id.ratingdetails);
        Me me = new Me();
        String monId = me.getMonId();
        String monNom=me.getMonNOm();
        String maPhoto=me.getMaPhoto();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        Bundle extra = getIntent().getExtras();
        serviceCollegue = DI.getService();
        servicePlace=DI.getServicePlace();
        if (extra != null) {
            if ( extra.getString("id")!=null) {
                idData = extra.getString("id");
            }
        }

        List<String> infos=servicePlace.getMyPlace(idData);
                String nomData = extra.getString("nom");
        String monChoix = me.getMon_choix();
        String nomData1 = nomData;
        if (monChoix.equals(nomData1)){
            buttonImage.setVisibility(View.GONE);
            put_me_Out.setVisibility(View.VISIBLE);
        }
        List<String> myLikes = me.getMyLikes();
        String idData = this.idData;
        if (myLikes.contains(idData)){
            like.setVisibility(View.GONE);
            unlikebutton.setVisibility(View.VISIBLE);
        }
                List<String> note= servicePlace.getPlaceInfo(nomData);
                String adresseData = extra.getString("adresse");
                if (extra.getString("etoile") != null) {
                     etoileData = extra.getString("etoile");
                    etoiles.setRating(Integer.parseInt(etoileData.trim()));
                } else {
                   String etoileData="0";
                }
                nom.setText(nomData);
                adresse.setText(adresseData);
                listedecollegues = servicePlace.compareCollegueNPlace(nomData, this.idData);
                String finalEtoileData = etoileData;
                buttonImage.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        servicePlace.saveMyPlace(monNom, ActivityDetails.this.idData);
                        serviceCollegue.addmychoice(monId, nomData, adresseData, ActivityDetails.this.idData, finalEtoileData, me.getId_monchoix());
                      //  listedecollegues.add(new Collegue(me.getMonNOm(), "mon choix", me.getMaPhoto()));
                        buttonImage.setVisibility(View.GONE);
                        serviceCollegue.whenNotifyme(ActivityDetails.this,true,nomData);
                        put_me_Out.setVisibility(View.VISIBLE);
                        serviceCollegue.twentyFourHourLast(ActivityDetails.this,true);
//                        servicePlace.addMyChoice(idData, true,false);
                    }
                });
                put_me_Out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicePlace.unsaveMyPlace(monNom, maPhoto, nomData);
                        serviceCollegue.addmychoice(monId, nomData, adresseData, ActivityDetails.this.idData,finalEtoileData,me.getId_monchoix());
                        listedecollegues.remove(new Collegue(me.getMonNOm(), "mon choix", me.getMaPhoto()));
                        put_me_Out.setVisibility(View.GONE);
                        buttonImage.setVisibility(View.VISIBLE);
                        serviceCollegue.whenNotifyme(ActivityDetails.this,false, nomData);


                    }
                });
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicePlace.ilike(ActivityDetails.this.idData);
                        Toast.makeText(ActivityDetails.this, "Liké !", Toast.LENGTH_SHORT).show();
                        like.setVisibility(View.GONE);
                        unlikebutton.setVisibility(View.VISIBLE);
                        servicePlace.addMyChoice(ActivityDetails.this.idData, false,true);

                    }
                });

                unlikebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicePlace.unlike(idData, nomData);
                        Toast.makeText(ActivityDetails.this, "Like enlevé !", Toast.LENGTH_SHORT).show();
                        unlikebutton.setVisibility(View.GONE);
                        like.setVisibility(View.VISIBLE);
                    }
                });
        viewModelCollegue=new ViewModelProvider(ActivityDetails.this).get(ViewModelCollegue.class);
        recyclerView = findViewById(R.id.RecycleGrand);
        recyclerView.setHasFixedSize(true);
        List<Collegue> valeur=viewModelCollegue.getwhocome().getValue();
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
//        Bundle extra = getIntent().getExtras();
//        String idData=extra.getString("id");
//        String nomData = extra.getString("nom");
//        nom.setText(nomData);
//        listedecollegues = servicePlace.compareCollegueNPlace(nomData,idData);

    }
}
