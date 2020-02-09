//package entrainement.timer.workmanagertest;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.os.Build;
//import android.os.CountDownTimer;
//import android.os.VibrationEffect;
//import android.os.Vibrator;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.constraintlayout.solver.widgets.Snapshot;
//import androidx.core.app.NotificationCompat;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//public class Worker_test extends Worker {
//    private final Vibrator vibrator;
//    public Worker_test(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @NonNull
//    @Override
//    public Result doWork() {
////        Toast.makeText(getApplicationContext(),"Apps is running",Toast.LENGTH_SHORT).show();
//        dsplayNotification("hey je travaille" ,"travaille fini");
//        vibrate();
//        return Result.success();
//    }
//
//
//private void vibrate() {
//        new CountDownTimer(180000,1000){
//    @Override
//    public void onTick(long millisUntilFinished) {
//        final long[] pattern1 = {750,50};
//        vibrator.vibrate(pattern1,1);
////        vibrator.vibrate(pattern, -1);
//    }
//
//    @Override
//    public void onFinish() {
//        vibrator.cancel();
//
//    }
//}.start();
//
//}
//
//
//
//
//
////}
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void  dsplayNotification(String task, String desc){
//        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channel= new NotificationChannel("simplificactioncoding","notif",NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(channel);
//        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(), "simplificactioncoding")
//                .setContentTitle(task)
//                .setContentText(desc)
//                .setSmallIcon(R.mipmap.ic_launcher);
//        manager.notify(1,builder.build());
//
//
//    }
//}
