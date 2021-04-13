package com.neu.snowhouse.ui.mountain;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neu.snowhouse.R;
import com.neu.snowhouse.SessionManagement;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.response.Image;
import com.neu.snowhouse.model.response.MountainResponseModel;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MountainFragment extends Fragment implements OnMapReadyCallback {
    String userName;
    int mountainId;
    API api = RetrofitClient.getInstance().getAPI();
    private final Handler textHandler = new Handler();
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    TextView mountainName;
    TextView mountainDesc;
    TextView mountainTicket;
    TextView mountainWeather;
    TextView mountainHours;
    TextView mountainAddress;
    GoogleMap map;
    MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mountain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = SessionManagement.getUserName(getContext());
        mountainId = getArguments().getInt("mountainId");
        mountainName = view.findViewById(R.id.mountain_name);
        mountainDesc = view.findViewById(R.id.mountain_desc);
        mountainTicket = view.findViewById(R.id.mountain_ticket);
        mountainWeather = view.findViewById(R.id.mountain_weather);
        mountainHours = view.findViewById(R.id.mountain_hours);
        mountainAddress = view.findViewById(R.id.mountain_address);
        sliderView = view.findViewById(R.id.imageSlider);
        sliderAdapter = new SliderAdapter(getContext());
        sliderView.setSliderAdapter(sliderAdapter);
        getMountain();
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(8); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        mapView = view.findViewById(R.id.mapsView);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void getMountain() {
        Call<MountainResponseModel> fetchMountain = api.getMountainById(mountainId, userName);
        List<Image> images = new ArrayList<>();
        fetchMountain.enqueue(new Callback<MountainResponseModel>() {
            @Override
            public void onResponse(Call<MountainResponseModel> call, Response<MountainResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Thread thread = new Thread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            MountainResponseModel mountainResponseModel = response.body();
                            Image image1 = mountainResponseModel.getImage1();
                            Image image2 = mountainResponseModel.getImage2();
                            Image image3 = mountainResponseModel.getImage3();
                            images.add(image1);
                            images.add(image2);
                            images.add(image3);
                            textHandler.post(() -> {
                                mountainName.setText(mountainResponseModel.getMountainName());
                                mountainDesc.setText(mountainResponseModel.getDescription());
                                mountainTicket.setText(mountainResponseModel.getTicketInfo());
                                mountainWeather.setText("Weather: " + mountainResponseModel.getWeather().toString());
                                mountainHours.setText("Open Hours: " + mountainResponseModel.getOpenHour());
                                mountainAddress.setText("Address: " + mountainResponseModel.getAddress());
                            });
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sliderAdapter.renewItems(images);
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
            public void onFailure(Call<MountainResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;

        LatLng stevensPass = new LatLng(47.74499702, -121.09333296);
        map.addMarker(new MarkerOptions().position(stevensPass).title("Marker in Stevens Pass"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(stevensPass, 15f));
    }
}