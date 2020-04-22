package com.example.testentoutgenre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ExampleDialo.ExampleDialogListener{
    private TextView usernames;
    private TextView passwords ;
    private TextView Ch1;
    private TextView Ch2;
    private Button Bouton;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout);
       // usernames = (TextView) findViewById(R.id.textView_username);
       // passwords = (TextView) findViewById(R.id.textView_password);
       // Ch1 = (TextView) findViewById(R.id.choix1);
       // Bouton = (Button) findViewById(R.id.button);
       // Bouton.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View v) {
           //     openDialog();
           // }
       // });
        notifyme();

    }
    public void openDialog() {
        ExampleDialo exampleDialo= new ExampleDialo();
        exampleDialo.show(getSupportFragmentManager(),"exemple du baye");
    }

    @Override
    public void applyTexts(String username, String mdp, String C1) {
    usernames.setText(username);
    passwords.setText(mdp);
        Log.d(TAG, "applyTexts: " + C1);
    Ch1.setText(C1);
    }
    private void notifyme (){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
               NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_LOW);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo("Info");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }
}
