package entrainement.timer.workmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Random;

public class Relance {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Context context;

    public Relance(Context context) {
        this.context = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void relancetoi(int hour,int minute) {
        Vibrator vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            final  long [] pattern = {2000,1000};
            vibrator.vibrate(pattern,-1);
        Calendar calendar = Calendar.getInstance();
        final int random = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80]

        calendar.setTimeInMillis(System.currentTimeMillis());
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context ,Alarmreceveur.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                random, intent, 0);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }
}
