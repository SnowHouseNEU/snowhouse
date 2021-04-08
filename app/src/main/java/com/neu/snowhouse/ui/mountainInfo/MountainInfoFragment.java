package com.neu.snowhouse.ui.mountainInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.neu.snowhouse.R;
import com.neu.snowhouse.databinding.FragmentMountainInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class MountainInfoFragment extends Fragment {
    private FragmentMountainInfoBinding binding;

    public MountainInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMountainInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.crystal_mountain, "Crystal mountain", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.stevens_pass, "Stevens Pass", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.summit_at_snoqualmie, "Snoqualmie", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }
}