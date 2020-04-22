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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.ViewModelApi;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.model.Me;
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

    private CoordinatorLayout coordinatorLayout;

    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private List<String> myLikes= new ArrayList<>();
    private List<Collegue> listedecollegues = new ArrayList<>();
    private String etoileData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        coordinatorLayout = findViewById(R.id.coordinate2);
        RelativeLayout myChoice = findViewById(R.id.checkGrand);
        RatingBar etoiles= findViewById(R.id.ratingdetails);
        ButterKnife.bind(this);
        myLikes = Me.getMyLikes();
        Other.internetVerify(ActivityDetails.this);
        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();


        if (extra != null) {
            Results place= (Results) extra.getSerializable("Place");
            if ((place!=null) &&(place.getPlaceId()!=null)){
       Other.CallApiOnOnePlacePlease(place.getPlaceId(), new Other.Finish() {
                    @Override
                    public void onFinish(Results apiforOnePlaces) {
                        if (apiforOnePlaces!=null){
                            if (apiforOnePlaces.getFormattedPhoneNumber() == null) {
                                tel.setEnabled(false);
                                tel.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
                            }
                            if (apiforOnePlaces.getWebsite() == null || place.getWebsite().equals("null")) {
                                internet.setEnabled(false);
                                internet.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
                            }

                        } else {
                            tel.setEnabled(false);
                            internet.setEnabled(false);
                        }
                        tel.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                String phone1 = "tel:" + Objects.requireNonNull(apiforOnePlaces).getFormattedPhoneNumber();
                                intent.setData(Uri.parse(phone1));
                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ActivityDetails.this, new String[]{CALL_PHONE}, 1);
                                } else {
                                    startActivity(intent);
                                }
                                Snackbar.make(coordinatorLayout, getString(R.string.call), Snackbar.LENGTH_LONG).show();
                            }

                        });
                        internet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = null;
                                if (apiforOnePlaces != null) {
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apiforOnePlaces.getWebsite()));
                                }
                                startActivity(intent);
                            }
                        });
                    }
                });

                if (place.getPhotos()!=null) {
                    if (place.getPhotos().get(0) != null) {
                        String html= Other.getUrlimage(place.getPhotos().get(0).getPhotoReference(), String.valueOf(place.getPhotos().get(0).getWidth()));
                        Picasso.get().load(html).into(image);
                    }
                }
                if (place.getRating() != null) {
                etoileData = extra.getString("etoile");
                etoiles.setRating(Float.parseFloat(place.getRating().toString()));
            } else {
                place.setRating(Double.valueOf(0));
            }

            adresse.setText(place.getVicinity());
            nom.setText(place.getName());

            if (Me.getMy_choice()!=null) {
                if (Me.getMy_choice().equals(place.getName())) {
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
            listedecollegues = servicePlace.CompareCollegueNPlace(place, ActivityDetails.this);

            myChoice.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    listedecollegues.add(new Collegue(Me.getMyName(), getString(R.string.mychoice), Me.getMyPhoto()));
                    Other.decrement(Me.getMy_choice());
                    Me.setMy_choice(place.getName());
                    serviceCollegue.addMyChoice(Me.getMyId(), place.getName(), place.getVicinity(), place.getPlaceId(), place.getRating().toString(), place.getPhotos().get(0).getPhotoReference());
                    listedecollegues = servicePlace.CompareCollegueNPlace(place, ActivityDetails.this);
                    serviceCollegue.twentyFourHourLast(ActivityDetails.this, true);
                    servicePlace.saveMyPlace(place);
                    serviceCollegue.whenNotifyme(ActivityDetails.this, true, place.getName());
                    myChoice.setVisibility(View.GONE);
                    put_me_Out.setVisibility(View.VISIBLE);
                    Snackbar.make(coordinatorLayout, getString(R.string.newPlace), Snackbar.LENGTH_LONG).show();
                }
            });
            put_me_Out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listedecollegues.remove(new Collegue(Me.getMyName(), getString(R.string.mychoice), Me.getMyPhoto()));
                    Me.setMy_choice(" ");
                    Me.setId_mychoice(" ");
                    serviceCollegue.dontaddMyChoice(Me.getMyId(), place.getName(), place.getVicinity(), place.getPlaceId(), etoileData, Me.getId_mychoice());
                    listedecollegues = servicePlace.CompareCollegueNPlace(place, ActivityDetails.this);
                    serviceCollegue.twentyFourHourLast(ActivityDetails.this, true);
                    servicePlace.unsaveMyPlace(place);
                    serviceCollegue.whenNotifyme(ActivityDetails.this, false, place.getName());
                    put_me_Out.setVisibility(View.GONE);
                    myChoice.setVisibility(View.VISIBLE);
                    Snackbar.make(coordinatorLayout, getString(R.string.retired), Snackbar.LENGTH_LONG).show();
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myLikes=new ArrayList<>();
                    myLikes.add(place.getId());
                    Me.setMyLikes(myLikes);
                    servicePlace.iLike(place);
                    servicePlace.addMyChoice(place.getId(), false, true);
                    like.setVisibility(View.GONE);
                    unlikebutton.setVisibility(View.VISIBLE);
                    Snackbar.make(coordinatorLayout, getString(R.string.youlike), Snackbar.LENGTH_LONG).show();
//                    etoiles.setRating(Integer.parseInt(etoileData.trim()) + 1);
                }
            });
            unlikebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myLikes.remove(place.getId());
                    Me.setMyLikes(myLikes);
                    servicePlace.unLike(place);
                    unlikebutton.setVisibility(View.GONE);
                    like.setVisibility(View.VISIBLE);
                    Snackbar.make(coordinatorLayout, getString(R.string.dontlike), Snackbar.LENGTH_LONG).show();
//                    etoiles.setRating(Integer.parseInt(etoileData.trim()) - 1);
                }
            });
                ViewModelApi viewModelApi = new ViewModelProvider(ActivityDetails.this).get(ViewModelApi.class);
                AdaptateurQuiVient adapter = new AdaptateurQuiVient(viewModelApi.getwhocome(), this);
                RecyclerView recyclerView = findViewById(R.id.RecycleGrand);
            recyclerView.setHasFixedSize(true);
            List<Collegue> valeur = viewModelApi.getwhocome().getValue();
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
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
