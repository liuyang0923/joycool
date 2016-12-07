/*
 * Created on 2006-1-12
 *
 */
package net.joycool.wap.bean.wgame;

/**
 * @author lbj
 *
 */
public class TigerBean {
    int wager;
    int[] results;
    int result;
    
    
    /**
     * @return Returns the result.
     */
    public int getResult() {
        return result;
    }
    /**
     * @param result The result to set.
     */
    public void setResult(int result) {
        this.result = result;
    }
    /**
     * @return Returns the results.
     */
    public int[] getResults() {
        return results;
    }
    /**
     * @param results The results to set.
     */
    public void setResults(int[] results) {
        this.results = results;
    }
    /**
     * @return Returns the wager.
     */
    public int getWager() {
        return wager;
    }
    /**
     * @param wager The wager to set.
     */
    public void setWager(int wager) {
        this.wager = wager;
    }
}
