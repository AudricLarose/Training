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
    private Relance relance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final PeriodicWorkRequest request = new  PeriodicWorkRequest.Builder(Worker_test.class,15,TimeUnit.MINUTES)
//                .setInitialDelay(10,TimeUnit.SECONDS)
//                .build();

//        bouton= findViewById(R.id.button);
//        bouton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WorkManager.getInstance(MainActivity.this).enqueue(request);
//            }
//        });
relance= new Relance(MainActivity.this);
        bouton2= findViewById(R.id.button2);
        bouton2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                relance.relancetoi(1);
//                Toast.makeText(MainActivity.this,"okok",Toast.LENGTH_LONG).show();
//alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
//Intent intent= new Intent(MainActivity.this,Alarmreceveur.class);
//pendingIntent= PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
//// Calendar calendar = Calendar.getInstance();
////                calendar.setTimeInMillis(System.currentTimeMillis());
////                calendar.set(Calendar.HOUR_OF_DAY, 14);
////                calendar.set(Calendar.MINUTE, 14);
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+60*100,pendingIntent);

//                JobScheduler jobScheduler = (JobScheduler)getApplicationContext()
//                        .getSystemService(JOB_SCHEDULER_SERVICE);
//                ComponentName componentName = new ComponentName(MainActivity.this,
//                        jobyjob.class);
//                JobInfo jobInfoObj = new JobInfo.Builder(1, componentName)
//                        .setPeriodic(50000).build();
//                jobScheduler.schedule(jobInfoObj);
            }
        });

//        final TextView textView=findViewById(R.id.Textview);
//        WorkManager.getInstance(MainActivity.this).getWorkInfoByIdLiveData(request.getId())
//                .observe(this, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//                        String status = workInfo.getState().name();
//                        textView.append(status+"\n");
//                    }
//                });
    }
}
