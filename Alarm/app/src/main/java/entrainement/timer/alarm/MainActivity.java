package entrainement.timer.alarm;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private TextView alarm;
    private EditText test;
    private String heure ;
    private String heure1 = "18 heure 30";
    private String heure2 = "8 heure 10";
    private String heure3 = "13 heure 3";
    private String heure4 = "14 heure 50";
    private String heure5 = "1 heure 30";
    private String heure6 = "3 heure 30";
    private final int REQ_CODE8SPEECH_OUTPUT= 143;
    private ArrayList<String> voice= new ArrayList<>();
    private String[] listedemots;
    public static  int heures;
    public static int minutes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarm= findViewById(R.id.texte);
        Button button= findViewById(R.id.alarm);
        EditText editText=findViewById(R.id.test);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOpenMic();

            }
        });

    Button buttonCancealarm = findViewById(R.id.cancel);
    buttonCancealarm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelAlrm();
        }
    });
    }

    private void updateTimeTex(Calendar c){
        String timeText= "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        alarm.setText(timeText);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm (Calendar c){
        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);

    }

    private void cancelAlrm (){
        final long[] pattern = {2000, 1000};
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        vibrator.cancel();
        alarmManager.cancel(pendingIntent);
        alarm.setText("Alarm Canceled");
    }
    private void btnOpenMic(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"A toi ! ");

        try {
            startActivityForResult(intent,REQ_CODE8SPEECH_OUTPUT);
        }
        catch (ActivityNotFoundException tim){
            Toast.makeText(this,"Le micro a un probleme",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE8SPEECH_OUTPUT: {
                if (resultCode == RESULT_OK ){
                    voice = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    onTimeSet(voice);

                    Toast.makeText(MainActivity.this,voice.get(0),Toast.LENGTH_LONG).show();

                }
                break;
            }
        }
    }
    public void onTimeSet(ArrayList<String> voice) {
         listedemots =voice.toArray( new String[voice.size()]);
        String [] listedemotss=listedemots[0].split("h");
        int heures= Integer.parseInt(listedemotss[0]);
        int minutes= Integer.parseInt(listedemotss[1]);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, heures);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);
        updateTimeTex(c);
        startAlarm(c);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
