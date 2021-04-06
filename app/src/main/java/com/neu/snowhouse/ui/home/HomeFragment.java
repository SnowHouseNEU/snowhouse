package com.neu.snowhouse.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.neu.snowhouse.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    RecyclerView recyclerView;
    ArrayList<MountainData> dataHolder;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.home_mountain_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataHolder = new ArrayList<>();

        MountainData crystal = new MountainData(R.drawable.crystal_mountain, "Crystal Mountain", "Crystal information");
        MountainData stevens = new MountainData(R.drawable.stevens_pass, "Stevens Pass", "Stevens Pass information");
        MountainData snoqualmie = new MountainData(R.drawable.summit_at_snoqualmie, "Summit at Snoqualmie", "Snoqualmie informaiton");

        dataHolder.add(crystal);
        dataHolder.add(stevens);
        dataHolder.add(snoqualmie);

        recyclerView.setAdapter(new HomeAdapter(dataHolder));
        return view;
    }
}