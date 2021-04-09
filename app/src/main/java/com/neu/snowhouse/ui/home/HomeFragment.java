package com.neu.snowhouse.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.neu.snowhouse.R;
import com.neu.snowhouse.databinding.FragmentHomeBinding;
import com.neu.snowhouse.ui.mountainInfo.MountainInfoFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeAdapter.ItemClickListener {
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
        buildListData();
        initRecylerView(view);

        return view;
    }

    private void initRecylerView(View view) {
        recyclerView = view.findViewById(R.id.home_mountain_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HomeAdapter adapter = new HomeAdapter(dataHolder, this);
        recyclerView.setAdapter(adapter);
    }

    private void buildListData() {
        dataHolder = new ArrayList<>();

        MountainData crystal = new MountainData(R.drawable.crystal_mountain, "Crystal Mountain", "Crystal information");
        MountainData stevens = new MountainData(R.drawable.stevens_pass, "Stevens Pass", "Stevens Pass information");
        MountainData snoqualmie = new MountainData(R.drawable.summit_at_snoqualmie, "Summit at Snoqualmie", "Snoqualmie informaiton");

        dataHolder.add(crystal);
        dataHolder.add(stevens);
        dataHolder.add(snoqualmie);
    }

    @Override
    public void onItemClick(MountainData data) {
        Fragment fragment = MountainInfoFragment.newInstance(data.getTitle());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment, "fragment_mountain_info");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}