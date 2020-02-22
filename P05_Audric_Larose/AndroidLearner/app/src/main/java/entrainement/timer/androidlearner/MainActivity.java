package entrainement.timer.androidlearner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private static final String TAG = "MainActivity";
    private Vibrator vibrator;
    private static final int REQ_CODE8SPEECH_OUTPUT=123;
    private ArrayList<String> voice= new ArrayList<>();
    private GoogleMap mMap;
    private static final LatLng Loc1= new LatLng(19.6 ,-25.8);
    private static final LatLng Loc2= new LatLng(-10.9 ,-89.4);
    private static final LatLng Loc3= new LatLng(-17.0  ,-126.3);
    private static final LatLng Loc4= new LatLng(6.3 ,-45.0);
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Marker Loca1;
    private Marker Loca2;
    private Marker Loca3;
    private Marker Loca4;
    private RequestQueue queue;
    private Marker meU;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void areYousure() {
        AlertDialog alertDialog= surtcheck();
        alertDialog.show();

    }
    private AlertDialog surtcheck(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view= getLayoutInflater().from(this).inflate(R.layout.sure, null);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        builder.setView(view)
                .setTitle("Effacer ?")
                .setPositiveButton("Effacer !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }

    private void fires(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notebook")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> listeArray=new ArrayList<>();
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String text = document.getString("titre");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        Map<String, String> note= new HashMap<>();
        note.put("titre","tcheck");
        db.collection("important").document(nom)
                .set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(MainActivity.this,"Envoyé !",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Pas de Reseau",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notification (){
        androidx.core.app.RemoteInput remoteInput= new RemoteInput.Builder("key_text_reply")
                .setLabel("Ajouter").build();
        Intent Outintent = new Intent(MainActivity.this, ReceveurMessage.class);
        PendingIntent replypendingintent= PendingIntent.getBroadcast(MainActivity.this,0,Outintent,0);
        NotificationCompat.Action action =new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background, "Ajouter une tache",replypendingintent)
                .addRemoteInput(remoteInput).build();
        Intent intent= new Intent (MainActivity.this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,intent,0);
        NotificationManager notificationManager= (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel= new NotificationChannel("channel1", " Reminder",NotificationManager.IMPORTANCE_LOW);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(MainActivity.this,"chanel1");
        notificationManager.createNotificationChannel(notificationChannel);
        builder.setContentTitle("To-Do")
                .setContentText("Acceder a la liste")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("hey"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .addAction(action)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1,builder.build());
    }

    public void lancetoi() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                final long [] pattern = {3000,2000};
                vibrator.vibrate(pattern,-1);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
    private void btnOpenMic(){
       Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
       intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
       intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
       intent.putExtra(RecognizerIntent.EXTRA_PROMPT, " a toi ");
       startActivityForResult(intent,REQ_CODE8SPEECH_OUTPUT);

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
    public void timer(){
AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
Calendar calendar= Calendar.getInstance();
calendar.setTimeInMillis(System.currentTimeMillis());
calendar.set(Calendar.HOUR_OF_DAY,12);
calendar.set(Calendar.MINUTE,12);
calendar.set(Calendar.SECOND,0);
alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void map(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        queue= Volley.newRequestQueue(this);
        getEarthQuakes();

        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onStatusChanged: " + location.toString() );
                LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(me).title("C'est Créteil !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me,14));
                Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    String fulladress= " ";

                    if (addressList!=null && addressList.size()>0){
                        if (addressList.get(0).getSubLocality()!=null){
                            fulladress+=addressList.get(0).getSubLocality();
                        }
                        Toast.makeText(getApplicationContext(),addressList.get(0).getAddressLine(0),Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onLocationChanged: "+ addressList.get(0).toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
