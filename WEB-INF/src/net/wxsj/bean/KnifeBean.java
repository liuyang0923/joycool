/*
 * Created on 2006-12-9
 *
 */
package net.wxsj.bean;

import net.joycool.wap.util.Constants;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-9
 * 
 * 说明：
 */
public class KnifeBean {
    int id;

    String name;

    String image;

    String introduction;

    String price;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the image.
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image
     *            The image to set.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return Returns the introduction.
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param introduction
     *            The introduction to set.
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    /**
     * @return Returns the price.
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price
     *            The price to set.
     */
    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return Constants.URL_PREFIX + "/wxsj/knife/images/" + image;
    }
}
