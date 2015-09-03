package triplived.cos.com.gallery;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class BucketEntry implements Parcelable {
    public String bucketName;
    public int bucketId;
    public Image bucketPhoto;
    public int totalImages = 0 ;

    public BucketEntry(int id, String name) {
        bucketId = id;
        bucketName = name;
    }
    @Override
    public int hashCode() {
        int hashCode = 0;
        if(bucketPhoto != null ){
            hashCode= bucketPhoto.mMediaId ;
        }
        hashCode +=bucketId;
        return hashCode;
    }

    @Override
    public String toString() {
        return "BucketEntry{" +
                "bucketId=" + bucketId +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }

    public int getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(int totalImages) {
        this.totalImages = totalImages;
    }

    public void incrementImagesCount() {
        this.totalImages++;
    }

    public void setBucketPhoto(Image bucketPhoto) {
        this.bucketPhoto = bucketPhoto;
    }


    public Image getBucketPhoto() {
        return bucketPhoto;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BucketEntry)) return false;
        BucketEntry entry = (BucketEntry) object;
        return bucketId == entry.bucketId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bucketName);
        dest.writeInt(this.bucketId);
        dest.writeParcelable(this.bucketPhoto, 0);
        dest.writeInt(this.totalImages);
    }

    private BucketEntry(Parcel in) {
        this.bucketName = in.readString();
        this.bucketId = in.readInt();
        this.bucketPhoto = in.readParcelable(Image.class.getClassLoader());
        this.totalImages = in.readInt();
    }

    public static final Parcelable.Creator<BucketEntry> CREATOR = new Parcelable.Creator<BucketEntry>() {
        public BucketEntry createFromParcel(Parcel source) {
            return new BucketEntry(source);
        }

        public BucketEntry[] newArray(int size) {
            return new BucketEntry[size];
        }
    };
}
