package entrainement.timer.p7_go4lunch.utils.restaurant;

import android.app.SearchManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;
import entrainement.timer.p7_go4lunch.api.restaurant.ExtendedServicePlace;
import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.R;
import entrainement.timer.p7_go4lunch.model.Results;
import entrainement.timer.p7_go4lunch.utils.Other;


public class FragmentResto extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private List<Results> liste2Place = servicePlace.generateListPlaceAPI();
    private SearchView searchView = null;
    private AdaptateurPlace adapter=new AdaptateurPlace(liste2Place);
    private static final String TAG = "FragmentContact";
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardView norestaurant;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        liste2Place = servicePlace.generateListPlaceAPI();
        View view = inflater.inflate(R.layout.fragment_fragment_resto, container, false);
        recyclerView =  view.findViewById(R.id.recycleContact);
        recyclerView.setHasFixedSize(true);
        norestaurant= view.findViewById(R.id.norestaurant);
        layoutManager = new LinearLayoutManager(view.getContext());
        coordinatorLayout= view.findViewById(R.id.coordinate);
        recyclerView.setLayoutManager(layoutManager);
        serviceCollegue.getListCollegue();
        recyclerView.setAdapter(adapter);


        Other.checkrealtime(new Other.Adapterinterf() {
            @Override
            public void onFinish(List<Results> listePlaceApi) {
                if (listePlaceApi.isEmpty())
                {
                    norestaurant.setVisibility(View.VISIBLE);
                } else
                {
                    norestaurant.setVisibility(view.GONE);
                }
                AdaptateurPlace adapter=new AdaptateurPlace(listePlaceApi);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onRequest(List<Results> request) {

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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        menu.setGroupVisible(R.id.sortedmenu,true);
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
                    List<Results> newList = new ArrayList<>();
                    for (Results name :liste2Place)
                    {
                        if (name.getName().toLowerCase().contains(userInput)){
                            Toast.makeText(getContext(), name.getName(), Toast.LENGTH_SHORT).show();
                            newList.add(name);
                        }
                    }
                    adapter.updatelistplace(newList);
                    recyclerView.setAdapter(adapter);
                    return true;
                }

            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.people:
              List<Results>liste3Plac= Other.sortedByWhoCome(liste2Place);
                AdaptateurPlace adaptateurPlace= new AdaptateurPlace(liste3Plac);
                recyclerView.setAdapter(adaptateurPlace);
                return true;
            case R.id.fav:
                List<Results>listeFavPlace= Other.sortedByLike(liste2Place);
                AdaptateurPlace adaptateurPlace1= new AdaptateurPlace(listeFavPlace);
                recyclerView.setAdapter(adaptateurPlace1);
                return true;
            case R.id.proxy:
                List<Results>listeDistancePlace=Other.sortedByDistance(liste2Place);
                AdaptateurPlace adaptateurPlace2= new AdaptateurPlace(listeDistancePlace);
                recyclerView.setAdapter(adaptateurPlace2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
