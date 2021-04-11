package com.neu.snowhouse.ui.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.snowhouse.R;
import com.neu.snowhouse.SessionManagement;
import com.neu.snowhouse.UserAuthorizeActivity;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.request.UserDropAccountRequestModel;
import com.neu.snowhouse.model.request.UserResetPasswordRequestModel;
import com.neu.snowhouse.model.response.LitePostResponseModel;
import com.neu.snowhouse.ui.forum.PostAdapter;
import com.neu.snowhouse.ui.login.LoginFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private AlertDialog.Builder dialogBuilder1;
    private AlertDialog.Builder dialogBuilder2;
    private AlertDialog dialog1;
    private AlertDialog dialog2;
    private EditText editOldPassword, editNewPassword;
    private Button button_confirm_reset, button_cancel_reset, button_confirm_drop, button_cancel_drop;
    String userName;
    RecyclerView recyclerView;
    AccountAdapter accountAdapter;
    ArrayList<LitePostResponseModel> posts = new ArrayList<>();
    API api = RetrofitClient.getInstance().getAPI();
    TextView helperMessage;
    Call<ResponseBody> uploadPassword;
    Call<ResponseBody> dropAccount;
//    int postId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.button_resetpassword) {
            createResetPasswordDialog();
        }
        if (id == R.id.button_logout) {
            SessionManagement.removeUserName(getActivity());
            Intent intent = new Intent(getContext(), UserAuthorizeActivity.class);
            startActivity(intent);
        }
        if (id == R.id.button_dropacc) {
            createDropAccountDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createResetPasswordDialog(){
        dialogBuilder1 = new AlertDialog.Builder(getActivity());
        final View resetPopup = getLayoutInflater().inflate(R.layout.reset_popup, null);
        editOldPassword = (EditText) resetPopup.findViewById(R.id.reset_oldpassword);
        editNewPassword = (EditText) resetPopup.findViewById(R.id.reset_newpassword);
        button_confirm_reset = (Button) resetPopup.findViewById(R.id.reset_save);
        button_cancel_reset = (Button) resetPopup.findViewById(R.id.reset_cancel);
        dialogBuilder1.setView(resetPopup);
        dialog1 = dialogBuilder1.create();
        dialog1.show();
        userName = SessionManagement.getUserName(getContext());
        button_confirm_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editOldPassword.getText().toString().trim().equals("") || editNewPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Old Password or New Password can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    UserResetPasswordRequestModel userResetPasswordRequestModel = new UserResetPasswordRequestModel();
                    userResetPasswordRequestModel.setUserName(userName);
                    userResetPasswordRequestModel.setOldPassword(editOldPassword.getText().toString());
                    userResetPasswordRequestModel.setNewPassword(editNewPassword.getText().toString());
                    uploadPassword = api.resetPassword(userResetPasswordRequestModel);

                    uploadPassword.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                                    dialog1.dismiss();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        button_cancel_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

    }
     public void createDropAccountDialog(){
        dialogBuilder2 = new AlertDialog.Builder(getActivity());
        final View dropPopup = getLayoutInflater().inflate(R.layout.drop_popup, null);
        button_confirm_drop = (Button) dropPopup.findViewById(R.id.button_drop_confirm);
        button_cancel_drop = (Button) dropPopup.findViewById(R.id.button_drop_cancel);
        dialogBuilder2.setView(dropPopup);
        dialog2 = dialogBuilder2.create();
        dialog2.show();
        userName = SessionManagement.getUserName(getContext());
        button_confirm_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDropAccountRequestModel userDropAccountRequestModel = new UserDropAccountRequestModel();
                userDropAccountRequestModel.setUserName(userName);
                dropAccount = api.deleteAccount(userDropAccountRequestModel);
                dropAccount.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getContext(), UserAuthorizeActivity.class);
                                startActivity(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        button_cancel_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
     }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;
        userName = SessionManagement.getUserName(getContext());
        recyclerView = view.findViewById(R.id.account_post);
        helperMessage = view.findViewById(R.id.account_helper_message);
        accountAdapter = new AccountAdapter(posts);
//        postId = getArguments().getInt("postId");
        getMyPosts();
//        Button logoutButton = getView().findViewById(R.id.button_logout);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SessionManagement.removeUserName(getActivity());
//                Intent intent = new Intent(getContext(), UserAuthorizeActivity.class);
//                startActivity(intent);
//            }
//        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(accountAdapter);
//        DeleteClickListener deleteClickListener = new DeleteClickListener() {
////            @Override
////            public void onDeleteClick(int position) {
////                Call<ResponseBody>
////            }
//        }

    }
    private void getMyPosts() {
        Call<List<LitePostResponseModel>> fetchPosts = api.getMyPosts(userName);
        fetchPosts.enqueue(new Callback<List<LitePostResponseModel>>() {
            @Override
            public void onResponse(Call<List<LitePostResponseModel>> call, Response<List<LitePostResponseModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    posts = (ArrayList<LitePostResponseModel>) response.body();
                    accountAdapter.updateAdapter(posts);
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
            helperMessage.setText("No posts...");
        } else {
            helperMessage.setText("");
        }
    }
}