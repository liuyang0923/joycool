/*
 * Created on 2006-8-15
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.video.VideoBean;
import net.joycool.wap.bean.video.VideoFileBean;

/**
 * @author MCQ
 *
 */
public interface IVideoService {
    
    public VideoBean getVideo(String condition);
    public Vector getVideoList(String condition);
    public boolean updateVideo(String set, String condition);
    public int getVideoCount(String condition);
    
    public VideoFileBean getVideoFile(String condition);
    public Vector getVideoFileList(String condition);
    public boolean updateVideoFile(String set, String condition);
    public int getVideoFileCount(String condition);
}