package com.neu.snowhouse.ui.mountain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.neu.snowhouse.R;
import com.neu.snowhouse.model.response.Image;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ImageViewHolder> {

    private Context context;
    private List<Image> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<Image> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Image image) {
        this.mSliderItems.add(image);
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent) {
        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image_item, null);
        return new ImageViewHolder(inflate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, final int position) {
        final Bitmap[] bmp = new Bitmap[1];
        byte[] bytes = Base64.getDecoder().decode(mSliderItems.get(position).getPicture());
        bmp[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        viewHolder.image.setImageBitmap(bmp[0]);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    static class ImageViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView image;

        public ImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_item);
        }
    }
}
