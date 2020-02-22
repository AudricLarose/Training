package entrainement.timer.p7_go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private static final LatLng Loc1= new LatLng(19.6 ,-25.8);
    private static final LatLng Loc2= new LatLng(-10.9 ,-89.4);
    private static final LatLng Loc3= new LatLng(-17.0  ,-126.3);
    private static final LatLng Loc4= new LatLng(6.3 ,-45.0);
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker Loca1;
    private Marker Loca2;
    private Marker Loca3;
    private Marker Loca4;
    private RequestQueue queue;
    private static final int RC_SIGN_IN = 123;
    private Button button;
    private ExtendedServiceCollegue service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        createSignInIntent();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void createSignInIntent( ) {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        service=DI.getService();

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String collegue = user.getDisplayName();
                String photo= user.getPhotoUrl().toString();
                String id= user.getUid().toString();
                service.addCollegue(MainActivity.this,id,collegue,photo);
                Intent intent= new Intent(MainActivity.this, ActivityAfterCheck.class);
                startActivity(intent);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        button.setVisibility(View.GONE);
                    }
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }

    public void themeAndLogo() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.com_facebook_button_login_logo)      // Set logo drawable
                        .setTheme(R.style.AppTheme)      // Set theme
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_theme_logo]
    }

    public void privacyAndTerms() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();
        // [START auth_fui_pp_tos]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_pp_tos]
    }//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_nav);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                    Fragment selectdFragment=null;
//                    switch (menuItem.getItemId()){
//                        case R.id.blue:
//                            selectdFragment= new Fragmentcarte();
//                            break;
//                        case R.id.violet:
//                            selectdFragment= new FragmentContact();
//                            break;
//                        case R.id.orange:
//                            selectdFragment= new FragmentResto();
//                            break;
//                    }
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectdFragment).commit();
//                    return true;
//                }
//            };

}
