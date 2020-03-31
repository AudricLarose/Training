package entrainement.timer.p7_go4lunch.Bases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import entrainement.timer.p7_go4lunch.api.ViewModelApi;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.model.Place;
import entrainement.timer.p7_go4lunch.utils.collegue.FragmentContact;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.model.Me;
import entrainement.timer.p7_go4lunch.utils.Other;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.utils.restaurant.FragmentResto;
import entrainement.timer.p7_go4lunch.utils.restaurant.Fragmentcarte;

public class ActivityAfterCheck extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Fragment[] fragments={new Fragmentcarte(),new FragmentContact(), new FragmentResto()};
    private Switch switchmode;
    private SearchView searchView;
    private ExtendedServiceCollegue serviceCollegue= DI.getService();
    private ExtendedServicePlace servicePlace= DI.getServicePlace();
    private ViewModelApi viewModelApi;
    private Other other= new Other();
    private MenuItem sortedmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_check);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        TextView nomSide = headerview.findViewById(R.id.nomSide);
        TextView mailSide = headerview.findViewById(R.id.mailSide);
        ImageView photoSide = headerview.findViewById(R.id.photoidenti);
        Picasso.get().load(Me.getMaPhoto()).into(photoSide);
        nomSide.setText(Me.getMonNOm());
        mailSide.setText(Me.getMonMail());
        servicePlace.SortPlaceDB();

        viewModelApi = new ViewModelProvider(this).get(ViewModelApi.class);
        other.internetVerify(ActivityAfterCheck.this);
        ViewPager pagerAdapter= (ViewPager) findViewById(R.id.pager123);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.yourlunch:
                                String id_rest=Me.getId_monchoix();
                                Intent intent= new Intent(ActivityAfterCheck.this,ActivityDetails.class);
                                Bundle extra= new Bundle();
                                String name_lunch=Me.getMon_choix();

                                if (name_lunch!=null && !name_lunch.trim().isEmpty()) {
                                    Other.theGoodPlace(Me.getId_monchoix(), new Other.ThegoodPlace() {
                                        @Override
                                        public void GoodPlace(Place place) {
                                            extra.putSerializable("Place",place);
                                        }
                                    });
                                    intent.putExtras(extra);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ActivityAfterCheck.this,R.string.nordv, Toast.LENGTH_LONG).show();
                                }
                                break;
                            case R.id.nav_param:
                                parametreNotification();
                                break;
                            case R.id.nav_deco:
                                AuthUI.getInstance()
                                        .signOut(ActivityAfterCheck.this)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(@NonNull Task<Void> task) {
                           Toast.makeText(ActivityAfterCheck.this, R.string.deconnection, Toast.LENGTH_LONG).show();
                           finish();
                                            }
                                        });
                            }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    };
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragments[0]).commit();
    }

    private void parametreNotification() {
        AlertDialog alertDialog= NotificactionAction();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        iPressed();
    }

    private void iPressed(){
        AlertDialog alertDialog=backpressed();
        alertDialog.show();
    }
    private AlertDialog backpressed(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view= getLayoutInflater().from(this).inflate(R.layout.backpressed,null);
        builder.setView(view).setTitle(getString(R.string.areyousur)).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ActivityAfterCheck.this, R.string.youstay, Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
    private AlertDialog NotificactionAction(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View view= getLayoutInflater().from(this).inflate(R.layout.choosenotif,null);
        switchmode= view.findViewById(R.id.switch1);
        Boolean beNotified = Me.getBeNotified();
        switchmode.setChecked(beNotified);
        alertDialog.setView(view);
        alertDialog.setTitle(getString( R.string.benotified)).setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(switchmode.isChecked()){
                    Me.setBeNotified(true);
                    Toast.makeText(ActivityAfterCheck.this, R.string.notified, Toast.LENGTH_SHORT).show();
                    serviceCollegue.updateNotify();
                } else {
                    Me.setBeNotified(false);
                    Toast.makeText(ActivityAfterCheck.this, R.string.nonotif, Toast.LENGTH_SHORT).show();
                    serviceCollegue.whenNotifyme(getApplicationContext(),false,"");
                    serviceCollegue.updateNotify();
                }
            }
        });
        return alertDialog.create();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem= menu.findItem(R.id.search);
         sortedmenu= menu.findItem(R.id.sortedmenu);
        searchView= (SearchView) menuItem.getActionView();
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectdFragment=null;
                    switch (menuItem.getItemId()){
                        case R.id.blue:

                            selectdFragment= fragments[0];
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    Fragmentcarte fragmentcarte = new Fragmentcarte();
                                    fragmentcarte.filterSearch(ActivityAfterCheck.this,query);
//                Toast.makeText(ActivityAfterCheck.this,"ok",Toast.LENGTH_SHORT).show();
                                    return true;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    return false;
                                }
                            });
                            break;
                        case R.id.violet:
                            selectdFragment= fragments[1];
                            break;
                        case R.id.orange:

                            selectdFragment= fragments[2];
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    Fragmentcarte fragmentcarte = new Fragmentcarte();
                                    fragmentcarte.filterSearch(ActivityAfterCheck.this,query);
                                    return true;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {

                                    return false;
                                }
                            });
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectdFragment).commit();
                    return true;
                }
            };
}
