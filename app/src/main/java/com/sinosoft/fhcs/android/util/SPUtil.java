package com.sinosoft.fhcs.android.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class SPUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private static SPUtil spUtil;

    private SPUtil(Context context) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance(Context context) {
        if (spUtil == null) {
            spUtil = new SPUtil(context);
        }
        return spUtil;

    }

    public void putString(String key, String value) {
        edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }
    public void remove(String ...key){
        edit = sp.edit();
        if(key != null){
            for(int i = 0;i<key.length;i++){
                edit.remove(key[i]);
            }
        };
        edit.commit();;
    }
    public void putLong(String key, Long value) {
        edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }
    public Long getLong(String key, Long defaultValue) {
        return sp.getLong(key, defaultValue);
    }
    
    public void putInt(String key, int value) {
        edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }
    
    public <T> void putObj(String key,T t){
        putString(key, bean2String(t));
    }
  
    public <T> T getObj(String key,T defaultValue){
    	T t = null;
    	String str = getString(key, "");
    	try {
			t = string2Bean(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return t;
    }
    
    public static <T> String bean2String(T t){
		String result = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream(); 
			oos = new ObjectOutputStream(baos); 
			oos.writeObject(t); 
			byte[] buf = baos.toByteArray(); 
			oos.flush();
			result = encodeBase64(buf);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(baos != null){
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(oos!=null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(result);
		return result;
	}
    
	public static<T> T string2Bean(String str){
		T result = null;
		ByteArrayInputStream bi = null;
		ObjectInputStream oi = null;
		try {
			// bytearray to object
			 bi = new ByteArrayInputStream(decodeBase64(str));
			 oi = new ObjectInputStream(bi);
			result = (T) oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("string2Bean error");
			
		}finally{
			if(bi != null){
				try {
					bi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(oi != null){
				try {
					oi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/***
	 * encode by Base64
	 */
	public static String encodeBase64(byte[]input) throws Exception{
		/*Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod= clazz.getMethod("encode", byte[].class);
		mainMethod.setAccessible(true);
		 Object retObj=mainMethod.invoke(null, new Object[]{input});*/
		return Base64.encodeToString(input, Base64.DEFAULT);
		 //return (String)retObj;
	}
	/***
	 * decode by Base64
	 */
	public static byte[] decodeBase64(String input) throws Exception{
		/*Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod= clazz.getMethod("decode", String.class);
		mainMethod.setAccessible(true);
		 Object retObj=mainMethod.invoke(null, input);*/
		// return (byte[])retObj;
		return Base64.decode(input, Base64.DEFAULT);
	}
}