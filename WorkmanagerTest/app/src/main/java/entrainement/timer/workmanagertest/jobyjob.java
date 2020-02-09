package entrainement.timer.workmanagertest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class jobyjob extends JobService{

     @RequiresApi(api = Build.VERSION_CODES.O)
     @Override
        public boolean onStartJob(JobParameters jobParameters) {

         final Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(pattern, -1);
//
         Toast.makeText(getApplicationContext(), "voila ! ", Toast.LENGTH_LONG).show();
         NotificationManager notificationManager= (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
         NotificationChannel notificationChannel= new NotificationChannel("notification1","Rappel", NotificationManager.IMPORTANCE_DEFAULT );
         notificationManager.createNotificationChannel(notificationChannel);
         NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),"notifiaciton1")
                 .setContentTitle("voici mon texte")
                 .setContentText("N'en mets aps , mec ")
                 .setSmallIcon(R.mipmap.ic_launcher);
         notificationManager.notify(1,builder.build());
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

             }
         }.start();

            return false;
        }

        @Override
        public boolean onStopJob(JobParameters jobParameters) {
            return false;
        }
}
