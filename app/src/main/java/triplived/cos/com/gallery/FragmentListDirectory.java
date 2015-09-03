package triplived.cos.com.gallery;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class FragmentListDirectory extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageGalleryAdapter imageGalleryAdapter;

    public FragmentListDirectory() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Gallery");

        int layout =   getArguments().getInt("layout") ;
        imageGalleryAdapter = new ImageGalleryAdapter(this.getActivity(), layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_list_directory, container, false);
        GridView galleryGridView = (GridView) view.findViewById(R.id.gallery_grid);
        Cursor imageCursor = null;
        ArrayList<BucketEntry> ImageFolders = getImagesFolder(imageCursor);
        for (BucketEntry entry : ImageFolders) {
            Log.d("BUCKET", entry.toString());
        }
        galleryGridView.setAdapter(imageGalleryAdapter);
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //mGalleryAdapter.notifyDataSetChangedbu();
                ImageGalleryAdapter.ViewHolder1 holder = (ImageGalleryAdapter.ViewHolder1) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putInt("bucketId", holder.mImage.bucketId );
                bundle.putString("type", "IMG");

                mListener.onFragmentInteraction(bundle);
                //imageGalleryAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private ArrayList<BucketEntry> getImagesFolder(Cursor imageCursor) {
        ArrayList<BucketEntry> buffer = new ArrayList<BucketEntry>();
        try {
            //columns
            final String[] PROJECTION_BUCKET = {
                    MediaStore.Images.ImageColumns.BUCKET_ID,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.ImageColumns.ORIENTATION,
                    MediaStore.Images.Media.DATE_ADDED,
            };

            //sort by
            final String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

            //Group By
            final String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";

            //URI
            Uri uriMedia = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            imageCursor = ((MainActivity) mListener).getContentResolver().query(uriMedia, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);
            //query(, columns, null, null, BUCKET_ORDER_BY);

            while (imageCursor.moveToNext()) {
                int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                int orientation = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                String bucketName = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                int bucketId = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID));

                Image image = null;
                if (!uri.toString().contains("/WPSystem/")) {
                   /* if ((locationImagesId.size() == 0) || (locationImagesId.size() > 0 && !locationImagesId.contains(id))) {
                        image = new Image(id, uri, orientation, bucketName, bucketId);
                        //mGalleryAdapter.add(image) ;
                    }*/

                    image = new Image(id, uri, orientation, bucketName, bucketId);

                    BucketEntry entry = new BucketEntry(bucketId, bucketName);
                    if (!buffer.contains(entry)) {
                        entry.setBucketPhoto(image);
                        buffer.add(entry);
                        int count = getImagesCountInBucket(bucketId);
                        entry.setTotalImages(count);

                        imageGalleryAdapter.add(entry);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }

        return buffer;
    }

    public int getImagesCountInBucket(int bucketId) {

        Cursor imageCursor = MainActivity.getCursor(bucketId, (MainActivity) mListener);
        int count = 0;
        while (imageCursor.moveToNext()) {
            count = imageCursor.getCount();
            return count;
        }
        return 0;
    }

}
