package com.neu.snowhouse.ui.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neu.snowhouse.R;

public class PostFragment extends Fragment {
    TextView postId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        postId = view.findViewById(R.id.postId);
        assert getArguments() != null;
        postId.setText(String.valueOf(getArguments().getInt("postId")));
        return view;
    }
}