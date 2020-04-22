package entrainement.timer.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class timer_set extends AppCompatActivity {
    private long START_TIME;
    private TextView timer;
    private Button start;
    private Button reset;
    private FloatingActionButton back;
    private Vibrator vibrator;

    private CountDownTimer zcountDownTimer;
    private boolean ztimerRunning;
    private long ztimeleftMilis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras= getIntent().getExtras();
        if (extras!=null){
            int Myint= extras.getInt("valeur");
            ztimeleftMilis=Myint*60000;
            START_TIME=ztimeleftMilis;
        }
        timer= findViewById(R.id.text_view_coutndown);
        start= findViewById(R.id.button_start_pause);
        reset= findViewById(R.id.button_reset);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        back= findViewById(R.id.back);
        startTimer();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ztimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserTimer();
            }
        });
        updateCountdownText();
    }
    private void startTimer(){
        zcountDownTimer= new CountDownTimer(ztimeleftMilis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ztimeleftMilis=millisUntilFinished;
                updateCountdownText();

            }



            @Override
            public void onFinish() {
                final long[] pattern = {2000,1000};
                vibrator.vibrate(pattern,0);
                ztimerRunning= false;
                start.setText("start");
                start.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
            }
        }.start();
        ztimerRunning= true;
        start.setText("pause");
        reset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer(){
        zcountDownTimer.cancel();
        ztimerRunning= false;
        start.setText("Start");
        reset.setVisibility(View.VISIBLE);

    }

    private void reserTimer(){
        ztimeleftMilis=START_TIME;
        updateCountdownText();
        reset.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);
        vibrator.cancel();

    }
    private void updateCountdownText() {
        int minutes = (int) (ztimeleftMilis/1000)/60;
        int seconds = (int) (ztimeleftMilis/1000)%60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeLeftFormatted);
    }
}
