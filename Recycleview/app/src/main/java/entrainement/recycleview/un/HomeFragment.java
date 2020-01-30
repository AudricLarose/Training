package entrainement.recycleview.un;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class HomeFragment extends Fragment {
    private ArrayList<ExempleItem> mNeighbours= new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ExempleItem exempleItem;
    private Button Bouton_important;
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
        androidx.appcompat.app.AlertDialog alertDialog= surtcheck(viewHolder);
        alertDialog.show();

    }
    private androidx.appcompat.app.AlertDialog surtcheck(final RecyclerView.ViewHolder viewHolder){
        androidx.appcompat.app.AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        View view= getLayoutInflater().from(getContext()).inflate(R.layout.sure, null);
        builder.setView(view)
                .setTitle("Effacer ?")
                .setPositiveButton("Effacer !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExempleItem task = new ExempleAdapter(mNeighbours).getPositionofTask(viewHolder.getAdapterPosition());
//                        viewModel.deleteData(task);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("notebook").document(task.toString()).delete();
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
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            viewModel.deleteAllData();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String text = document.getString("titre");
                                mNeighbours.add(new ExempleItem(text,0));
                                mRecyclerView.setAdapter(new ExempleAdapter(mNeighbours));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
