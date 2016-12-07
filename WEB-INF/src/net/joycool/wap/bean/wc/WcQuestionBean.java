/*
 * Created on 2006-6-9
 *
 */
package net.joycool.wap.bean.wc;

/**
 * @author lbj
 *  
 */
public class WcQuestionBean {
    int id;

    String title;

    int result;

    String endDatetime;

    String createDatetime;

    /**
     * @return Returns the createDatetime.
     */
    public String getCreateDatetime() {
        return createDatetime;
    }

    /**
     * @param createDatetime
     *            The createDatetime to set.
     */
    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * @return Returns the endDatetime.
     */
    public String getEndDatetime() {
        if (endDatetime != null) {
            if (endDatetime.indexOf("-") != -1) {
                endDatetime = endDatetime
                        .substring(endDatetime.indexOf("-") + 1);
            }
            if (endDatetime.lastIndexOf(":") != -1) {
                endDatetime = endDatetime.substring(0, endDatetime
                        .lastIndexOf(":"));
            }
        }
        return endDatetime;
    }
    /**
     * fanys 2006-06-15 start
     * @return
     */
    public String getEndDatetime2(){
    	return endDatetime;
    }
    // fanys 2006-06-15 end

    /**
     * @param endDatetime
     *            The endDatetime to set.
     */
    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
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
     * @return Returns the result.
     */
    public int getResult() {
        return result;
    }

    /**
     * @param result
     *            The result to set.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
