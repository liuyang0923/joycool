/*
 * Created on 2006-2-16
 *
 */
package net.joycool.wap.bean.ring;

/**
 * @author mcq
 *
 */
public class PringFileBean {
     int id;
     int pring_id;
     int size;
     String file_type;
     String file;
     String linkUrl;

     /**
      * @return Returns the linkUrl.
      */
     public String getLinkUrl() {
         return linkUrl;
     }

     /**
      * @param linkUrl
      *            The linkUrl to set.
      */
     public void setLinkUrl(String linkUrl) {
         this.linkUrl = linkUrl;
     }
     
     /**
      * @return Returns the id.
      */
     public int getId() {
         return id;
     }

     /**
      * @param id
      * The id to set.
      */
     public void setId(int id) {
         this.id = id;
     }
     
     /**
      * @return Returns the pring_id.
      */
     public int getPring_id() {
         return pring_id;
     }

     /**
      * @param pring_id
      * The pring_id to set.
      */
     public void setPring_id(int pring_id) {
         this.pring_id = pring_id;
     }
     
     /**
      * @return Returns the size.
      */
     public int getsize() {
         return size;
     }

     /**
      * @param size
      * The size to set.
      */
     public void setSize(int size) {
         this.size = size;
     }
     
     /**
      * @return Returns the file_type.
      */
     public String getFile_type() {
         return file_type;
     }

     /**
      * @param file_type
      * The file_type to set.
      */
     public void setFile_type(String file_type) {
         this.file_type = file_type;
     }
     
     /**
      * @return Returns the file.
      */
     public String getFile() {
         return file;
     }

     /**
      * @param file
      * The file to set.
      */
     public void setFile(String file) {
         this.file = file;
     }
}
