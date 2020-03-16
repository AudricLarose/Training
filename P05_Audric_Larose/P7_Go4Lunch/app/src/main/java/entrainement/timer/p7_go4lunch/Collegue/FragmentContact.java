package entrainement.timer.p7_go4lunch.Collegue;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.R;

public class FragmentContact extends Fragment implements SearchView.OnQueryTextListener {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inflated, menu);
        MenuItem menuItem= menu.findItem(R.id.search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<String> newList = new ArrayList<>();
        for (Collegue name :viewModelCollegue.getUser().getValue())
        {
            if (name.getNom().toLowerCase().contains(userInput)){
                newList.add(name.getNom());
            }
        }
        return true;
    }
}
