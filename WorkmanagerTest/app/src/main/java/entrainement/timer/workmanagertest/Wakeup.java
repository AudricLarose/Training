package entrainement.timer.workmanagertest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.zip.CheckedOutputStream;

public class Wakeup extends AppCompatActivity {
    private TextView compteur;
    private String countString;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);
        compteur = (TextView) findViewById(R.id.chrono);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        final Vibrator vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final long [] pattern = {1700,2000};
        new CountDownTimer (200*1000,2000){

            @Override
            public void onTick(long millisUntilFinished) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                vibrator.vibrate(pattern,-1);
                count=count+1;
                countString= String.valueOf(count);
                compteur.setText(countString);
            }

            @Override
            public void onFinish() {
            vibrator.cancel();
            finish();
            }
        }.start();
    }

}
