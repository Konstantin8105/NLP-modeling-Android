package com.modelingbrain.home.template;

public class ElementList {
    private int resourceImage;
    private String title;
    private String subTitle;
    private String secondSubTitle;
    private int resourceColorRectangle;
    private int iD;

    public ElementList(int resourceImage, String title, String subTitle, String secondSubTitle, int resourceColorRectangle, int iD) {
        this.resourceColorRectangle = resourceColorRectangle;
        this.resourceImage = resourceImage;
        this.secondSubTitle = secondSubTitle;
        this.subTitle = subTitle;
        this.title = title;
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public int getID() {
        return iD;
    }

    public String getSecondSubTitle() {
        return secondSubTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getResourceColorRectangle() {
        return resourceColorRectangle;
    }

}
