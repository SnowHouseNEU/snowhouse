package com.neu.snowhouse.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.neu.snowhouse.R;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.response.LiteMountainResponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    MountainAdapter mountainAdapter;
    ArrayList<LiteMountainResponseModel> mountains = new ArrayList<>();
    API api = RetrofitClient.getInstance().getAPI();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.home_mountain_recycler_view);
        mountainAdapter = new MountainAdapter(mountains);
        getMountains();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mountainAdapter);
    }

    private void getMountains() {
        Call<List<LiteMountainResponseModel>> fetchMountains = api.getAllMountains();
        fetchMountains.enqueue(new Callback<List<LiteMountainResponseModel>>() {
            @Override
            public void onResponse(Call<List<LiteMountainResponseModel>> call, Response<List<LiteMountainResponseModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mountains = (ArrayList<LiteMountainResponseModel>) response.body();
                    mountainAdapter.updateAdapter(mountains);
                }
                if (!response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LiteMountainResponseModel>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}