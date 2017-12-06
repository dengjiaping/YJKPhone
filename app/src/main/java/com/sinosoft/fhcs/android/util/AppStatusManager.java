package com.sinosoft.fhcs.android.util;

/**
 * 作者：shuiq_000 on 2017/8/30 09:10
 * 邮箱：2028318192@qq.com
 */

public class AppStatusManager {
    public int appStatus= Constant.STATUS_FORCE_KILLED; //APP状态初始值为没启动不在前台状态
    public static AppStatusManager appStatusManager;
    private AppStatusManager() {

    }
    public static AppStatusManager getInstance() {
        if(appStatusManager==null){
            appStatusManager=new AppStatusManager();
        }
        return appStatusManager;
    }
    public int getAppStatus() {
        return appStatus;
    }
    public void setAppStatus(int appStatus) {
        this.appStatus= appStatus;
    }
}
