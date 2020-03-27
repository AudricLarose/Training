package entrainement.timer.p7_go4lunch.Bases;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.ViewModelApi;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.utils.Other;
import entrainement.timer.p7_go4lunch.utils.collegue.AdaptateurQuiVient;

import static android.Manifest.permission.CALL_PHONE;

public class ActivityDetails extends AppCompatActivity {

    @BindView(R.id.nomGrand)
    TextView nom;
    @BindView(R.id.adresseGrand)
    TextView adresse;
    @BindView(R.id.wesiteGrand)
    CardView internet;
    @BindView(R.id.likeGrand)
    CardView like;
    @BindView(R.id.likenotGrand)
    CardView unlikebutton;
    @BindView(R.id.checknotGrand)
    RelativeLayout put_me_Out;
    @BindView(R.id.imageGrand)
    ImageView image;
    @BindView(R.id.callGrand)
    CardView tel;

    private TextView etoile;
    private RecyclerView recyclerView;
    private AdaptateurQuiVient adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Collegue> listedecollegues = new ArrayList<>();
    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private Me me = new Me();
    private List<String> myLikes;
    private ViewModelApi viewModelApi;
    private String idData;
    private String etoileData;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        coordinatorLayout = findViewById(R.id.coordinate2);
        RelativeLayout buttonImage = findViewById(R.id.checkGrand);
        RatingBar etoiles= findViewById(R.id.ratingdetails);


        myLikes = me.getMyLikes();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String idData = extra.getString("id");
            String adresseData = extra.getString("adresse");
            String etoile = extra.getString("etoile");
            String nomData = extra.getString("nom");
            String phone = extra.getString("phone");
            String photo = extra.getString("photo");
            String site = extra.getString("site");
            if (etoile != null) {
                etoileData = extra.getString("etoile");
                etoiles.setRating(Integer.parseInt(etoileData.trim()));
            } else {
                String etoileData = "0";
            }

            if (photo != null) {
                Picasso.get().load(photo).into(image);
            }
            if (phone == null) {
                tel.setEnabled(false);
                tel.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
            }
            if (site == null || site.equals("null")) {
                internet.setEnabled(false);
                internet.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
            }

            adresse.setText(adresseData);
            nom.setText(nomData);

            if (me.getMon_choix().equals(nomData)) {
                buttonImage.setVisibility(View.GONE);
                put_me_Out.setVisibility(View.VISIBLE);
            }
            if (myLikes != null) {
                if (myLikes.contains(idData)) {
                    like.setVisibility(View.GONE);
                    unlikebutton.setVisibility(View.VISIBLE);
                }
            }
            Other.internetVerify(ActivityDetails.this);
            listedecollegues = servicePlace.compareCollegueNPlace(nomData, ActivityDetails.this.idData, ActivityDetails.this);

            buttonImage.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    listedecollegues.add(new Collegue(me.getMonNOm(), "mon choix", me.getMaPhoto()));
                    listedecollegues = servicePlace.compareCollegueNPlace(me.getMon_choix(), me.getId_monchoix(), ActivityDetails.this);
                    listedecollegues = servicePlace.compareCollegueNPlace(nomData, ActivityDetails.this.idData, ActivityDetails.this);
                    serviceCollegue.addmychoice(me.getMonId(), nomData, adresseData, ActivityDetails.this.idData, etoileData, me.getId_monchoix());
                    serviceCollegue.twentyFourHourLast(ActivityDetails.this, true);
                    servicePlace.saveMyPlace(me.getMonNOm(), ActivityDetails.this.idData);
                    serviceCollegue.whenNotifyme(ActivityDetails.this, true, nomData);
                    buttonImage.setVisibility(View.GONE);
                    put_me_Out.setVisibility(View.VISIBLE);
                    Snackbar.make(coordinatorLayout, getString(R.string.newPlace), Snackbar.LENGTH_LONG).show();

                }
            });
            tel.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Snackbar.make(coordinatorLayout, getString(R.string.call), Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    String phone1 = "tel:" + phone;
                    intent.setData(Uri.parse(phone1));
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ActivityDetails.this, new String[]{CALL_PHONE}, 1);
                    } else {
                        startActivity(intent);
                    }
                }
            });
            internet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
                    startActivity(intent);
                }
            });
            put_me_Out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(coordinatorLayout, getString(R.string.retired), Snackbar.LENGTH_LONG).show();
                    servicePlace.unsaveMyPlace(idData);
                    listedecollegues = servicePlace.compareCollegueNPlace(nomData, ActivityDetails.this.idData, ActivityDetails.this);
                    listedecollegues.remove(new Collegue(me.getMonNOm(), getString(R.string.mychoice), me.getMaPhoto()));
                    put_me_Out.setVisibility(View.GONE);
                    buttonImage.setVisibility(View.VISIBLE);
                    serviceCollegue.whenNotifyme(ActivityDetails.this, false, nomData);
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePlace.ilike(ActivityDetails.this.idData);
                    Snackbar.make(coordinatorLayout, getString(R.string.youlike), Snackbar.LENGTH_LONG).show();
                    like.setVisibility(View.GONE);
                    unlikebutton.setVisibility(View.VISIBLE);
                    servicePlace.addMyChoice(ActivityDetails.this.idData, false, true);
                    myLikes.add(idData);
                    me.setMyLikes(myLikes);
                    etoiles.setRating(Integer.parseInt(etoileData.trim()) + 1);
                }
            });
            unlikebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(coordinatorLayout, getString(R.string.dontlike), Snackbar.LENGTH_LONG).show();

                    servicePlace.unlike(idData, nomData);
                    Toast.makeText(ActivityDetails.this, R.string.notlike, Toast.LENGTH_SHORT).show();
                    unlikebutton.setVisibility(View.GONE);
                    like.setVisibility(View.VISIBLE);
                    myLikes.remove(idData);
                    me.setMyLikes(myLikes);
                    etoiles.setRating(Integer.parseInt(etoileData.trim()) - 1);
                }
            });
            viewModelApi = new ViewModelProvider(ActivityDetails.this).get(ViewModelApi.class);
            adapter = new AdaptateurQuiVient(viewModelApi.getwhocome(), this);
            recyclerView = findViewById(R.id.RecycleGrand);
            recyclerView.setHasFixedSize(true);
            List<Collegue> valeur = viewModelApi.getwhocome().getValue();
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
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
