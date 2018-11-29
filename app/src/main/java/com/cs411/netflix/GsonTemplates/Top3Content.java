package com.cs411.netflix.GsonTemplates;

public class Top3Content {

    private String ContentId;

    private String ViewingLanguage;

    private String Genre;

    private String Director;

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getViewingLanguage() {
        return ViewingLanguage;
    }

    public void setViewingLanguage(String viewingLanguage) {
        ViewingLanguage = viewingLanguage;
    }


    public String getContentId() {
        return ContentId;
    }


    public void setContentId(String contentId) {
        this.ContentId = contentId;
    }
}
