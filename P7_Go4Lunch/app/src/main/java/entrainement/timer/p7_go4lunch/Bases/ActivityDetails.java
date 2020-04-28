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
import entrainement.timer.p7_go4lunch.utils.ViewModelApi;
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
    @BindView(R.id.nocollegue)
    CardView noplace;
    @BindView(R.id.nointernet)
    ImageView nointernet;

    private CoordinatorLayout coordinatorLayout;

    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private List<String> myLikes = new ArrayList<>();
    private List<Collegue> collegueList = new ArrayList<>();
    private String starData;
    private ViewModelApi viewModelApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        coordinatorLayout = findViewById(R.id.coordinate2);
        RelativeLayout myChoice = findViewById(R.id.checkGrand);
        RatingBar stars = findViewById(R.id.ratingdetails);
        ButterKnife.bind(this);
        myLikes = Me.getMyLikes();
        viewModelApi = new ViewModelProvider(ActivityDetails.this).get(ViewModelApi.class);
        AdaptateurQuiVient adapter = new AdaptateurQuiVient(viewModelApi.getwhocome(), this);

        // I verifiy if User have internet and the GPS on.
        Other.initGlobalVerificationConnectionCheck(ActivityDetails.this);
        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            Results place = (Results) extra.getSerializable("Place");
            if ((place != null) && (place.getPlaceId() != null)) {
                Other.CallApiOnOnePlacePlease(place.getPlaceId(), new Other.Finish() {
                    @Override
                    public void onFinish(Results apiforOnePlaces) {
                        if (apiforOnePlaces != null && Other.internetIsOn(ActivityDetails.this)) {
                            initiatePlaceApiDetails(apiforOnePlaces);
                        } else {
                            tel.setEnabled(false);
                            tel.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
                            internet.setEnabled(false);
                            internet.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
                        }
                    }
                });
                InitiatePlaceNearByElements(myChoice, stars, extra, place);
                nom.setText(place.getName());
                collegueList = servicePlace.CompareCollegueNPlace(place, ActivityDetails.this, new ExtendedServicePlace.Increment() {
                    @Override
                    public void onFinish() {
                        if (collegueList.isEmpty() || viewModelApi.getwhocome().getValue().isEmpty()) {
                            noplace.setVisibility(View.VISIBLE);
                        } else {
                            noplace.setVisibility(View.GONE);
                        }
                    }
                });
                if (Other.internetIsOn(ActivityDetails.this)) {
                    enableButtons(myChoice, place);
                } else {
                    disableButtons(myChoice);
                }
                RecyclerView recyclerView = findViewById(R.id.RecycleGrand);
                recyclerView.bringToFront();
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void initiatePlaceApiDetails(Results apiforOnePlaces) {
        setAdressIfPossible(apiforOnePlaces);
        setPhoneIfPossible(apiforOnePlaces);
        setWebsiteIfPossible(apiforOnePlaces);
        setPhotoIfPossible(apiforOnePlaces);
    }

    private void setAdressIfPossible(Results apifor) {
        if (apifor.getVicinity()!=null) {
            adresse.setText(apifor.getVicinity());
        }
    }

    private void InitiatePlaceNearByElements(RelativeLayout myChoice, RatingBar stars, Bundle extra, Results place) {
        setPhotoIfPossibleApi(place);
        setRatingIfPossibleApi(stars, extra, place);
        setChoiceIfPossibleApi(myChoice, place);
        setLikeIfPossibleApi(place);
    }

    private void setLikeIfPossibleApi(Results place) {
        if (myLikes != null) {
            if (myLikes.contains(place.getPlaceId())) {
                like.setVisibility(View.GONE);
                unlikebutton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setChoiceIfPossibleApi(RelativeLayout myChoice, Results place) {
        if (Me.getMy_choice() != null) {
            if (Me.getMy_choice().equals(place.getName())) {
                myChoice.setVisibility(View.GONE);
                put_me_Out.setVisibility(View.VISIBLE);
            } else {
                myChoice.setVisibility(View.VISIBLE);
                put_me_Out.setVisibility(View.GONE);
            }
        }
    }

    private void setRatingIfPossibleApi(RatingBar stars, Bundle extra, Results place) {
        if (place.getRating() != null) {
            starData = extra.getString("etoile");
            stars.setRating(Float.parseFloat(place.getRating().toString()));
        } else {
            place.setRating(Double.valueOf(0));
        }
    }

    private void setPhotoIfPossibleApi(Results place) {
        if (place.getPhotos() != null) {
            if (place.getPhotos().get(0) != null) {
                String html = Other.getUrlimage(place.getPhotos().get(0).getPhotoReference(), String.valueOf(place.getPhotos().get(0).getWidth()));
                Picasso.get().load(html).into(image);
            }
        } else if (place.getPhoto() != null) {
            if (place.getPhoto() != null) {
                String html = Other.getUrlimage(place.getPhoto(), "300");
                Picasso.get().load(html).into(image);
            }
        }
    }

    private void setPhotoIfPossible(Results apiforOnePlaces) {
        if (apiforOnePlaces.getPhotos() != null) {
            if (apiforOnePlaces.getPhotos().get(0) != null) {
                String html = Other.getUrlimage(apiforOnePlaces.getPhotos().get(0).getPhotoReference(), String.valueOf(apiforOnePlaces.getPhotos().get(0).getWidth()));
                Picasso.get().load(html).into(image);
            }
        }
    }

    private void setWebsiteIfPossible(Results apiforOnePlaces) {
        if (apiforOnePlaces.getWebsite() == null || apiforOnePlaces.getWebsite().equals("null")) {
            internet.setEnabled(false);
            internet.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
        } else {
            internet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apiforOnePlaces.getWebsite()));
                    startActivity(intent);
                }
            });
        }
    }

    private void setPhoneIfPossible(Results apiforOnePlaces) {
        if (apiforOnePlaces.getFormattedPhoneNumber() == null) {
            tel.setEnabled(false);
            tel.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
        } else {
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
        }
    }

    private void enableButtons(RelativeLayout myChoice, Results place) {
        nointernet.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        iPressLikeButton(place);
        iPressChoiceButton(myChoice, place);
        iUnpresschoiceButton(myChoice, place);
        iUnpressLikeButton(place);
    }

    private void disableButtons(RelativeLayout myChoice) {
        like.setEnabled(false);
        like.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
        tel.setEnabled(false);
        tel.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
        internet.setEnabled(false);
        internet.setCardBackgroundColor(getResources().getColor(R.color.quantum_grey));
        myChoice.setEnabled(false);
        image.setVisibility(View.INVISIBLE);
        nointernet.setVisibility(View.VISIBLE);
    }

    private void iUnpressLikeButton(Results place) {
        unlikebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myLikes.remove(place.getPlaceId());
                Me.setMyLikes(myLikes);
                servicePlace.unLike(place);
                unlikebutton.setVisibility(View.GONE);
                like.setVisibility(View.VISIBLE);

                Snackbar.make(coordinatorLayout, getString(R.string.dontlike), Snackbar.LENGTH_LONG).show();
//                    etoiles.setRating(Integer.parseInt(starData.trim()) - 1);
            }
        });
    }

    private void iUnpresschoiceButton(RelativeLayout myChoice, Results place) {
        put_me_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collegueList.remove(new Collegue(Me.getMyName(), getString(R.string.mychoice), Me.getMyPhoto()));
                Me.setMy_choice(" ");
                Me.setId_mychoice(" ");
                serviceCollegue.dontaddMyChoice();
                collegueList = servicePlace.CompareCollegueNPlace(place, ActivityDetails.this);
                serviceCollegue.twentyFourHourLast(ActivityDetails.this, true);
                servicePlace.unsaveMyPlace(place);
                serviceCollegue.whenNotifyme(ActivityDetails.this, false, place.getName());
                put_me_Out.setVisibility(View.GONE);
                myChoice.setVisibility(View.VISIBLE);
                Snackbar.make(coordinatorLayout, getString(R.string.retired), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void iPressChoiceButton(RelativeLayout myChoice, Results place) {
        myChoice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                noplace.setVisibility(View.GONE);
                collegueList.add(new Collegue(Me.getMyName(), getString(R.string.mychoice), Me.getMyPhoto()));
                Other.decrement(Me.getMy_choice());
                Me.setMy_choice(place.getName());
                if (place.getPhotos() != null) {
                    serviceCollegue.addMyChoiceToLists(Me.getMyId(), place.getName(), place.getVicinity(), place.getPlaceId(), place.getRating().toString(), place.getPhotos().get(0).getPhotoReference());
                } else {
                    serviceCollegue.addMyChoiceToLists(Me.getMyId(), place.getName(), place.getVicinity(), place.getPlaceId(), place.getRating().toString(), " ada");

                }
                collegueList = servicePlace.CompareCollegueNPlace(place, ActivityDetails.this);
                serviceCollegue.twentyFourHourLast(ActivityDetails.this, true);
                servicePlace.saveMyPlace(place);
                serviceCollegue.whenNotifyme(ActivityDetails.this, true, place.getName());
                myChoice.setVisibility(View.GONE);
                put_me_Out.setVisibility(View.VISIBLE);
                Snackbar.make(coordinatorLayout, getString(R.string.newPlace), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void iPressLikeButton(Results place) {
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLikes = new ArrayList<>();
                myLikes.add(place.getPlaceId());
                Me.setMyLikes(myLikes);
                servicePlace.iLike(place);
                servicePlace.addMyChoice(place.getPlaceId(), false, true);
                like.setVisibility(View.GONE);
                unlikebutton.setVisibility(View.VISIBLE);
                Snackbar.make(coordinatorLayout, getString(R.string.youlike), Snackbar.LENGTH_LONG).show();
//                    etoiles.setRating(Integer.parseInt(starData.trim()) + 1);
            }
        });
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
