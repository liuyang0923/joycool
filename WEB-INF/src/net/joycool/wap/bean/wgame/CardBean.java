/*
 * Created on 2006-1-11
 *
 */
package net.joycool.wap.bean.wgame;

/**
 * @author lbj
 *
 */
public class CardBean implements Comparable{
    int value;
    int type;
    String picUrl;
    boolean open;
    
    /**
     * @return Returns the open.
     */
    public boolean isOpen() {
        return open;
    }
    /**
     * @param open The open to set.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }
    public CardBean(){        
    }
    
    public CardBean(int value, int type, String picUrl){
        this.value = value;
        this.type = type;
        this.picUrl = picUrl;
    }
    /**
     * @return Returns the picUrl.
     */
    public String getPicUrl() {
        return picUrl;
    }
    /**
     * @param picUrl The picUrl to set.
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(int type) {
        this.type = type;
    }
    /**
     * @return Returns the value.
     */
    public int getValue() {
        return value;
    }
    
    public int getSHValue() {
        if(value == 1){
            return value + 13;
        }
        return value;
    }
    
    /**
     * @param value The value to set.
     */
    public void setValue(int value) {
        this.value = value;
    }
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    //比较牌的类型是否相等
	public int compareTo(Object o) {
		CardBean c = (CardBean) o;
    //等于0时代表庄家和挑战者的牌值大小相等，返回牌的类型相比
		if(this.getValue() - c.getValue() == 0){
			return this.getType() - c.getType();
		} else {
			return 1;
		}
	}        
}
