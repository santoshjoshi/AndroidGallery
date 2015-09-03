package triplived.cos.com.gallery;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 */
public class CosImageViewerFragment extends Fragment {
    public static final String ARG_POSITION = "position";
    public static final String ARG_IMG_LIST = "image_list";
    public static final String ARG_IMG_LIST_2 = "image_list_2";
    public static final String ARG_TRIP_TYPE = "trip_type";

    private int mPosition;
    private ViewPager mRootPager;

    private ImageView previousVisibleImage;
    private CosImagePagerAdapter mAdapter;
    private ImageGalleryAdapter imageGalleryAdapter;

    private ArrayList<BucketEntry> images = new ArrayList<>();
    private ArrayList<CosImages> images1 = new ArrayList<>();

    private ArrayList<CosImages> cosImages;

    public static CosImageViewerFragment newInstance(int position, ArrayList<? extends CosImages> images) {
        CosImageViewerFragment fragment = new CosImageViewerFragment();
        Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            args.putInt(ARG_TRIP_TYPE, 1);
            args.putParcelableArrayList(ARG_IMG_LIST, images);

            fragment.setArguments(args);


            return fragment;
        }

        public CosImageViewerFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mPosition = getArguments().getInt(ARG_POSITION);

                images = getArguments().getParcelableArrayList(ARG_IMG_LIST_2);
                int layout =   getArguments().getInt("layout") ;
                imageGalleryAdapter = new ImageGalleryAdapter(this.getActivity(), layout);
                images1= new ArrayList<CosImages>();


                for(BucketEntry entry : images ){

                    CosImages cosImages = new CosImages();
                    cosImages.setImageType( Image.TYPE.IMAGE.ordinal());
                    cosImages.setmThumbUri(entry.getBucketPhoto().mUri.toString());
                    cosImages.setmUri(entry.getBucketPhoto().mUri.toString());
                    images1.add(cosImages);

                    imageGalleryAdapter.add(entry);
                }



            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            final FrameLayout mRootPagerFrameLayout = (FrameLayout) inflater.inflate(R.layout.timeline_image_viewer, container, false);

            mRootPager = (ViewPager) mRootPagerFrameLayout.findViewById(R.id.image_view_pager);
            mAdapter = new CosImagePagerAdapter(getActivity(), this);
            mAdapter.setCosImages(images1);
            mRootPager.setAdapter(mAdapter);
            mRootPager.setCurrentItem(mPosition, false);



            final GridView galleryGridView = (GridView) mRootPagerFrameLayout.findViewById(R.id.gallery_grid_selected);
            galleryGridView.setAdapter(imageGalleryAdapter);

            galleryGridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ImageGalleryAdapter.ViewHolder1 holder = (ImageGalleryAdapter.ViewHolder1) view.getTag();

                    holder.mThumbnailOverlapView.setVisibility(View.VISIBLE);
                    if(previousVisibleImage != null ) {
                        previousVisibleImage.setVisibility(View.GONE);
                    }
                    previousVisibleImage =  holder.mThumbnailOverlapView;

                    mRootPager.setCurrentItem(position);
                }
            });


            mRootPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    View view = galleryGridView.getChildAt(position);
                    if(view != null) {
                        if(previousVisibleImage != null) {
                            previousVisibleImage.setVisibility(View.GONE);
                        }
                        view.findViewById(R.id.thumbnail_image_over).setVisibility(View.VISIBLE);
                        previousVisibleImage = (ImageView) view.findViewById(R.id.thumbnail_image_over) ;
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        return mRootPagerFrameLayout;
    }



}