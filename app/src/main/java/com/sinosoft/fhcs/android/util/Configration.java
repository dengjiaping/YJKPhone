package com.sinosoft.fhcs.android.util;

import com.sinosoft.fhcs.android.entity.Weather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Configration
{
    public Weather readInfo(InputStream a){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();  //取得SAXParserFactory实例
            SAXParser parser = factory.newSAXParser();
            MyHandler handler = new MyHandler();
            parser.parse(a, handler);
            a.close();
            return handler.getWeather();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(a != null){
                try {
                    a.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            if(a != null){
                try {
                    a.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }
    public String EcodingGB2312(String a) throws UnsupportedEncodingException {
        return URLEncoder.encode(a, "gb2312");
    }
    private class MyHandler extends DefaultHandler {
        private Weather weather=null;
        private String tagName = null;//当前解析的元素标签

        public Weather getWeather(){
            return weather;
        }

        /**
         * @see org.xml.sax.ContentHandler#startDocument()
         */
        @Override
        public void startDocument() throws SAXException {
            System.out.println("ERROR");
            weather = new Weather();
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
         */
        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            this.tagName=localName;

        }

        /**
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if(this.tagName!=null){
                String data=new String(ch,start,length);
                if(this.tagName.equals("city")){
                    weather.setCity(data);
                }else if(this.tagName.equals("temperature2")){
                    weather.setTemperature2(Integer.valueOf(data));
                }else if(this.tagName.equals("temperature1")){
                    weather.setTemperature1(Integer.valueOf(data));
                }else if(this.tagName.equals("status1")){
                    weather.setStatus1(data);
                }else if(this.tagName.equals("status2")){
                    weather.setStatus2(data);
                }else if(this.tagName.equals("direction1")){
                    weather.setDirection1(data);
                }else if(this.tagName.equals("direction2")){
                    weather.setDirection2(data);
                }else if(this.tagName.equals("power1")){
                    weather.setPower1(data);
                }else if(this.tagName.equals("power2")){
                    weather.setPower2(data);
                }else if(this.tagName.equals("xcz_s")){
                    weather.setXcz_s(data);
                }else if(this.tagName.equals("yd_s")){
                    weather.setYd_s(data);
                }else if(this.tagName.equals("gm")){
                    weather.setGm(data);
                }
            }

        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(String, String, String)
         */
        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            this.tagName = null;
        }
    }
}