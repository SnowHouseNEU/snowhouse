package com.neu.snowhouse.ui.mountainInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.neu.snowhouse.R;
import com.neu.snowhouse.databinding.FragmentMountainInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class MountainInfoFragment extends Fragment {
    private FragmentMountainInfoBinding binding;

    private static final String ARG_PARAM = "param";
    private String mParam;

    public MountainInfoFragment() {
        // Required empty public constructor
    }

    public static MountainInfoFragment newInstance(String param) {
        MountainInfoFragment fragment = new MountainInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mountain_info, container, false);
        TextView title =  view.findViewById(R.id.mountain_title_text_view);
        title.setText(mParam);
        return view;
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