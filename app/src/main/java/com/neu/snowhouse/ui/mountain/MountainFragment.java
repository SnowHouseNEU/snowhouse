package com.neu.snowhouse.ui.mountain;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
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
    List<Image> images = new ArrayList<>();
    final Object monitor = new Object();
    boolean dataReady = false;
    private final Handler textHandler = new Handler();
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    TextView mountainName;
    TextView mountainDesc;
    TextView mountainTicket;
    TextView mountainWeather;
    TextView mountainHours;
    TextView mountainAddress;
    double latitude;
    double longitude;
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
        View view = inflater.inflate(R.layout.fragment_mountain, container, false);
        userName = SessionManagement.getUserName(getContext());
        assert getArguments() != null;
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
        thread1.start();
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(8); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        mapView = view.findViewById(R.id.mapsView);
        mapView.onCreate(null);
        mapView.onResume();

        return view;
    }

    Thread thread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            getMountain();
        }
    });

    private void getMountain() {
        Call<MountainResponseModel> fetchMountain = api.getMountainById(mountainId, userName);
        fetchMountain.enqueue(new Callback<MountainResponseModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MountainResponseModel> call, Response<MountainResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MountainResponseModel mountainResponseModel = response.body();
                    textHandler.post(() -> {
                        mountainName.setText(mountainResponseModel.getMountainName());
                        mountainDesc.setText(mountainResponseModel.getDescription());
                        mountainTicket.setText(mountainResponseModel.getTicketInfo());
                        mountainWeather.setText("Weather: " + mountainResponseModel.getWeather().toString());
                        mountainHours.setText("Open Hours: " + mountainResponseModel.getOpenHour());
                        mountainAddress.setText("Address: " + mountainResponseModel.getAddress());
                        images.add(mountainResponseModel.getImage1());
                        images.add(mountainResponseModel.getImage2());
                        images.add(mountainResponseModel.getImage3());
                        sliderAdapter.renewItems(images);
                    });
                    latitude = mountainResponseModel.getLatitude();
                    longitude = mountainResponseModel.getLongitude();
                    System.out.println(111);
                    System.out.println(latitude);
                    System.out.println(longitude);
                    assert getActivity() != null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapView.getMapAsync(MountainFragment.this);
                        }
                    });
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
        assert getContext() != null;
        MapsInitializer.initialize(getContext());
        map = googleMap;
        System.out.println(222);
        System.out.println(latitude);
        System.out.println(longitude);
        LatLng stevensPass = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(stevensPass).title("Marker in Stevens Pass"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(stevensPass, 15f));
    }
}