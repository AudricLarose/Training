package entrainement.timer.workmanagertest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.Context.VIBRATOR_SERVICE;

public class Alarmreceveur extends BroadcastReceiver {
    private Relance relance;
    private int compteur=0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        Toast.makeText(context, "voila ! ", Toast.LENGTH_LONG).show();
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel= new NotificationChannel("notification1","Rappel", NotificationManager.IMPORTANCE_DEFAULT );
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"notification1")
                .setContentTitle("voici mon texte")
                .setContentText("N'en mets aps , mec ")
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1,builder.build());
        relance=new Relance(context);

        new CountDownTimer(180000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                final long[] pattern1 = {750,50};
                vibrator.vibrate(pattern1,1);
//        vibrator.vibrate(pattern, -1);
            }

            @Override
            public void onFinish() {
                vibrator.cancel();
                compteur=compteur+1;
                if (compteur!=3) {
                    relance.relancetoi(18000);
                }




            }
        }.start();

    }
}
