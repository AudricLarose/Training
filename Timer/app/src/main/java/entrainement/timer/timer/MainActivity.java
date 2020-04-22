package entrainement.timer.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
private EditText choix;
private Button Applic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
        choix = findViewById(R.id.choix);
        Applic = findViewById(R.id.check);
        Applic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valeur = choix.getText().toString();
                int Value = Integer.parseInt(valeur);
                Intent intent = new Intent(MainActivity.this, timer_set.class);
                intent.putExtra("valeur", Value);
                startActivity(intent);
            }
        });

    }
}
