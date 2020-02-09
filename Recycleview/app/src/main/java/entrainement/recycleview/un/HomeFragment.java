package entrainement.recycleview.un;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment {
    private ArrayList<ExempleItem> mNeighbours= new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ExempleItem exempleItem;
    private Button Bouton_important;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static final String TAG = "ImportantFragment";
    private ViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initList();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                areYousure(viewHolder);
            }
        }).attachToRecyclerView(mRecyclerView);
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void areYousure(RecyclerView.ViewHolder viewHolder) {
        AlertDialog alertDialog= surtcheck(viewHolder);
        alertDialog.show();
    }

    private AlertDialog surtcheck(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        View view= getLayoutInflater().from(getContext()).inflate(R.layout.sure, null);
        builder.setView(view)
                .setTitle("Effacer ?")
                .setPositiveButton("Effacer !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExempleItem task = new ExempleAdapter(mNeighbours).getPositionofTask(viewHolder.getAdapterPosition());
//                        viewModel.deleteData(task);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("notebook").document(task.getNom()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "onComplete: ");
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }
    private void initList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notebook")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private List<String> listeArray=new ArrayList<>();

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            viewModel.deleteAllData();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getId();
                                String text = document.getString("titre");
                                listeArray.add(text);
                                mNeighbours.add(new ExempleItem(text,"0"));
                                mRecyclerView.setAdapter(new ExempleAdapter(mNeighbours));

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            androidx.core.app.RemoteInput remoteInput= new RemoteInput.Builder("key_text_reply")
                                    .setLabel("Ajouter").build();
                            Intent Outintent = new Intent(getContext(), ReceveurMessage.class);
                            PendingIntent replypendingintent= PendingIntent.getBroadcast(getContext(),0,Outintent,0);
                            NotificationCompat.Action action =new NotificationCompat.Action.Builder(R.drawable.ic_launcher_background, "Ajouter une tache",replypendingintent)
                                    .addRemoteInput(remoteInput).build();
                            Intent intent= new Intent (getContext(),MainActivity.class);
                            PendingIntent pendingIntent=PendingIntent.getActivity(getContext(),0,intent,0);
                            NotificationManager notificationManager= (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
                            NotificationChannel notificationChannel= new NotificationChannel("channel1", " Reminder",NotificationManager.IMPORTANCE_LOW);
                            NotificationCompat.Builder builder=new NotificationCompat.Builder(getContext(),"channel1");
                            notificationManager.createNotificationChannel(notificationChannel);
                            builder.setContentTitle("To-Do")
                                    .setContentText("Acceder a la liste")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(listederoule(0,listeArray)+ " | "+ listederoule(1,listeArray)+ " | "+listederoule(2,listeArray)+ " | "+listederoule(3,listeArray)+ " | "+ listederoule(4,listeArray)+ " | "+listederoule(5,listeArray)))
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(false)
                                    .setOngoing(true)
                                    .addAction(action)
                                    .setSmallIcon(R.mipmap.ic_launcher);
                            notificationManager.notify(1,builder.build());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public  String listederoule (int x,List<String> liste){
        if (x<=liste.size()-1){
            return   liste.get(x);
        } else {
            return " ";
        }
    }
}
