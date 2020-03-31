package entrainement.timer.p7_go4lunch.utils.restaurant;

import android.app.SearchManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import entrainement.timer.p7_go4lunch.model.Place;
import entrainement.timer.p7_go4lunch.runTest.FirebaseCollectionGrabber;
import entrainement.timer.p7_go4lunch.utils.Other;
import entrainement.timer.p7_go4lunch.utils.collegue.Adaptateur;

public class FragmentResto extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ExtendedServicePlace servicePlace = DI.getServicePlace();
    private ExtendedServiceCollegue serviceCollegue = DI.getService();
    private List<Place> liste2Place = servicePlace.generateListPlace();
    private SearchView searchView = null;
    private AdaptateurPlace adapter=new AdaptateurPlace(liste2Place);;
    private static final String TAG = "FragmentContact";
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        liste2Place = servicePlace.generateListPlace();
        View view = inflater.inflate(R.layout.fragment_fragment_resto, container, false);
        recyclerView =  view.findViewById(R.id.recycleContact);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        coordinatorLayout= view.findViewById(R.id.coordinate);
        swipeRefreshLayout= view.findViewById(R.id.swipedem);
        recyclerView.setLayoutManager(layoutManager);
        serviceCollegue.getListCollegue();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(view.getContext(),R.string.listtoday, Toast.LENGTH_SHORT).show();
            }
        });

//        adapter= new Adaptateur(viewModelApi.getUser(), this);
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
                    List<Place> newList = new ArrayList<>();
                    for (Place name :liste2Place)
                    {
                        if (name.getnomPlace().toLowerCase().contains(userInput)){
                            Toast.makeText(getContext(), name.getnomPlace(), Toast.LENGTH_SHORT).show();
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
            case R.id.fav:
                Toast.makeText(getContext(), "dsds", Toast.LENGTH_SHORT).show();
                FirebaseCollectionGrabber.getCollection("restaurant", Place.class, new FirebaseCollectionGrabber.OnFinish<Place>() {
                    @Override
                    public void success(List<Place> objects) {
                        AdaptateurPlace adaptateurPlace= new AdaptateurPlace(objects);
                        recyclerView.setAdapter(adaptateurPlace);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                },"quivient");
                return true;
            case R.id.people:
                FirebaseCollectionGrabber.getCollection("restaurant", Place.class, new FirebaseCollectionGrabber.OnFinish<Place>() {
                    @Override
                    public void success(List<Place> objects) {
                        AdaptateurPlace adaptateurPlace= new AdaptateurPlace(objects);
                        recyclerView.setAdapter(adaptateurPlace);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                },"note");
                return true;
            case R.id.proxy:
                FirebaseCollectionGrabber.getCollection("restaurant", Place.class, new FirebaseCollectionGrabber.OnFinish<Place>() {
                    @Override
                    public void success(List<Place> objects) {
                        AdaptateurPlace adaptateurPlace= new AdaptateurPlace(objects);
                        recyclerView.setAdapter(adaptateurPlace);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                },"nomPlace");
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
