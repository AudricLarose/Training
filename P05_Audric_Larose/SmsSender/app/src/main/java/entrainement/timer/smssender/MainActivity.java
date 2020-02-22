package entrainement.timer.smssender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText zonedetexte;
    private EditText numero;
    private EditText heure;
    private EditText minute;
    private Button bouton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zonedetexte = findViewById(R.id.zoneTexte);
        numero = findViewById(R.id.number);
        bouton = findViewById(R.id.envoyer);
        heure = findViewById(R.id.heure);
        minute = findViewById(R.id.minute);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zonedetexte.getText().equals(" ") || numero.getText().equals(" ") || minute.getText().equals(" ") || heure.getText().equals(" ")) {
                    Toast.makeText(MainActivity.this, "Remplit wesh", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "texto envoyé", Toast.LENGTH_LONG).show();
                    String phonetel = numero.getText().toString();
                    String zoneT = zonedetexte.getText().toString();
                    int min =Integer.parseInt(minute.getText().toString());
                    int hour =Integer.parseInt(heure.getText().toString());
                    Intent intent = new Intent(MainActivity.this, Receiver.class);
                    intent.putExtra("numero", phonetel);
                    intent.putExtra("zt", zoneT);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                    alarmManager.setExact(AlarmManager.RTC,
                            calendar.getTimeInMillis() , pendingIntent);

                }

            }
        });
//    }

            }
}