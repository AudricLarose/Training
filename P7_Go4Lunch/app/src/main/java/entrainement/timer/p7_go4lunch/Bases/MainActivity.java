package entrainement.timer.p7_go4lunch.Bases;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.model.Inter;
import entrainement.timer.p7_go4lunch.model.PlaceApi;
import entrainement.timer.p7_go4lunch.utils.Other;
import entrainement.timer.p7_go4lunch.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;




public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private Button button;
    private ExtendedServiceCollegue service;
    private ExtendedServicePlace servicePlace=DI.getServicePlace();
//    private CardView cardF;
    private CardView cardG;
    private static final String TAG = "MainActivity";
    private CallbackManager mCallbackManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        button = findViewById(R.id.button);
//        cardF=findViewById(R.id.cardF);
        cardG=findViewById(R.id.cardG);
        initializeFBLogin();

        //Facebook Login
//        cardF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createSignInIntent(1);
//                Other.internetVerify(MainActivity.this);
//                Other.GPSOnVerify(MainActivity.this);
//            }
//        });
        //Gmail Login
        cardG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignInIntent(0);
                Other.internetVerify(MainActivity.this);
                Other.GPSOnVerify(MainActivity.this);
            }
        });

        //Verify if GPS and Internet is available
        Other.GPSOnVerify(MainActivity.this);
        Other.internetVerify(MainActivity.this);
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

// Initialize Facebook Login button
    private void initializeFBLogin(){

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.cardF);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }
    // ...

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
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String collegue = user.getDisplayName();
                            String photo= user.getPhotoUrl().toString();
                            String id= user.getUid();
                            String mail= "no Mails";
                            service.newCollegue(MainActivity.this,id,collegue,photo, mail);
                            Intent intent= new Intent(MainActivity.this, ActivityAfterCheck.class);
                            startActivity(intent);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }





}
