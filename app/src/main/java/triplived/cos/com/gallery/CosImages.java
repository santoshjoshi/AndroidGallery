package triplived.cos.com.gallery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shadman Anwer on 17/04/15.
 */
public class CosImages implements Parcelable {

    private static final String TAG = "COS_IMAGES";
    private static final String IMAGE_UPLOAD_ENDPOINT = "image/uploadTimelineFile";

    private String mUri;
    private String mThumbUri;

    public int mMediaId;
    public int mDbId;
    public int mEntityId;
    public int mEntityType;
    public String mRelativePath;
    public String mServerUri;
    public String mServerThumbUri;
    public String mText;
    public int mOrientation;

    public int imageType = Image.TYPE.IMAGE.ordinal();

    public String getmUri() {
        return mUri;
    }

    public void setmUri(String mUri) {
        this.mUri = mUri;
    }

    public String getmThumbUri() {
        return mThumbUri;
    }

    public void setmThumbUri(String mThumbUri) {
        this.mThumbUri = mThumbUri;
    }

    public int getmMediaId() {
        return mMediaId;
    }

    public void setmMediaId(int mMediaId) {
        this.mMediaId = mMediaId;
    }

    public int getmDbId() {
        return mDbId;
    }

    public void setmDbId(int mDbId) {
        this.mDbId = mDbId;
    }

    public int getmEntityId() {
        return mEntityId;
    }

    public void setmEntityId(int mEntityId) {
        this.mEntityId = mEntityId;
    }

    public int getmEntityType() {
        return mEntityType;
    }

    public void setmEntityType(int mEntityType) {
        this.mEntityType = mEntityType;
    }

    public String getmRelativePath() {
        return mRelativePath;
    }

    public void setmRelativePath(String mRelativePath) {
        this.mRelativePath = mRelativePath;
    }

    public String getmServerUri() {
        return mServerUri;
    }

    public void setmServerUri(String mServerUri) {
        this.mServerUri = mServerUri;
    }

    public String getmServerThumbUri() {
        return mServerThumbUri;
    }

    public void setmServerThumbUri(String mServerThumbUri) {
        this.mServerThumbUri = mServerThumbUri;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmOrientation() {
        return mOrientation;
    }

    public void setmOrientation(int mOrientation) {
        this.mOrientation = mOrientation;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public static final int ENTITY_TYPE_ATTRACTIONS = 0;
    public static final int ENTITY_TYPE_TRANSPORT = 1;
    public static final int ENTITY_TYPE_RECORDING = 2;
    public static final int ENTITY_TYPE_UPDATE = 3;
    public static final int ENTITY_TYPE_RECORDING_UPDATE = 4;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUri);
        dest.writeString(this.mThumbUri);
        dest.writeInt(this.mMediaId);
        dest.writeInt(this.mDbId);
        dest.writeInt(this.mEntityId);
        dest.writeInt(this.mEntityType);
        dest.writeString(this.mRelativePath);
        dest.writeString(this.mServerUri);
        dest.writeString(this.mServerThumbUri);
        dest.writeString(this.mText);
        dest.writeInt(this.mOrientation);
        dest.writeInt(this.imageType);
    }

    public CosImages() {
    }

    private CosImages(Parcel in) {
        this.mUri = in.readString();
        this.mThumbUri = in.readString();
        this.mMediaId = in.readInt();
        this.mDbId = in.readInt();
        this.mEntityId = in.readInt();
        this.mEntityType = in.readInt();
        this.mRelativePath = in.readString();
        this.mServerUri = in.readString();
        this.mServerThumbUri = in.readString();
        this.mText = in.readString();
        this.mOrientation = in.readInt();
        this.imageType = in.readInt();
    }

    public static final Parcelable.Creator<CosImages> CREATOR = new Parcelable.Creator<CosImages>() {
        public CosImages createFromParcel(Parcel source) {
            return new CosImages(source);
        }

        public CosImages[] newArray(int size) {
            return new CosImages[size];
        }
    };
}