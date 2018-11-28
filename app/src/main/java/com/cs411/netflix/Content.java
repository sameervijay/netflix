package com.cs411.netflix;

import android.os.Parcel;
import android.os.Parcelable;

public class Content implements Parcelable {
    private String ContentId;
    private String Name;
    private String Genre;
    private String AvgRating;
    private String ReleaseDate;
    private String Language;
    private String Director;
    private String Duration;
    private String Thumbnail;

    public String getContentId() {
        return ContentId;
    }

    public void setContentId(String contentId) {
        this.ContentId = contentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        this.Genre = genre;
    }

    public String getAvgRating() {
        return AvgRating;
    }

    public void setAvgRating(String avgRating) {
        this.AvgRating = avgRating;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.ReleaseDate = releaseDate;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        this.Language = language;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        this.Director = director;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        this.Duration = duration;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ContentId);
        dest.writeString(this.Name);
        dest.writeString(this.Genre);
        dest.writeString(this.AvgRating);
        dest.writeString(this.ReleaseDate);
        dest.writeString(this.Language);
        dest.writeString(this.Director);
        dest.writeString(this.Duration);
        dest.writeString(this.Thumbnail);
    }

    public Content() {
    }

    protected Content(Parcel in) {
        this.ContentId = in.readString();
        this.Name = in.readString();
        this.Genre = in.readString();
        this.AvgRating = in.readString();
        this.ReleaseDate = in.readString();
        this.Language = in.readString();
        this.Director = in.readString();
        this.Duration = in.readString();
        this.Thumbnail = in.readString();
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
