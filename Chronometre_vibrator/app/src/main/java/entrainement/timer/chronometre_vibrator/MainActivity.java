package entrainement.timer.chronometre_vibrator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int count = 0;
    private TextView compteur;
    private Vibrator vibrator;
    private String countString;
    private boolean isitlance;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        compteur = (TextView) findViewById(R.id.chrono);
        Bundle extra= getIntent().getExtras();
        if (extra!=null) {
            String check = extra.getString("vibrationcheck");
                lancetoi();
        }
        SendNotif sendNotif= new SendNotif();
        sendNotif.notification(MainActivity.this);
        compteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isitlance==false){
                    lancetoi();
                }
            }
        });
    }
    public void lancetoi() {
        new CountDownTimer(200*1000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                isitlance=true;
                final long[] pattern1 = {1700,2000};
                vibrator.vibrate(pattern1,-1);
                count=count+1;
                countString= String.valueOf(count);
                compteur.setText(countString);
            }

            @Override
            public void onFinish() {
                vibrator.cancel();
                isitlance=false;
                compteur.setText(0);
                count=0;
            }
        }.start();
    }
}

