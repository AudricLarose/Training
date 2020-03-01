package entrainement.timer.p7_go4lunch.Restaurant;

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
import entrainement.timer.p7_go4lunch.Collegue.ViewModelCollegue;
import entrainement.timer.p7_go4lunch.DI;
import entrainement.timer.p7_go4lunch.R;


public class FragmentResto extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Place> liste2Place= new ArrayList<>();
    private ExtendedServicePlace service;
    private ViewModelCollegue viewModelCollegue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModelCollegue= new ViewModelProvider(requireActivity()).get(ViewModelCollegue.class);
        service= DI.getServicePlace();
        View view= inflater.inflate(R.layout.fragment_fragment_resto, container, false);
        recyclerView= (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
//        liste2Place=service.getListOfPlace();
        adapter= new AdaptateurPlace(viewModelCollegue.getPlace(),this);
        layoutManager= new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
