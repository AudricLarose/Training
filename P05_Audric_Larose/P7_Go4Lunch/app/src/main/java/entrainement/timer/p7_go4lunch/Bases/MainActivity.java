package entrainement.timer.p7_go4lunch.Bases;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.utils.Other;
import entrainement.timer.p7_go4lunch.R;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private Button button;
    private ExtendedServiceCollegue service;
    private CardView cardF;
    private CardView cardG;
    private Other other=new Other();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        button = findViewById(R.id.button);
        cardF=findViewById(R.id.cardF);
        cardG=findViewById(R.id.cardG);
        cardF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignInIntent(1);
                other.internetVerify(MainActivity.this);
            }
        });
        cardG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignInIntent(0);
                other.internetVerify(MainActivity.this);

            }
        });
        other.internetVerify(MainActivity.this);
    }


    public void createSignInIntent(int i) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(providers.get(i)))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        service= DI.getService();

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String collegue = user.getDisplayName();
                String photo= user.getPhotoUrl().toString();
                String id= user.getUid();
                String mail= user.getEmail().toString();
                service.newCollegue(MainActivity.this,id,collegue,photo, mail);
                Intent intent= new Intent(MainActivity.this, ActivityAfterCheck.class);
                startActivity(intent);
        }
    }

}
