package entrainement.timer.workmanagertest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
private AlarmManager alarmManager;
private PendingIntent pendingIntent;
    private Button bouton;
    private Button bouton2;
    private int increment =1;
    private Relance relance;
    private TextView compteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window MyWindow = getWindow();
        WindowManager.LayoutParams winParams = MyWindow.getAttributes();
        winParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
        MyWindow.setAttributes(winParams);
        relance= new Relance(MainActivity.this);
        relance.relancetoi(7,30);
        relance.relancetoi(9,30);
        relance.relancetoi(18,40);
        relance.relancetoi(22,40);
    }

}
