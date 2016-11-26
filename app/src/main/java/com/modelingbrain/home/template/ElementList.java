package com.modelingbrain.home.template;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class ElementList {
    private final int resourceImage;
    private final String title;
    private final String subTitle;
    private final String secondSubTitle;
    private final int resourceColorRectangle;
    private final int iD;

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
