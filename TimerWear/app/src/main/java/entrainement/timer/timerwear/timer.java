package entrainement.timer.timerwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class timer extends Activity {
    private long START_TIME;
    private TextView timer;
    private Button start;
    private Button reset;
    private FloatingActionButton back;
    private Vibrator vibrator;
    public static VibrationEffect createOneShot;
    private CountDownTimer zcountDownTimer;
    private boolean ztimerRunning;

    private long ztimeleftMilis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_timer);
        Bundle extras= getIntent().getExtras();
        if (extras!=null){
            int Myint= extras.getInt("valeur");
            ztimeleftMilis=Myint*60000;
            START_TIME=ztimeleftMilis;
            notifyme();
        }

        timer= findViewById(R.id.text_view_coutndown);
        start= findViewById(R.id.button_start_pause);
        reset= findViewById(R.id.button_reset);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        startTimer();

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
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                vibrator.vibrate(pattern,2);

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
                        final long[] pattern = {200,100};
//        vibrator.vibrate(pattern,1);
        int minutes = (int) (ztimeleftMilis/1000)/60;
        int seconds = (int) (ztimeleftMilis/1000)%60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeLeftFormatted);
    }

    private void notifyme (){
        Intent intent = new Intent(this, NotificationDetails.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT); //getactivity au singulier ( et non getactivities)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("titre");
        builder.setContentText("voici les bailles");
        builder.setSmallIcon(R.drawable.ic_cc_clear);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,notification);
    }
}
