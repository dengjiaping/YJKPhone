package com.sinosoft.fhcs.android.util;


import java.text.SimpleDateFormat;

/**
 * Created by fan on 2016/6/23.
 */
public class TimeUtils {

    //姣杞
    public static String long2String(long time){

        //姣杞
        int sec = (int) time / 1000 ;
        int min = sec / 60 ;	//鍒嗛挓
        sec = sec % 60 ;		//绉�
        if(min < 10){	//鍒嗛挓琛�0
            if(sec < 10){	//绉掕ˉ0
                return "0"+min+":0"+sec;
            }else{
                return "0"+min+":"+sec;
            }
        }else{
            if(sec < 10){	//绉掕ˉ0
                return min+":0"+sec;
            }else{
                return min+":"+sec;
            }
        }

    }

    /**
     * 杩斿洖褰撳墠鏃堕棿鐨勬牸寮忎负 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String  getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }


}
