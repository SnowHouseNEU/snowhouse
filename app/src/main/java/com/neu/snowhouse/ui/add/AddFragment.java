package com.neu.snowhouse.ui.add;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.neu.snowhouse.R;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {

    private EditText enterTitle;
    private EditText mainBody;
    private ImageView imageView;
    private Button post;
    private Uri selectedImage;
    private ImageButton cameraImage;
    private ImageButton folderImage;
    private String partImage1;
    private String partImage2;

    private static final int PICK_IMAGE_REQUEST = 9544;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        enterTitle = view.findViewById(R.id.addTitle);
        mainBody = view.findViewById(R.id.mainText);
        imageView = (ImageView) view.findViewById(R.id.image);
        post = view.findViewById(R.id.postButton);
        return view;
    }

    public void getImageFromPhotos () {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(selectedImage,
                        imageProjection, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    partImage1 = cursor.getString(indexImage);
                    folderImage.setText(partImage1);                                                        // Get the image file absolute path
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image.setImageBitmap(bitmap);
            }
        }
    }


//    public boolean isStoragePermissionGranted() {
//        int ACCESS_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if ((ACCESS_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
//            return false;
//        }
//        return true;
//    }


}

