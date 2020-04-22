package entrainement.timer.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.Context.VIBRATOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class AlertReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        final long[] pattern = {4000, 6000};
        final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(pattern,-1);
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel= new NotificationChannel("chanel1","notif",NotificationManager.IMPORTANCE_DEFAULT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"chanel1");
        builder.setContentTitle("Reveil").setContentText("Wake Up ! ").setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1,builder.build());
        Toast.makeText(context,"voila ! " , Toast.LENGTH_LONG).show();
    }
}
