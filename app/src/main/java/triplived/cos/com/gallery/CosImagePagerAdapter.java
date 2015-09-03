package triplived.cos.com.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import java.util.ArrayList;

/**
 * Created by Shadman Anwer on 05-04-2015.
 */
public class CosImagePagerAdapter extends PagerAdapter {

    private static final String TAG = "COS_IMAGE_ADAPTER";

    private View mCurrentView;

    private Context mContext;
    private ArrayList<CosImages> cosImages;
    private boolean showPannel ;
    private Fragment fragment;

    public boolean isShowPannel() {
        return showPannel;
    }

    public CosImagePagerAdapter(Context context, Fragment fragment) {
        mContext = context;
        cosImages = new ArrayList<>();
        this.fragment = fragment;

    }

    public ArrayList<CosImages> getCosImages() {
        return cosImages;
    }

    @Override
    public int getCount() {
        return cosImages.size();
    }

    public void setCosImages(ArrayList<CosImages> cosImages) {
        this.cosImages = cosImages;
    }

    public void removeAllViews() {
        cosImages.clear();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        final CosImagePagerAdapter cosImagePagerAdapter = this;

        if ( BuildConfig.DEBUG )
            Log.d(TAG, "Instantiate view = " + position);

        if (cosImages.size() > 0) {

            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View v = inflater.inflate(R.layout.gallery_item, (ViewGroup) null, false);

            final ImageView view = (ImageView) v.findViewById(R.id.galleryImage);
            final TextView errorTxtView = (TextView) v.findViewById(R.id.errorTxt);
            final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.image_loading_progress);


            final CosImages cm = cosImages.get(position);
            final TextView captionText = (TextView) v.findViewById(R.id.caption_text);
            if(cm.getmText() != null && cm.getmText() != "") {
                captionText.setText(cm.getmText());
            }

            if(cm.getImageType() == Image.TYPE.IMAGE.ordinal()){
                CosImageUtils.LoadImageToView(mContext, cm.getmUri(), view, new Callback() {
                    @Override
                    public void onSuccess() {
                        view.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() { progressBar.setVisibility(View.GONE); errorTxtView.setVisibility(View.VISIBLE); }
                });

            }else{

                final LinearLayout videoViewLayout = (LinearLayout) v.findViewById(R.id.videoViewLayout);
               // final VideoView mVideoView = (VideoView) v.findViewById(R.id.videoView);
                final ImageView playIcon = (ImageView) v.findViewById(R.id.play_icon);

                if(cm.getmThumbUri() == null) {
                    final Bitmap thumbnailImgofVideo = ThumbnailUtils.createVideoThumbnail(cm.getmUri(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    view.setImageBitmap(thumbnailImgofVideo);
                }else{
                    CosImageUtils.LoadImageToView(mContext, cm.getmThumbUri(), view, new Callback() {
                        @Override
                        public void onSuccess() {
                            view.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            playIcon.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            playIcon.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            errorTxtView.setVisibility(View.VISIBLE);
                        }
                    });
                }



                playIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        view.setVisibility(View.GONE);
                        playIcon.setVisibility(View.GONE);
                        videoViewLayout.setVisibility(View.VISIBLE);



                    }
                });
            }


            if(cm.mDbId > 0 ){
                final LinearLayout imagesToolBar = (LinearLayout) v.findViewById(R.id.imageToolBar);
                showPannel( imagesToolBar, v, cm, cosImagePagerAdapter, position, captionText);

            }else{
                //hide all buttons
                ViewGroup parent = (ViewGroup) v.findViewById(R.id.imageToolBar);
                parent.setVisibility(View.GONE);
                parent.removeAllViews();
            }

            container.addView(v);
            return v;
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view){
        if ( BuildConfig.DEBUG )
            Log.d(TAG, "View destroyed - " + view + " position - " + position);
        collection.removeView((View)view);
    }

    private boolean showPannel(final LinearLayout imagesToolBar,  final View v, final CosImages img, final CosImagePagerAdapter cosImagePagerAdapter, final int position, final TextView captionText ) {

        /*if(!isPanelShown) {
            return false;
        }*/

        final ImageButton deleteButton = (ImageButton) v.findViewById(R.id.deleteImage);
        final ImageButton shareButton  = (ImageButton) v.findViewById(R.id.shareImage);
        final ImageButton editButton  = (ImageButton) v.findViewById(R.id.addText);
        final EditText imageCaption  = (EditText) v.findViewById(R.id.imageCaption);
        final ImageButton saveButton  = (ImageButton) v.findViewById(R.id.saveCaption);

        imageCaption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                imageCaption.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(imageCaption, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        imageCaption.requestFocus();

        editButton.setOnClickListener( new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               imageCaption.setVisibility(View.VISIBLE);
                                               saveButton.setVisibility(View.VISIBLE);
                                               shareButton.setVisibility(View.GONE);
                                               deleteButton.setVisibility(View.GONE);
                                               editButton.setVisibility(View.GONE);
                                           }
                                       }

        );

        saveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editButton.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                imageCaption.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);

                String caption = imageCaption.getText().toString();

                imageCaption.setText("");

                if(caption != "") {
                    captionText.setText(caption);
                    //img.setText(caption);
                    //img.setCaption(caption, img.mDbId);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {

                ViewGroup parent = (ViewGroup) v.getParent();
                ViewPager mRootPager = (ViewPager) parent;
                int totalElement = mRootPager.getAdapter().getCount();

                //img.removeFromDb();
                cosImagePagerAdapter.removeView(position);

                if(totalElement == position) {
                    mRootPager.setCurrentItem(mRootPager.getCurrentItem() - 1);
                }else {
                    mRootPager.setCurrentItem(mRootPager.getCurrentItem() + 1);
                }
                parent.removeView(v);
            }
        });
/*
        shareButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared From TripLived");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Shared From TripLived");
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putExtra(Intent.EXTRA_STREAM,  Uri.fromFile(new File(img.getmImageUri())));
                shareIntent.setType("image*//*");
                mContext.startActivity(Intent.createChooser(shareIntent, mContext.getResources().getText(R.string.send_to)));
            }
        });*/

        Animation bottomUp = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_out_bottom);
        imagesToolBar.startAnimation(bottomUp);
        imagesToolBar.setVisibility(View.VISIBLE);

        return true;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object){
        mCurrentView = (View)object;
    }

    public void removeView(int index) {

        cosImages.remove(index);
        notifyDataSetChanged();
    }

    public CosImages getItemAtPosition(int index) {
        return  cosImages.get(index);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
