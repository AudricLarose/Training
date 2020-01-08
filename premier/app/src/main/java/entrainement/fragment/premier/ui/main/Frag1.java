package entrainement.fragment.premier.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import entrainement.fragment.premier.Model;
import entrainement.fragment.premier.R;
import entrainement.recycleview.un.Adaptateur;

public class Frag1 extends Fragment {
    private List<Model> mNeighbours;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleliste, parent, false);  // on crée un Recycleview separé du MainActivity
        Context context = view.getContext(); // on invoque le contexte
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        ArrayList<Model> Models= new ArrayList<>();
        Models.add(new Model("Audric", "larose " ,"trop bien"));
        Models.add(new Model("mickaem", "larose " ,"trop bien"));
        Models.add(new Model("Audric", "larose " ,"trop bien"));
        adapter= new Adaptateur(Models);
        mRecyclerView.setAdapter(adapter);
            return view;
    }
}
