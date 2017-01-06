package com.rydohg.assignmenttracker;

public class OptionListItem {
    private String title;
    private int drawableId;
    private int imageViewOd;

    public OptionListItem(String title, int drawableId, int imageViewId){
        this.title = title;
        this.drawableId = drawableId;
        this.imageViewOd = imageViewId;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public int getImageViewId() {
        return imageViewOd;
    }

    public void setImageViewId(int imageViewOd) {
        this.imageViewOd = imageViewOd;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
