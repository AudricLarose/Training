package entrainement.timer.p7_go4lunch.Collegue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.R;

public class FragmentContact extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Collegue> liste2collegue= new ArrayList<>();
    private ExtendedServiceCollegue service;
    private ViewModelCollegue viewModelCollegue;
    private static final String TAG = "FragmentContact";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModelCollegue= new ViewModelProvider(requireActivity()).get(ViewModelCollegue.class);
        service= DI.getService();
        View view=inflater.inflate(R.layout.fragment_fragment_contact, container, false);
        recyclerView= (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        adapter= new Adaptateur(viewModelCollegue.getUser(), this);
        layoutManager= new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        viewModelCollegue.getUser().observe(getViewLifecycleOwner(), new Observer<List<Collegue>>() {
//            @Override
//            public void onChanged(List<Collegue> collegues) {
//                Log.d(TAG, "onChanged: "+ collegues);
//                adapter= new Adaptateur(collegues);
//                layoutManager= new LinearLayoutManager(view.getContext());
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }
//        });

        return view;
    }
}
