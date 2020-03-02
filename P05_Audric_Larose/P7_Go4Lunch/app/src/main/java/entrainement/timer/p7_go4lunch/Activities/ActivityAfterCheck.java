package entrainement.timer.p7_go4lunch.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import entrainement.timer.p7_go4lunch.Collegue.FragmentContact;
import entrainement.timer.p7_go4lunch.Me;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.Restaurant.FragmentResto;
import entrainement.timer.p7_go4lunch.Restaurant.Fragmentcarte;

public class ActivityAfterCheck extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Fragment[] fragments={new Fragmentcarte(),new FragmentContact(), new FragmentResto()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_check);
        Me me = new Me();
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_nav);
        NavigationView navigationView= findViewById(R.id.nav_view);
        View headerview= navigationView.getHeaderView(0);
        TextView nomSide= headerview.findViewById(R.id.nomSide);
        TextView mailSide= headerview.findViewById(R.id.mailSide);
        ImageView photoSide= headerview.findViewById(R.id.photoidenti);
        Picasso.get().load(me.getMaPhoto()).into(photoSide);
        nomSide.setText(me.getMonNOm());
        mailSide.setText(me.getMonMail());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_param:
                                Toast.makeText(ActivityAfterCheck.this, "ok", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.nav_deco:
                                AuthUI.getInstance()
                                        .signOut(ActivityAfterCheck.this)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(@NonNull Task<Void> task) {
                           Toast.makeText(ActivityAfterCheck.this, "Deconnection", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectdFragment=null;
                    switch (menuItem.getItemId()){
                        case R.id.blue:
                            selectdFragment= fragments[0];
                            break;
                        case R.id.violet:
                            selectdFragment= fragments[1];
                            break;
                        case R.id.orange:
                            selectdFragment= fragments[2];
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectdFragment).commit();
                    return true;
                }
            };
}
