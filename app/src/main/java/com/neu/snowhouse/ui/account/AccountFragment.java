package com.neu.snowhouse.ui.account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
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

import com.neu.snowhouse.DatabaseHelper;
import com.neu.snowhouse.R;
import com.neu.snowhouse.SessionManagement;
import com.neu.snowhouse.UserAuthorizeActivity;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.request.UserDropAccountRequestModel;
import com.neu.snowhouse.model.request.UserResetPasswordRequestModel;
import com.neu.snowhouse.model.response.LitePostResponseModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    // for posts
    TextView accountUserName;
    String userName;
    RecyclerView recyclerView;
    AccountAdapter accountAdapter;
    ArrayList<LitePostResponseModel> posts = new ArrayList<>();
    API api = RetrofitClient.getInstance().getAPI();
    TextView helperMessage;
    // for account related functions
    private AlertDialog dialog1;
    private AlertDialog dialog2;
    private EditText editOldPassword, editNewPassword;

    Call<ResponseBody> uploadPassword;
    Call<ResponseBody> dropAccount;

    // for user image
    Uri selectedImage;
    String part_image;
    CircleImageView image;
    DatabaseHelper mDatabaseHelper;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
        inflater.inflate(R.menu.top_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = SessionManagement.getUserName(getContext());
        accountUserName = view.findViewById(R.id.account_userName);
        accountUserName.setText(userName);
        // for posts
        recyclerView = view.findViewById(R.id.account_post_recycler_view);
        helperMessage = view.findViewById(R.id.account_helper_message);
        accountAdapter = new AccountAdapter(posts);
        mDatabaseHelper = new DatabaseHelper(getContext());
        image = view.findViewById(R.id.account_image);
        setUserImage();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(v);
            }
        });
        getMyPosts();
        DeleteClickListener listener = new DeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                int postId = posts.get(position).getPostId();
                Call<ResponseBody> deletePost = api.deletePostById(userName, postId);
                deletePost.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                                posts.remove(position);
                                accountAdapter.notifyItemRemoved(position);
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
                        t.printStackTrace();
                        Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        accountAdapter.setListener(listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(accountAdapter);
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
            helperMessage.setText("You don't have any posts...");
        } else {
            helperMessage.setText("");
        }
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

    public void createResetPasswordDialog() {
        // for popup
        AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(getActivity());
        final View resetPopup = getLayoutInflater().inflate(R.layout.reset_popup, null);
        editOldPassword = (EditText) resetPopup.findViewById(R.id.reset_oldpassword);
        editNewPassword = (EditText) resetPopup.findViewById(R.id.reset_newpassword);
        Button button_confirm_reset = (Button) resetPopup.findViewById(R.id.reset_save);
        Button button_cancel_reset = (Button) resetPopup.findViewById(R.id.reset_cancel);
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

    public void createDropAccountDialog() {
        AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(getActivity());
        final View dropPopup = getLayoutInflater().inflate(R.layout.drop_popup, null);
        Button button_confirm_drop = (Button) dropPopup.findViewById(R.id.button_drop_confirm);
        Button button_cancel_drop = (Button) dropPopup.findViewById(R.id.button_drop_cancel);
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

    ActivityResultLauncher<Intent> launchActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            // your operation....
            selectedImage = data.getData();                                                         // Get the image file URI
            String[] imageProjection = {MediaStore.Images.Media.DATA};
            @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(selectedImage, imageProjection, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int indexImage = cursor.getColumnIndex(imageProjection[0]);
                part_image = cursor.getString(indexImage);
                part_image = verify(part_image);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Set the ImageView with the bitmap of the image
                image.setImageBitmap(bitmap);

                // Store the image to the SQLite db
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                byte[] bytes = stream.toByteArray();
                storeUserImage(bytes);
            }
        }
    });

    // Method for starting the activity for selecting image from phone storage
    public void pickImage(View view) {
        verifyStoragePermissions((Activity) view.getContext());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launchActivity.launch(intent);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void setUserImage() {
        Cursor data = mDatabaseHelper.getUserImage(userName);
        if (!data.moveToFirst()) data.moveToFirst();
        if (data.getCount() != 0) {
            byte[] picture = data.getBlob(1);
            final Bitmap[] bmp = new Bitmap[1];
            bmp[0] = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            image.setImageBitmap(bmp[0]);
        }
        mDatabaseHelper.close();
    }

    private void storeUserImage(byte[] picture) {
        Cursor data = mDatabaseHelper.getUserImage(userName);
        if (data.getCount() == 0) {
            mDatabaseHelper.addUserImage(userName, picture);
        } else {
            mDatabaseHelper.updateUserImage(userName, picture);
        }
        mDatabaseHelper.close();
    }

    private String verify(String path) {
        String[] arr = path.split("/");
        if (!arr[3].equals("DCIM")) {
            return path;
        }
        String res = "/";
        res += arr[0] + "/";
        res += arr[1] + "/";
        res += arr[2] + "/";
        res += "Pictures" + "/";
        for (int i = 5; i < arr.length; i++) {
            res += arr[i];
            if (i != arr.length - 1) {
                res += "/";
            }
        }
        return res;
    }
}