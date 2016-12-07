/*
 * Created on 2005-12-13
 *
 */
package net.joycool.wap.cache;

import java.util.Vector;

import net.joycool.wap.bean.JaLineBean;

/**
 * @author lbj
 *  
 */
public class ColumnCache {
    int totalPageCount;

    int pageIndex;

    String prefixUrl;

    Vector blockList;

    JaLineBean column;

    String backTo;

    int isRoot;

    /**
     * @return Returns the backTo.
     */
    public String getBackTo() {
        return backTo;
    }

    /**
     * @param backTo
     *            The backTo to set.
     */
    public void setBackTo(String backTo) {
        this.backTo = backTo;
    }

    /**
     * @return Returns the blockList.
     */
    public Vector getBlockList() {
        return blockList;
    }

    /**
     * @param blockList
     *            The blockList to set.
     */
    public void setBlockList(Vector blockList) {
        this.blockList = blockList;
    }

    /**
     * @return Returns the column.
     */
    public JaLineBean getColumn() {
        return column;
    }

    /**
     * @param column
     *            The column to set.
     */
    public void setColumn(JaLineBean column) {
        this.column = column;
    }

    /**
     * @return Returns the isRoot.
     */
    public int getIsRoot() {
        return isRoot;
    }

    /**
     * @param isRoot
     *            The isRoot to set.
     */
    public void setIsRoot(int isRoot) {
        this.isRoot = isRoot;
    }

    /**
     * @return Returns the pageIndex.
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * @param pageIndex
     *            The pageIndex to set.
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return Returns the prefixUrl.
     */
    public String getPrefixUrl() {
        return prefixUrl;
    }

    /**
     * @param prefixUrl
     *            The prefixUrl to set.
     */
    public void setPrefixUrl(String prefixUrl) {
        this.prefixUrl = prefixUrl;
    }

    /**
     * @return Returns the totalPageCount.
     */
    public int getTotalPageCount() {
        return totalPageCount;
    }

    /**
     * @param totalPageCount
     *            The totalPageCount to set.
     */
    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
