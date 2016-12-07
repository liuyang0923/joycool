/*
 * Created on 2005-11-30
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.image.ImageBean;

/**
 * @author lbj
 *
 */
public interface IImageService {
    public ImageBean getImage(String condition);
    public Vector getImagesList(String condition);
    public boolean updateImage(String set, String condition);
    public int getImagesCount(String condition);
}
