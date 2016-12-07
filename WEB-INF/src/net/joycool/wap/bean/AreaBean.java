/*
 * Created on 2006-4-11
 *
 */
package net.joycool.wap.bean;

/**
 * @author lbj
 *  
 */
public class AreaBean {
    int placeno;

    String cityname;

    int cityno;

    /**
     * @return Returns the cityname.
     */
    public String getCityname() {
        return cityname;
    }

    /**
     * @param cityname
     *            The cityname to set.
     */
    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    /**
     * @return Returns the cityno.
     */
    public int getCityno() {
        return cityno;
    }

    /**
     * @param cityno
     *            The cityno to set.
     */
    public void setCityno(int cityno) {
        this.cityno = cityno;
    }

    /**
     * @return Returns the placeno.
     */
    public int getPlaceno() {
        return placeno;
    }

    /**
     * @param placeno
     *            The placeno to set.
     */
    public void setPlaceno(int placeno) {
        this.placeno = placeno;
    }
}
