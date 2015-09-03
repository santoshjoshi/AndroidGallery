package triplived.cos.com.gallery;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Santosh Joshi
 */
public class Image implements android.os.Parcelable {

    public static enum TYPE { IMAGE, VIDEO};
    public int mMediaId;
    public Uri mUri;
    public int mOrientation;
    public String bucketName;
    public int bucketId ;
    public int type = TYPE.IMAGE.ordinal() ; //default type

    public Image(int mediaId, Uri uri, int orientation, String bucketName, int bucketId) {
        mMediaId = mediaId;
        mUri = uri;
        mOrientation = orientation;
        this.bucketId = bucketId;
        this.bucketName = bucketName;
    }

    public int getBucketId() {
        return bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMediaId);
        dest.writeParcelable(this.mUri, 0);
        dest.writeInt(this.mOrientation);
        dest.writeInt(this.type);
    }

    private Image(Parcel in) {

        this.mMediaId = in.readInt();
        this.mUri = in.readParcelable(Uri.class.getClassLoader());
        this.mOrientation = in.readInt();
        this.type = in.readInt();

    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
