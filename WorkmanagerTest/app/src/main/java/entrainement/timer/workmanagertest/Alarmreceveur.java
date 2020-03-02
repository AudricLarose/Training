package entrainement.timer.workmanagertest;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.VIBRATOR_SERVICE;

public class Alarmreceveur extends BroadcastReceiver {
    private int compteur=0;
    private Relance relance;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel= new NotificationChannel("channel1","Good to Go",NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channel1");
            builder.setContentTitle("Good 2 go ").setContentText("Clear !").setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setOngoing(true);
            manager.notify(0,builder.build());
            compteur = compteur + 1;
            relance = new Relance(context);
            Calendar calendar = Calendar.getInstance();
            Intent intent1 = new Intent(context, Wakeup.class);
            context.startActivity(intent1);

        }
    }
}
