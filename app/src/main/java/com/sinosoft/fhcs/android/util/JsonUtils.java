package com.sinosoft.fhcs.android.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by doc on 2016/10/25.
 */

public class JsonUtils {
    private JsonUtils(){

    }

    /**
     * 对象转换成json字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }
    /**
     * 将获取到的json字符串转换为对象集合进行返回
     * @param jsonData 需要解析的json字符串
     * @param cls 类模板
     * @return
     */
    public static <T> List<T> getObjList(String jsonData, Class<T> cls){
        List<T> list = new ArrayList<T>();
        if (jsonData.startsWith("[") && jsonData.endsWith("]")) {//当字符串以“[”开始，以“]”结束时，表示该字符串解析出来为集合
            //截取字符串，去除中括号
            jsonData = jsonData.substring(1, jsonData.length() -1);
            //将字符串以"},"分解成数组
            String[] strArr = jsonData.split("\\},");
            //分解后的字符串数组的长度
            int strArrLength = strArr.length;
            //遍历数组，进行解析，将字符串解析成对象
            for (int i = 0; i < strArrLength; i++) {
                String newJsonString = null;
                if (i == strArrLength -1) {
                    newJsonString = strArr[i];
                } else {
                    newJsonString = strArr[i] + "}";
                }
                T bean = fromJson(newJsonString, cls);
                list.add(bean);
            }
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }
}
