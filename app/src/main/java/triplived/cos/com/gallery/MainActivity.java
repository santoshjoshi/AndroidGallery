package triplived.cos.com.gallery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * https://android.googlesource.com/platform/packages/apps/Gallery2/+/jb-dev/src/com/android/gallery3d/data/LocalAlbumSet.java
 *
 */
public class MainActivity extends ActionBarActivity  implements OnFragmentInteractionListener {


    private HashSet<Integer> locationImagesId = new HashSet<>();
    private HashSet<BucketEntry> images = new HashSet<>();
    MainActivity ctxt ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctxt = this;

        Intent intent = getIntent();

        String type = intent.getStringExtra("type");
        type = "DIR";
        if("DIR".equalsIgnoreCase(type)) {

            FragmentListDirectory dir = new FragmentListDirectory();
            Bundle bundle = new Bundle();
            bundle.putInt("layout", R.layout.gallery_folder);
            dir.setArguments(bundle);

            loadFragment(dir);
        }

    }

    public void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
        transaction.replace(R.id.activity_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static  Cursor getCursor(int bucketId, Context ctxt) {
        final String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.Media.DATE_ADDED,
        };

        String WHERE_CLAUSE = MediaStore.Images.ImageColumns.BUCKET_ID + " = "+bucketId;
        String BUCKET_ORDER_BY = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC, " + MediaStore.Images.ImageColumns._ID + " DESC";
        Uri uriMedia = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return ctxt.getContentResolver().query(uriMedia, PROJECTION_BUCKET, WHERE_CLAUSE, null, BUCKET_ORDER_BY);
    }

    @Override
    public void onFragmentInteraction(Bundle  bundleReceived){

        Bundle bundle = new Bundle();
        String type = bundleReceived.getString("type");

        if("IMG".equalsIgnoreCase(type)){

            int bucketId = bundleReceived.getInt("bucketId");
            bundle.putInt("layout", R.layout.gallery_image);
            bundle.putInt("bucketId", bucketId);

            FragmentListImage fragment = new FragmentListImage();

            fragment.setArguments(bundle);
            loadFragment(fragment);

        }else if("DIR".equalsIgnoreCase(type)){

            FragmentListDirectory fragment = new FragmentListDirectory();

            bundle.putInt("layout", R.layout.gallery_folder);
            fragment.setArguments(bundle);
            loadFragment(fragment);

        } else   if("VIEW".equalsIgnoreCase(type)){

           CosImageViewerFragment imageViewerFragment =  new  CosImageViewerFragment();

            Bundle args = new Bundle();
            args.putInt("layout", R.layout.gallery_image_selected);
            args.putInt(CosImageViewerFragment.ARG_POSITION, 1);
            args.putInt(CosImageViewerFragment.ARG_TRIP_TYPE, 1);

            ArrayList<BucketEntry> i = new ArrayList<>();
            for(BucketEntry entry : this.images){
                i.add(entry);
            }

            args.putParcelableArrayList(CosImageViewerFragment.ARG_IMG_LIST_2, i);

            imageViewerFragment.setArguments(args);
            loadFragment(imageViewerFragment);
        }

    }

    public void addImages(HashSet<BucketEntry> images ) {

        this.images.addAll(images);
    }


}