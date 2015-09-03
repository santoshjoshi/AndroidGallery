package triplived.cos.com.gallery;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashSet;


public class FragmentListImage extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageGalleryAdapter imageGalleryAdapter;
    int bucketId ;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int layout =   getArguments().getInt("layout") ;
        bucketId =   getArguments().getInt("bucketId") ;
        imageGalleryAdapter = new ImageGalleryAdapter(this.getActivity(), layout);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        this.menu = menu;
        inflater.inflate(R.menu.menu_image_selection, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:

                ((MainActivity) getActivity()).addImages(imageGalleryAdapter.getImages());
                Bundle bundle = new Bundle();
                 bundle.putString("type", "VIEW");
                 mListener.onFragmentInteraction(bundle);
                break;

            default:

        }

        return true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_list_image, container, false);
        GridView galleryGridView = (GridView) view.findViewById(R.id.gallery_grid);
        ArrayList<BucketEntry> images = getImagesFolder();

        galleryGridView.setAdapter(imageGalleryAdapter);
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageGalleryAdapter.ViewHolder1 holder = (ImageGalleryAdapter.ViewHolder1) view.getTag();

                if(holder.mThumbnailOverlapView.getVisibility() == View.GONE) {
                    holder.mThumbnailOverlapView.setVisibility(View.VISIBLE);
                    if(imageGalleryAdapter.addImage(holder.mImage)) {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(imageGalleryAdapter.getImages().size() + " Selected");
                    }//mens object added successfullt

                }else{

                    holder.mThumbnailOverlapView.setVisibility(View.GONE);
                   if(imageGalleryAdapter.removeImage(holder.mImage)){
                       ((MainActivity) getActivity()).getSupportActionBar().setTitle(imageGalleryAdapter.getImages().size() + " Selected");
                   }
                }

                if(imageGalleryAdapter.getImages().size() == 0){
                    menu.findItem(R.id.action_next).setVisible(false);
                }else if(imageGalleryAdapter.getImages().size() > 0){
                    menu.findItem(R.id.action_next).setVisible(true);
                }
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


    private ArrayList<BucketEntry> getImagesFolder() {
        Cursor imageCursor = null;
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
            imageCursor = MainActivity.getCursor(bucketId, (MainActivity) mListener);
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

                        //mGalleryAdapter.add(image) ;
                    }*/

                    image = new Image(id, uri, orientation, bucketName, bucketId);

                    BucketEntry entry = new BucketEntry(bucketId, bucketName);
                   // if (!buffer.contains(entry)) {
                        entry.setBucketPhoto(image);
                        buffer.add(entry);
                        imageGalleryAdapter.add(entry);
                        int count = getImagesCountInBucket(bucketId);
                        entry.setTotalImages(count);
                   // }
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
