package net.joycool.wap.service.infc;



public interface IStatisticService {

    public int getUserNumber(String date,String condition);
    public int getMobileNumber(String date,String condition);
    public int getNewUserNumber(String date,String cond);
    public int getWapUserNumber(String date,String condition);
    public int getWapMobileNumber(String date,String condition);
    public int getWapNewUserNumber(String date,String cond);
  
}
