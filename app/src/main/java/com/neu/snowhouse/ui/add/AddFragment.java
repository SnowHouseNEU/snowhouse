package com.neu.snowhouse.ui.add;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.gson.Gson;
import com.neu.snowhouse.R;
import com.neu.snowhouse.SessionManagement;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.request.PostRequestModel;
import com.neu.snowhouse.model.response.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddFragment extends Fragment {

    API api = RetrofitClient.getInstance().getAPI();
    // for image chosen
    private static final int PICK_IMAGE_REQUEST = 9544;
    TextView imgPath;
    Uri selectedImage;
    String part_image;
    ImageView image;
    String jsonImage = "";

    // for construct the post
    String userName;
    EditText editTitle;
    EditText editTag;
    EditText editContent;
    Button uploadPostButton;

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
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgPath = view.findViewById(R.id.imagePickPath);
        image = view.findViewById(R.id.image);
        userName = SessionManagement.getUserName(getContext());
        editTitle = view.findViewById(R.id.editTitle);
        editTag = view.findViewById(R.id.editTag);
        editContent = view.findViewById(R.id.editContent);
        imgPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(v);
            }
        });
        uploadPostButton = view.findViewById(R.id.uploadPostButton);
        uploadPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost(v);
            }
        });
    }

    private void uploadPost(View view) {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();
        String tags = editTag.getText().toString().trim();
        String tag1 = "";
        String tag2 = "";
        String tag3 = "";
        String[] arr = tags.split(",");
        if (title.equals("")) {
            Toast.makeText(getContext(), "Post title can't be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (arr.length > 0) {
            tag1 = arr[0].trim();
        }
        if (arr.length > 1) {
            tag2 = arr[1].trim();
        }
        if (arr.length > 2) {
            tag3 = arr[2].trim();
        }
        PostRequestModel post = new PostRequestModel();
        post.setUserName(userName);
        post.setTitle(title);
        post.setContent(content);
        post.setTag1(tag1);
        post.setTag2(tag2);
        post.setTag2(tag3);
        post.setJsonImage(jsonImage);
        Call<ResponseBody> addPost = api.uploadPost(post);

        // get the result
        addPost.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.add_forum);
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
                imgPath.setText(part_image);                                                        // Get the image file absolute path
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                byte[] bytes = stream.toByteArray();
                bytes = reshape(bytes);
                jsonImage = Base64.getEncoder().encodeToString(bytes);
                ViewGroup.LayoutParams params = image.getLayoutParams();
                params.width = 500;
                params.height = 500;
                image.setLayoutParams(params);
                image.setImageBitmap(bitmap);                                                       // Set the ImageView with the bitmap of the image
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

    private byte[] reshape(byte[] img) {
        while (img.length > 500000) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 10, stream);
            img = stream.toByteArray();
        }
        return img;
    }
}

