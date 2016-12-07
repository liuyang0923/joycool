/*
 * Created on 2006-12-10
 *
 */
package net.wxsj.service.factory;

import net.wxsj.service.impl.FlushServiceImpl;
import net.wxsj.service.impl.MallServiceImpl;
import net.wxsj.service.impl.NewTestServiceImpl;
import net.wxsj.service.impl.StageServiceImpl;
import net.wxsj.service.impl.ZippoServiceImpl;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IFlushService;
import net.wxsj.service.infc.IMallService;
import net.wxsj.service.infc.INewTestService;
import net.wxsj.service.infc.IStageService;
import net.wxsj.service.infc.IZippoService;
import net.wxsj.util.db.DbOperation;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-10
 * 
 * 说明：
 */
public class ServiceFactory {
    public static IZippoService createZippoService() {
        return new ZippoServiceImpl(IBaseService.CONN_IN_METHOD, null);
    }

    public static IZippoService createZippoService(int useConnType,
            DbOperation dbOp) {
        return new ZippoServiceImpl(useConnType, dbOp);
    }
    
    public static INewTestService createNewTestService() {
        return new NewTestServiceImpl(IBaseService.CONN_IN_METHOD, null);
    }

    public static INewTestService createNewTestService(int useConnType,
            DbOperation dbOp) {
        return new NewTestServiceImpl(useConnType, dbOp);
    }    

    public static IMallService createMallService() {
        return new MallServiceImpl(IBaseService.CONN_IN_METHOD, null);
    }
    
    public static IMallService createMallService(int useConnType,
            DbOperation dbOp) {
        return new MallServiceImpl(useConnType, dbOp);
    }
    
    public static IStageService createStageService() {
        return new StageServiceImpl(IBaseService.CONN_IN_METHOD, null);
    }
    
    public static IStageService createStageService(int useConnType,
            DbOperation dbOp) {
        return new StageServiceImpl(useConnType, dbOp);
    }
    
    public static IFlushService createFlushService() {
        return new FlushServiceImpl(IBaseService.CONN_IN_METHOD, null);
    }
    
    public static IFlushService createFlushService(int useConnType,
            DbOperation dbOp) {
        return new FlushServiceImpl(useConnType, dbOp);
    }    
}
