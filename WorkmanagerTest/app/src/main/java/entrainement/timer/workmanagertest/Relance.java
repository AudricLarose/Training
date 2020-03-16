package entrainement.timer.workmanagertest;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Relance {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Context context;

    public Relance(Context context) {
        this.context = context;
    }

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