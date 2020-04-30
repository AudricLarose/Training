package entrainement.timer.p7_go4lunch.utils.collegue;

import android.app.SearchManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import entrainement.timer.p7_go4lunch.utils.ViewModelApi;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.model.Collegue;
import entrainement.timer.p7_go4lunch.utils.Other;

public class FragmentContact extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ExtendedServiceCollegue service=DI.getService();
    private List<Collegue> liste2collegue = service.generateListCollegue();
    private ViewModelApi viewModelApi;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private Adaptateur adapter=new Adaptateur(liste2collegue);
    private static final String TAG = "FragmentContact";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        viewModelApi = new ViewModelProvider(requireActivity()).get(ViewModelApi.class);
        View view = inflater.inflate(R.layout.fragment_fragment_contact, container, false);
        recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Other.checkrealitimeCollegue(new Other.AdapterCollegueCB() {
            @Override
            public void onFinish(List<Collegue> listresultcollegue) {
                Adaptateur adaptateur= new Adaptateur(listresultcollegue);
                recyclerView.setAdapter(adaptateur);
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItem sortedenu= menu.findItem(R.id.sortedmenu);
        if (sortedenu != null) {
            SearchView searchView2 = (SearchView) sortedenu.getActionView();
            searchView2.setVisibility(View.INVISIBLE);
        }

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Toast.makeText(getContext(), newText, Toast.LENGTH_SHORT).show();
                    String userInput = newText.toLowerCase();
                    List<Collegue> newList = new ArrayList<>();
                    for (Collegue name :liste2collegue)
                    {
                        if (name.getName().toLowerCase().contains(userInput)){
                            Toast.makeText(getContext(), name.getName(), Toast.LENGTH_SHORT).show();
                            newList.add(name);
                        }
                    }

                    adapter.updateList(newList);
                    recyclerView.setAdapter(adapter);

                    return true;
                }

            });
        }
    }
}
