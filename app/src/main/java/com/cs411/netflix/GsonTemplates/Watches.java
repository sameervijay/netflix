package com.cs411.netflix.GsonTemplates;

import android.os.Parcel;
import android.os.Parcelable;

public class Watches implements Parcelable {
    private String Username, ViewingLanguage, TimeStamp;
    private int ContentId;
    private float UserRating;

    public Watches() {

    }
    public Watches(String username, String viewingLanguage, String timeStamp, int contentId, float userRating) {
        Username = username;
        ViewingLanguage = viewingLanguage;
        TimeStamp = timeStamp;
        ContentId = contentId;
        UserRating = userRating;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getViewingLanguage() {
        return ViewingLanguage;
    }

    public void setViewingLanguage(String viewingLanguage) {
        ViewingLanguage = viewingLanguage;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public int getContentId() {
        return ContentId;
    }

    public void setContentId(int contentId) {
        ContentId = contentId;
    }

    public float getUserRating() {
        return UserRating;
    }

    public void setUserRating(float userRating) {
        UserRating = userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Username);
        dest.writeString(this.ViewingLanguage);
        dest.writeString(this.TimeStamp);
        dest.writeInt(this.ContentId);
        dest.writeFloat(this.UserRating);
    }

    protected Watches(Parcel in) {
        this.Username = in.readString();
        this.ViewingLanguage = in.readString();
        this.TimeStamp = in.readString();
        this.ContentId = in.readInt();
        this.UserRating = in.readFloat();
    }

    public static final Parcelable.Creator<Watches> CREATOR = new Parcelable.Creator<Watches>() {
        @Override
        public Watches createFromParcel(Parcel source) {
            return new Watches(source);
        }

        @Override
        public Watches[] newArray(int size) {
            return new Watches[size];
        }
    };
}
