package entrainement.timer.workmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class Relance {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Context context;

    public Relance(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void relancetoi(int x) {
        Toast.makeText(context, "okok", Toast.LENGTH_LONG).show();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarmreceveur.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
 Calendar calendar = Calendar.getInstance();
////                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, 1);
//                calendar.set(Calendar.MINUTE, 12);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+x*60*1000,pendingIntent);

//                JobScheduler jobScheduler = (JobScheduler)getApplicationContext()
//                        .getSystemService(JOB_SCHEDULER_SERVICE);
//                ComponentName componentName = new ComponentName(MainActivity.this,
//                        jobyjob.class);
//                JobInfo jobInfoObj = new JobInfo.Builder(1, componentName)
//                        .setPeriodic(50000).build();
//                jobScheduler.schedule(jobInfoObj);
    }
}
