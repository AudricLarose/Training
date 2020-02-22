package entrainement.timer.p7_go4lunch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentContact extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Collegue> listedecollegue;
    private List<Collegue> liste2collegue= new ArrayList<>();
    private ExtendedServiceCollegue service;
    private ViewModelCollegue viewModelCollegue;
    private static final String TAG = "FragmentContact";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModelCollegue= new ViewModelProvider(requireActivity()).get(ViewModelCollegue.class);

        service=DI.getService();
//        liste2collegue=service.getListCollegue();
//        listedecollegue.add(new Collegue("Audric","Seoul"));
//        listedecollegue.add(new Collegue("Carlos","Buzan"));
//        listedecollegue.add(new Collegue("Yves","Paris"));
//        listedecollegue.add(new Collegue("Jean","Mambani"));
        View view=inflater.inflate(R.layout.fragment_fragment_contact, container, false);
        recyclerView= (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        viewModelCollegue.getUser().observe(getViewLifecycleOwner(), new Observer<List<Collegue>>() {
            @Override
            public void onChanged(List<Collegue> collegues) {
                Log.d(TAG, "onChanged: "+ collegues);
                adapter= new Adaptateur(collegues);
                layoutManager= new LinearLayoutManager(view.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
