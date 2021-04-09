package com.neu.snowhouse.ui.inbox;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.snowhouse.R;
import com.neu.snowhouse.SessionManagement;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.response.LitePostResponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxFragment extends Fragment {
    String userName;
    RecyclerView recyclerView;
    InboxAdapter inboxAdapter;
    ArrayList<LitePostResponseModel> posts = new ArrayList<>();
    API api = RetrofitClient.getInstance().getAPI();
    TextView helperMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = SessionManagement.getUserName(getContext());
        helperMessage = view.findViewById(R.id.inbox_helper_message);
        recyclerView = view.findViewById(R.id.inbox_post_recycler_view);
        inboxAdapter = new InboxAdapter(posts);
        getPosts();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(inboxAdapter);
    }

    private void getPosts() {
        Call<List<LitePostResponseModel>> fetchPosts = api.getPostsWithNewComments(userName);
        fetchPosts.enqueue(new Callback<List<LitePostResponseModel>>() {
            @Override
            public void onResponse(Call<List<LitePostResponseModel>> call, Response<List<LitePostResponseModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    posts = (ArrayList<LitePostResponseModel>) response.body();
                    inboxAdapter.updateAdapter(posts);
                    setHelperMessage(posts.size());
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
            public void onFailure(Call<List<LitePostResponseModel>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setHelperMessage(int size) {
        if (size == 0) {
            helperMessage.setText("Your posts don't have any new comments...");
        } else {
            helperMessage.setText("");
        }
    }
}