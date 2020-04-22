package entrainement.fragment.premier.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import entrainement.fragment.premier.Model;
import entrainement.fragment.premier.R;

public class Frag3 extends Fragment {
    private List<Model> mNeighbours;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) { View view = inflater.inflate(R.layout.recycleliste, parent, false);  // on crée un Recycleview separé du MainActivity
        ArrayList<Model> Models= new ArrayList<>();
        Models.add(new Model("george", "LeSuperChien" ,"trop bien"));
        Models.add(new Model("Kamel", "Moula " ,"trop bien"));
        Models.add(new Model("fricfric", "larose " ,"trop bien"));
        Context context = view.getContext(); // on invoque le contexte
        mRecyclerView = (RecyclerView) view; // pareil que FindViewById(R.id.Recycleview) on prend la partie Recleview de la vue
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        adapter= new entrainement.recycleview.un.Adaptateur(Models);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
