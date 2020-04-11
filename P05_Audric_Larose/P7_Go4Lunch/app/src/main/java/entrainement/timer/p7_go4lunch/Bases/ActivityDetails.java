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

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.ViewModelApi;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.model.Place;
import entrainement.timer.p7_go4lunch.model.Results;
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

    private AdaptateurQuiVient adapter;
    private CoordinatorLayout coordinatorLayout;

    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private List<String> myLikes;
    private List<Collegue> listedecollegues = new ArrayList<>();
    private String etoileData;
    private ViewModelApi viewModelApi;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        ButterKnife.bind(this);
        coordinatorLayout = findViewById(R.id.coordinate2);
        RelativeLayout myChoice = findViewById(R.id.checkGrand);
        RatingBar etoiles= findViewById(R.id.ratingdetails);
        myLikes = Me.getMyLikes();
        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            Results place= (Results) extra.getSerializable("Place");
            if (place.getRating() != null) {
                etoileData = extra.getString("etoile");
                etoiles.setRating(Float.parseFloat(place.getRating().toString()));
            } else {
                place.setRating(Double.valueOf(0));
            }
            if (place.getPhotos().get(0) != null) {
                String html= Other.getUrlimage(place.getPhotos().get(0).getPhotoReference(), String.valueOf(place.getPhotos().get(0).getWidth()));
                Picasso.get().load(html).into(image);
            }
            if (place.getphone == null) {
                tel.setEnabled(false);
                tel.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
            }
            if (place.getWebsite() == null || place.getWebsite().equals("null")) {
                internet.setEnabled(false);
                internet.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
            }
            adresse.setText(place.getVicinity());
            nom.setText(place.getName());

            if (Me.getMon_choix()!=null) {
                if (Me.getMon_choix().equals(place.getName())) {
                    myChoice.setVisibility(View.GONE);
                    put_me_Out.setVisibility(View.VISIBLE);
                } else{
                    myChoice.setVisibility(View.VISIBLE);
                    put_me_Out.setVisibility(View.GONE);
                }
            }
            if (myLikes != null) {
                if (myLikes.contains(place.getId())) {
                    like.setVisibility(View.GONE);
                    unlikebutton.setVisibility(View.VISIBLE);
                }
            }
            Other.internetVerify(ActivityDetails.this);
            listedecollegues = servicePlace.compareCollegueNPlace(place, ActivityDetails.this);

            myChoice.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    listedecollegues.add(new Collegue(Me.getMonNOm(), "mon choix", Me.getMaPhoto()));
                    Other.decrement(Me.getMon_choix());
                    Me.setMon_choix(place.getName());
                    listedecollegues = servicePlace.compareCollegueNPlace(place, ActivityDetails.this);
                    serviceCollegue.addmychoice(Me.getMonId(), place.getName(), place.getVicinity(), place.getId(), etoileData, Me.getId_monchoix());
                    serviceCollegue.twentyFourHourLast(ActivityDetails.this, true);
                    servicePlace.saveMyPlace(place);
                    serviceCollegue.whenNotifyme(ActivityDetails.this, true, place.getName());
                    myChoice.setVisibility(View.GONE);
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
                    String phone1 = "tel:" + place.getGetphone();
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
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getWebsite()));
                    startActivity(intent);
                }
            });
            put_me_Out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePlace.unsaveMyPlace(place);
                    listedecollegues = servicePlace.compareCollegueNPlace(place, ActivityDetails.this);
                    listedecollegues.remove(new Collegue(Me.getMonNOm(), getString(R.string.mychoice), Me.getMaPhoto()));
                    serviceCollegue.whenNotifyme(ActivityDetails.this, false, place.getName());
                    put_me_Out.setVisibility(View.GONE);
                    myChoice.setVisibility(View.VISIBLE);
                    Snackbar.make(coordinatorLayout, getString(R.string.retired), Snackbar.LENGTH_LONG).show();
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servicePlace.addMyChoice(place.getId(), false, true);
                    servicePlace.ilike(place);
                    Snackbar.make(coordinatorLayout, getString(R.string.youlike), Snackbar.LENGTH_LONG).show();
                    like.setVisibility(View.GONE);
                    unlikebutton.setVisibility(View.VISIBLE);
                    myLikes.add(place.getId());
                    Me.setMyLikes(myLikes);
//                    etoiles.setRating(Integer.parseInt(etoileData.trim()) + 1);
                }
            });
            unlikebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(coordinatorLayout, getString(R.string.dontlike), Snackbar.LENGTH_LONG).show();
                    servicePlace.unlike(place);
//                    Toast.makeText(ActivityDetails.this, R.string.notlike, Toast.LENGTH_SHORT).show();
                    unlikebutton.setVisibility(View.GONE);
                    like.setVisibility(View.VISIBLE);
                    myLikes.remove(place.getId());
                    Me.setMyLikes(myLikes);
//                    etoiles.setRating(Integer.parseInt(etoileData.trim()) - 1);
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
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
