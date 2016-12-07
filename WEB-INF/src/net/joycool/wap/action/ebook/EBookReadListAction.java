/*
 * Created on 2005-12-7
 *
 */
package net.joycool.wap.action.ebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.ebook.EBookBean;
import net.joycool.wap.bean.ebook.FileNameBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *
 */
public class EBookReadListAction extends Action {
    String realUrl = Constants.EBOOK_FILE_URL;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ICatalogService catalogService = ServiceFactory.createCatalogService();
        File processFile;
        String filesName[];

        String idStr = request.getParameter("ebookId");
        int id;
        if (idStr != null) {
            id = Integer.parseInt(idStr);
        } else {
            return mapping.findForward("systemFailure");
        }
        
        //取得当前电子书及其所属类别
        IEBookService ebookService = ServiceFactory.createEBookService();
        String condition = "id = " + id ;
        EBookBean ebook = ebookService.getEBook(condition);
        if(ebook == null)
        	return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        int catalogId = ebook.getCatalogId();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = catalogService.getCatalog(buffCondition);
        if(catalog == null)
        	return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        
        //对该电子书所在的目录进行操作，得到下载文件或者是阅读文件。
        String fileUrl = realUrl + ebook.getFileUrl();
        //System.out.println("fileUrl="+fileUrl);
        processFile = this.getDirectorys(fileUrl, "txt");
        if (processFile == null) 
            return mapping.findForward("systemFailure");
        filesName = this.getFiles(fileUrl + "/" + processFile.getName(),"txt");
        if (filesName == null)
            return mapping.findForward("systemFailure");       
        
        //对文件名进行分析，得到文件信息。
        Vector fileNames = new Vector();
        
        int index = 0;  //阅读时得到章名的序号；
        String chapters[] = this.readInfo(fileUrl + "/" + processFile.getName());
        for (int i = 0; i < filesName.length; i++) {
            if (filesName[i] == null)
                continue;
            FileNameBean fileName = new FileNameBean();

            fileName.setId(index+1);
            fileName.setChapters(chapters[index++]);
            fileName.setAddress(filesName[i]);

            fileNames.add(fileName);
        }

        request.setAttribute("catalog",catalog);
        request.setAttribute("rootId",request.getParameter("rootId"));
        request.setAttribute("ebook",ebook);
        request.setAttribute("fileNames", fileNames);
        return mapping.findForward("success");
    }
    
    /*
     * get the appointed directory  under the directory;
     */
    private File getDirectorys(String directoryName, String destDirectory) {
        File currentFile = new File(directoryName);
        String allFiles[] = currentFile.list();//get the all files under the directory;
        if (allFiles != null) {
            for (int i = 0; i < allFiles.length; i++) {
            	// System.out.println("----allFiles["+i+"]="+allFiles[i]);
                if (allFiles[i].equals(destDirectory))
                    return new File(allFiles[i]);
            }
        }
        return null;
    }
    
    /*
     * get the appointed files name under the directory;
     */
    private String[] getFiles(String directoryName,String suffix) {
        File currentFile = new File(directoryName);
        String allFiles[] = currentFile.list();//get the all files under the
        									  // directory;
        if (allFiles != null) {
            String[] filesNeed = new String[allFiles.length];
           
            for (int i = 0; i < allFiles.length; i++) {
            	 //System.out.println("****allFiles["+i+"]="+allFiles[i]);
            	int _index = StringUtil.toInt(allFiles[i]);
                if (_index > 0 && _index <= filesNeed.length)  //get the needed file;
                {
                    filesNeed[_index - 1] = allFiles[i];//按顺序存放文件名。
                }
            }
            return filesNeed;
        } else
            return null;
    }
    
    
    /**
     * @param directoryName  
     * @return String[]  get the chapters'name from the inf.txt 
     * @throws Exception
     */
    private String[] readInfo(String directoryName) throws Exception{
    	//System.out.println("directoryName="+directoryName);
        File currentFile = new File(directoryName);
        String allFiles[] = currentFile.list();//get the all files under the
        									  // directory;
        if(allFiles != null){
            for(int i = 0;i<allFiles.length;i++){
            	//System.out.println("#####allFiles["+i+"]="+allFiles[i]);
                if(allFiles[i].equals("inf.txt")){
                    FileInputStream in = new FileInputStream(directoryName + "/" + "inf.txt");
                    InputStreamReader reader = new InputStreamReader(in,"UTF-8");
                    BufferedReader br = new BufferedReader(reader);
                    String buffer = br.readLine();
                    while (!buffer.startsWith("章名")&&buffer != null){
                        buffer = br.readLine();
                    }
                    if(buffer != null){
                        String[] chapters = buffer.split(",");
                        chapters[0] = chapters[0].substring(chapters[0].indexOf(":")+1);
                        return chapters;    
                    }
                }
            }
        }
        return null;
    }


}
