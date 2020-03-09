package entrainement.timer.stretchtime;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

class SendNotif {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notification (Context context){
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("vibrationcheck","true");
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,intent,0);
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel= new NotificationChannel("channel1","StretchTime",NotificationManager.IMPORTANCE_LOW);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"channel1");
        notificationManager.createNotificationChannel(notificationChannel);
        builder.setContentTitle("StretchTime")
                .setContentText("Aller au chronos")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true);
        notificationManager.notify(1,builder.build());
    }
}
