package entrainement.timer.p7_go4lunch.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.Collegue.AdaptateurQuiVient;
import entrainement.timer.p7_go4lunch.Collegue.Collegue;
import entrainement.timer.p7_go4lunch.Collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.Collegue.ViewModelCollegue;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.Other;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.Restaurant.ExtendedServicePlace;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.system.Os.remove;

public class ActivityDetails extends AppCompatActivity {
    private TextView nom;
    private TextView adresse;
    private TextView etoile;
    private ImageView image;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Collegue> listedecollegues = new ArrayList<>();
    private ExtendedServiceCollegue serviceCollegue;
    private ExtendedServicePlace servicePlace;
    private RelativeLayout buttonImage;
    private RelativeLayout put_me_Out;
    private CardView unlikebutton;
    private CardView tel;
    private CardView internet;
    private Me me;
    private List<String> myLikes ;
    private ViewModelCollegue viewModelCollegue;
    private CardView like;
    private String idData;
    private String etoileData;
    private String adresseData;
    private String phone;
    private String site;
    private boolean everything_is_here = false;
    private ProgressBar progressBar;
    private Other other= new Other();
    private CoordinatorLayout coordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        nom = findViewById(R.id.nomGrand);
        adresse = findViewById(R.id.adresseGrand);
        buttonImage = findViewById(R.id.checkGrand);
        like = findViewById(R.id.likeGrand);
        tel = findViewById(R.id.callGrand);
        internet = findViewById(R.id.wesiteGrand);
        unlikebutton = findViewById(R.id.likenotGrand);
        put_me_Out = findViewById(R.id.checknotGrand);
        other.internetVerify(ActivityDetails.this);
        RatingBar etoiles = (RatingBar) findViewById(R.id.ratingdetails);
        Me me = new Me();
        myLikes= me.getMyLikes();
        String monId = me.getMonId();
        String monNom = me.getMonNOm();
        String maPhoto = me.getMaPhoto();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        Bundle extra = getIntent().getExtras();
        serviceCollegue = DI.getService();
        servicePlace = DI.getServicePlace();
        coordinatorLayout= findViewById(R.id.coordinate2);
        if (extra != null) {
            if (extra.getString("id") != null) {
                idData = extra.getString("id");

            }
            String etoile = extra.getString("etoile");
            if (etoile != null) {
                etoileData = extra.getString("etoile");
                etoiles.setRating(Integer.parseInt(etoileData.trim()));
            } else {
                String etoileData = "0";
            }
            String adresseData = extra.getString("adresse");
            adresse.setText(adresseData);
            site = extra.getString("site");
            phone = extra.getString("phone");
        }
        if (phone == null) {
               tel.setEnabled(false);
            tel.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
        }

        if (site == null) {
            internet.setEnabled(false);
            internet.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
        }

//        List<String> infos = servicePlace.getMyPlace(idData);
        String nomData = extra.getString("nom");
        String monChoix = me.getMon_choix();
        String nomData1 = nomData;
        nom.setText(nomData);
        if (monChoix.equals(nomData1)) {
            buttonImage.setVisibility(View.GONE);
            put_me_Out.setVisibility(View.VISIBLE);
        }
        if (myLikes!=null) {
            if (myLikes.contains(idData)) {
                like.setVisibility(View.GONE);
                unlikebutton.setVisibility(View.VISIBLE);
            }
        }
//                List<String> note= servicePlace.getPlaceInfo(nomData);

        listedecollegues = servicePlace.compareCollegueNPlace(nomData, ActivityDetails.this.idData,ActivityDetails.this);

        String finalEtoileData = etoileData;
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // servicePlace.oldRestaurantErase(me.getId_monchoix());
                Snackbar.make(coordinatorLayout,"Place ajouté !",Snackbar.LENGTH_LONG).show();
                listedecollegues = servicePlace.compareCollegueNPlace(me.getMon_choix(), me.getId_monchoix(),ActivityDetails.this);
                listedecollegues = servicePlace.compareCollegueNPlace(nomData, ActivityDetails.this.idData,ActivityDetails.this);
                servicePlace.saveMyPlace(monNom, ActivityDetails.this.idData);
                serviceCollegue.addmychoice(monId, nomData, adresseData, ActivityDetails.this.idData, finalEtoileData, me.getId_monchoix());
                listedecollegues.add(new Collegue(me.getMonNOm(), "mon choix", me.getMaPhoto()));
                buttonImage.setVisibility(View.GONE);
                serviceCollegue.whenNotifyme(ActivityDetails.this, true, nomData);
                put_me_Out.setVisibility(View.VISIBLE);
                serviceCollegue.twentyFourHourLast(ActivityDetails.this, true);
//                        servicePlace.addMyChoice(idData, true,false);
            }
        });
        tel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Snackbar.make(coordinatorLayout,getString(R.string.call),Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                String phone1= "tel:"+phone;
                intent.setData(Uri.parse(phone1));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                          ActivityCompat.requestPermissions(ActivityDetails.this,new String[]{CALL_PHONE},1);
                } else {
                    startActivity(intent);
                }
                    }
                });
        internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(coordinatorLayout,getString(R.string.redirection),Snackbar.LENGTH_LONG).show();
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(site));
                startActivity(intent);
            }
        });
                put_me_Out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(coordinatorLayout,getString(R.string.retired),Snackbar.LENGTH_LONG).show();
                        servicePlace.unsaveMyPlace(idData);
                        listedecollegues = servicePlace.compareCollegueNPlace(nomData, ActivityDetails.this.idData,ActivityDetails.this);
                        listedecollegues.remove(new Collegue(me.getMonNOm(), getString(R.string.mychoice), me.getMaPhoto()));
                        put_me_Out.setVisibility(View.GONE);
                        buttonImage.setVisibility(View.VISIBLE);
                        serviceCollegue.whenNotifyme(ActivityDetails.this,false, nomData);
                    }
                });
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        servicePlace.ilike(ActivityDetails.this.idData);
                        Snackbar.make(coordinatorLayout,getString(R.string.youlike),Snackbar.LENGTH_LONG).show();
                        like.setVisibility(View.GONE);
                        unlikebutton.setVisibility(View.VISIBLE);
                        servicePlace.addMyChoice(ActivityDetails.this.idData, false,true);
                        myLikes.add(idData);
                        me.setMyLikes(myLikes);
                        etoiles.setRating(Integer.parseInt(etoileData.trim())+1);
                    }
                });
                unlikebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(coordinatorLayout,getString(R.string.dontlike),Snackbar.LENGTH_LONG).show();

                        servicePlace.unlike(idData, nomData);
                        Toast.makeText(ActivityDetails.this,R.string.notlike, Toast.LENGTH_SHORT).show();
                        unlikebutton.setVisibility(View.GONE);
                        like.setVisibility(View.VISIBLE);
                        myLikes.remove(idData);
                        me.setMyLikes(myLikes);
                        etoiles.setRating(Integer.parseInt(etoileData.trim())-1);
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
