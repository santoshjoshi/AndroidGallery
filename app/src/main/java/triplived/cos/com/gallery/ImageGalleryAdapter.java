package triplived.cos.com.gallery;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashSet;

public class ImageGalleryAdapter extends ArrayAdapter<BucketEntry> {

    private int layoutResource;
    private HashSet<BucketEntry> images = new HashSet<>();

    public ImageGalleryAdapter(Context context , int resourceId) {
        super(context, 0);
        this.layoutResource = resourceId;

    }

    public HashSet<BucketEntry> getImages() {
        return images;
    }

    public boolean addImage(BucketEntry image){
        return this.images.add(image);
    }

    public boolean removeImage(BucketEntry image){
        return this.images.remove(image);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BucketEntry bucketEntry = getItem(position);
        ViewHolder1 holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResource, null);
            holder = new ViewHolder1();
            holder.mThumbnail = (ImageView) convertView.findViewById(R.id.thumbnail_image);
            holder.mThumbnailOverlapView = (ImageView) convertView.findViewById(R.id.thumbnail_image_over);
            holder.textView = (TextView) convertView.findViewById(R.id.folderName);
            holder.totalImages = (TextView) convertView.findViewById(R.id.totalImages);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder1) convertView.getTag();
        }

        if(holder.textView != null) {
            holder.textView.setText(bucketEntry.bucketName);
        }

        if(holder.totalImages != null) {
            holder.totalImages.setText(bucketEntry.getTotalImages() + "");
        }

        if (holder.mImage == null || !holder.mImage.equals(bucketEntry)) {

            Picasso.Builder builder = new Picasso.Builder(convertView.getContext());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e("TTT", "file not found " + uri.toString());
                }
            });

            Picasso.with(getContext())
                    .load(new File(bucketEntry.bucketPhoto.mUri.toString()))
                    .resize(90, 90)
                    .into(holder.mThumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("TAG", "success");
                        }

                        @Override
                        public void onError() {
                            Log.d("TAG", "error");
                        }
                    });
            holder.mImage = bucketEntry;
        }
        return convertView;
    }

    public class ViewHolder1<T> {
        ImageView mThumbnail;
        ImageView mThumbnailOverlapView;
        TextView textView;
        TextView totalImages;
        BucketEntry mImage;
    }

}