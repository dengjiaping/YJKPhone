package com.sinosoft.fhcs.android.util;

/**
 * @CopyRight: SinoSoft.
 * @Description: 网络请求类
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.entity.HealthRecord;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpManager {

//	public static String m_serverAddress = "http://www.yjkang.cn/";
	//	public static String m_serverAddress = "http://192.168.42.250:8080/portal/";//郭新明
//	public static String m_serverAddress = "http://192.168.11.30:8080/portal/";//郭新明
	public static String m_serverAddress = "http://124.127.116.152:8080/portal/";//研究院虚拟机
	public static String m_imageUrl = "http://www.yjkang.cn";
	public static final String weather_url_base = "http://php.weather.sina.com.cn/";
//	public static String m_imageUrl = "http://192.168.42.133:80/portal";


	//	 public static String m_serverAddress =
	//	 "http://www.yjkang.cn:9090/portal/";
	//	 public static String m_imageUrl = "http://www.yjkang.cn:9090/portal";

	// public static String m_serverAddress = "http://192.168.42.129:8080/portal/";
	//	 public static String m_imageUrl = "http://192.168.42.129:8080/portal";
	public static String miao_registration_url = "https://healthapitest.miaohealth.net/health-service-rest/v1/link/registration";//妙健康1.预约挂号接口
	public static String miao_medicine_url = "https://healthapitest.miaohealth.net/health-service-rest/v1/link/medicine";//妙健康2.用药评估接口
	public static String miao_diseaseEvaluate_url = "https://healthwebtest.miaohealth.net/test/servicestandard/src/html/diseaseEvaluate/diseaseEvaluate.html";//妙健康3.疾病评估接口
	public static String miao_shop_url_test = "https://plusmalltest.miaohealth.net/#/";//妙健康4.商城接口(测试)
	public static String miao_shop_url_classifya = "https://plusmalldev.miaohealth.net/#/classifya";//妙健康4.商城接口(测试)
	public static String miao_shop_url = "https://plusmall.miaohealth.net/#/";//妙健康4.商城接口(正式)

	// 咕咚
	public static String codoon_aouth = "http://api.codoon.com/authorize?";
	public static String codoon_appkey = "ba74847c8ca811e48ed700163e022745";
	public static String codoon_redirect_uri = m_serverAddress
			+ "home_home.action?";
	private static DataOutputStream outStream;
	private static FileInputStream is;
	private static InputStream in;
	private static BufferedReader reader;
	public static final int CODOON_AUTHORIZE_REQUEST_CODE = 111000;
	public static final String ACCESS_CODE = "code";

	private static final int outTime=30000;
	/**
	 * HttpGet请求
	 *
	 * @param url
	 * @param charset
	 * @return
	 * @throws ParseException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getStringContent(String url) {
		String result = "";
		HttpResponse httpResponse = null;
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, outTime);
			HttpConnectionParams.setSoTimeout(httpParams, outTime);
			// 新建HttpClient对象
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			// 发送请求，编写手机端传入参数
			HttpGet httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet);
			// 请求成功，解析数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			} else {
				result = "ERROR";
				Log.e("Error Response: ", httpResponse.getStatusLine()
						.toString());
			}
		} catch (Exception e) {
			result = "ERROR";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * HttpPost请求
	 *
	 * @param baseUrl
	 * @param params
	 * @return
	 */
	public static String getStringContentPost(String baseUrl,
											  Map<String, String> params) {
		String result = "";
		HttpResponse httpResponse = null;
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		String paramStr = "";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			paramStr = paramStr + "&" + name + "=" + value;
			NameValuePair param = new BasicNameValuePair(name, value);
			pairList.add(param);
		}
		String url = baseUrl + paramStr;
		Log.e("baseTVServer:", url);
		try {
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			// URL使用基本URL即可，其中不需要加参数
			HttpPost httpPost = new HttpPost(baseUrl);
			// 将请求体内容加入请求中
			httpPost.setEntity(requestHttpEntity);
			// 需要客户端对象来发送请求
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, outTime);
			// 请求超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, outTime);
			// 发送请求
			httpResponse = httpClient.execute(httpPost);
			// 请求成功，解析数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
			} else {
				result = "ERROR";
				Log.e("Error Response: ", httpResponse.getStatusLine()
						.toString());
			}
		} catch (Exception e) {
			result = "ERROR";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * HttpPost请求
	 *
	 * @param baseUrl
	 * @param params
	 * @return
	 */
	public static String getStringContentPost2(String baseUrl,
											   Map<String, String> params) {
		String result = "";
		HttpResponse httpResponse = null;
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		String paramStr = "?";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			paramStr = paramStr + name + "=" + value + "&";
			NameValuePair param = new BasicNameValuePair(name, value);
			pairList.add(param);
		}
		paramStr = paramStr.substring(0, paramStr.length() - 1);
		String url = baseUrl + paramStr;
		Log.e("baseTVServer:", url);
		try {
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			// URL使用基本URL即可，其中不需要加参数
			HttpPost httpPost = new HttpPost(baseUrl);
			// 将请求体内容加入请求中
			httpPost.setEntity(requestHttpEntity);
			// 需要客户端对象来发送请求
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, outTime);
			// 请求超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, outTime);
			// 发送请求
			httpResponse = httpClient.execute(httpPost);
			// 请求成功，解析数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
			} else {
				result = "ERROR";
				Log.e("Error Response: ", httpResponse.getStatusLine()
						.toString());
			}
		} catch (Exception e) {
			result = "ERROR";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Http 保存session
	 *
	 * @param url
	 * @param sessionID
	 * @return
	 * @throws IOException
	 */
	public static String phoneSessionFromGet(String url, String sessionID) {
		String result = "";
		try {
			URL getUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) getUrl
					.openConnection();
			if (!sessionID.equals("")) {
				connection.addRequestProperty("Cookie", "JSESSIONID="
						+ sessionID);
			}
			connection.setConnectTimeout(outTime);
			connection.setReadTimeout(outTime);
			connection.setRequestMethod("GET");
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			if (sessionID.equals("")) {
				System.out.println("cc"
						+ connection.getHeaderField("Set-Cookie"));
				String value = connection.getHeaderField("Set-Cookie");
				if (value != null) {
					String cookie = value.substring(0, value.indexOf(";"));
					int pos = cookie.indexOf('=');
					if (pos > 0) {
						System.out.println("aa" + cookie.substring(0, pos));
						System.out.println("bb" + cookie.substring(pos + 1));
						Constant.sessionIdPhone = cookie.substring(pos + 1);
					} else {
						System.out.println("cc"
								+ connection.getHeaderField("Set-Cookie"));
					}
				}

			}
			String lines;
			while ((lines = reader.readLine()) != null) {
				System.out.println(lines);
				result = lines;
			}
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			result = "ERROR";
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 上传图片
	 *
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 */
	public static String postImage(String url, Map<String, String> params,
								   Map<String, File> files) {
		String result = "";
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		URL uri;
		try {
			uri = new URL(url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) uri.openConnection();
			conn.setConnectTimeout(outTime);
			conn.setReadTimeout(outTime); // 缓存的最长时间
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
			conn.connect();
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			// 发送文件数据
			if (files != null)
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\""
							+ file.getKey() + "\"; filename=\""
							+ file.getValue().getName() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());

					is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			in = conn.getInputStream();
			if (res == 200) {
				StringBuilder sb2 = new StringBuilder();
				int ch;
				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
				result = sb2.toString();
			} else {
				result = "ERROR";
			}
			outStream.close();
			conn.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "ERROR";
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(outStream != null){
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return result;
	}

	// 判断网络是否可用
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 登录
	 *
	 * @param username
	 *            邮箱+手机号
	 * @param password
	 *            密码长度6-32
	 * @return tags：恒为 yjkang_sys deviceCode:设备类型(100001=android,100002=ios)
	 */
	public static String urlLogin(String username, String password,
								  String registrationID) {
		String str = m_serverAddress + "stblogin_login.action?";
		str += ("loginName=" + username + "&password=" + password
				+ "&registrationID=" + registrationID + "&tags=yjkang_sys,yjkang_android_sys&deviceCode=100001");
		return str;
	}
	/**
	 * @Description:  第三方登录
	 * @Author: cuihao
	 * @Date: 2016-8-1 下午3:34:42
	 * platformName：平台的名字：qq或微信
	 * uid：标志用户的uid
	 * nickName：用户昵称
	 * headIconUrl：头像地址
	 * registrationID：极光推送的注册id
	 * tags=yjkang_sys,yjkang_android_sys&deviceCode=100001：android登录之后的消息推送
	 */
	public static String urlThridLogin(String username, String password,String platformName,String uid,String nickName,String headIconUrl,String registrationID){
		String str = m_serverAddress+"stblogin_thirdPartyLogin.action?";
		str += ("loginName=" + username + "&password=" + password
				+ "&platformName=" + platformName + "&uid=" + uid+ "&nickName=" + nickName+ "&headIconUrl=" + headIconUrl
				+ "&registrationID=" + registrationID + "&tags=yjkang_sys,yjkang_android_sys&deviceCode=100001");
		return str;
	}

	/**
	 * 获取验证码
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static String urlYZM(String phoneNumber) {
		String str = m_serverAddress + "stblogin_captcha.action?";
		str += ("phoneNumber=" + phoneNumber);
		return str;
	}
	/**
	 * 获取验证码不需要判断是否注册过（第三方登录）
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static String urlYZM2(String phoneNumber) {
		String str = m_serverAddress + "stblogin_thirdPartyCaptcha.action?";
		str += ("phoneNumber=" + phoneNumber);
		return str;
	}

	/**
	 * 注册
	 *
	 * @param captchaNo
	 * @param phoneNumber
	 * @param nickName
	 * @param password
	 * @param provinceId
	 * @param cityId
	 * @param areaId
	 * @return
	 */
	public static String urlRegister(String phoneNumber, String nickName,
									 String password, String provinceId, String cityId, String areaId,
									 String gender, String birthday, String familyMemberRoleName,
									 String weight, String height, String stepSize, String waist) {
		String str = m_serverAddress + "stblogin_registe.action?";
		String name = "";
		String roleName = "";
		try {
			name = URLEncoder.encode(nickName, "utf-8");
			roleName = URLEncoder.encode(familyMemberRoleName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str += ("&phoneNumber=" + phoneNumber + "&nickName=" + name
				+ "&password=" + password);
		if (!provinceId.equals("")) {
			str += "&provinceId=" + provinceId;
		}
		if (!cityId.equals("")) {
			str += "&cityId=" + cityId;
		}
		if (!areaId.equals("")) {
			str += "&areaId=" + areaId;
		}
		str += ("&gender=" + gender + "&birthday=" + birthday
				+ "&familyMemberRoleName=" + roleName + "&weight=" + weight
				+ "&height=" + height + "&stepSize=" + stepSize + "&waist=" + waist);
		return str;
	}
	/**
	 * 新的注册
	 * @param phoneNumber
	 * @param password
	 * @param gender
	 * @param birthday
	 * @param familyMemberRoleName
	 * @param weight
	 * @param height
	 * @param stepSize
	 * @param waist
	 * @param verifyCode
	 * @return
	 */
	public static String urlRegisterNew(String phoneNumber, String password,
										String gender, String birthday, String familyMemberRoleName,
										String weight, String height, String stepSize, String waist,
										String verifyCode) {
		String str = m_serverAddress + "stblogin_registe.action?";
		String roleName = "";
		try {
			roleName = URLEncoder.encode(familyMemberRoleName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str += ("&phoneNumber=" + phoneNumber + "&password=" + password
				+ "&gender=" + gender + "&birthday=" + birthday
				+ "&familyMemberRoleName=" + roleName + "&weight=" + weight
				+ "&height=" + height + "&stepSize=" + stepSize + "&waist="
				+ waist + "&verifyCode=" + verifyCode);
		return str;
	}
	/**
	 * 通过此方法设置第三登录，设置familId
	 * @Description:
	 * @Author: cuihao
	 * @Date: 2016-8-3 下午5:18:43
	 */
	public static String urlRegisterNewThird(String gender, String birthday, String familyMemberRoleName,
											 String weight, String height, String stepSize, String waist,
											 String id) {
		String str = m_serverAddress + "stblogin_thirdPartySetFamilyMember.action?";
		String roleName = "";
		try {
			roleName = URLEncoder.encode(familyMemberRoleName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str += ("&gender=" + gender + "&birthday=" + birthday
				+ "&familyMemberRoleName=" + roleName + "&weight=" + weight
				+ "&height=" + height + "&stepSize=" + stepSize + "&waist="
				+ waist + "&id=" + id);
		return str;
	}

	/**
	 * 提醒列表主页
	 *
	 * @param userId
	 * @return
	 */
	public static String urlRemindHome(String userId) {
		String str = m_serverAddress + "medicine_getMedicineReminder.action?";
		str += ("userId=" + userId);
		return str;
	}

	/**
	 * 单个成员的提醒列表
	 *
	 * @param familyMemberId
	 * @param pageNum
	 * @return
	 */
	public static String urlRemindList(String familyMemberId, int pageNum) {
		String str = m_serverAddress
				+ "medicine_getAllMedicineReminderById.action?";
		str += ("familyMemberId=" + familyMemberId + "&pageNum=" + pageNum + "&pageSize=5");
		return str;
	}

	/**
	 * 所有成员的提醒
	 *
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public static String urlRemindHomeNew(String userId, int pageNum,
										  int pageSize) {
		String str = m_serverAddress + "rest/medicine/reminder/list/";
		str += (userId + "?pageNum=" + pageNum + "&pageSize=" + pageSize);
		return str;
	}

	/**
	 * 删除提醒
	 *
	 * @param id
	 * @return
	 */
	public static String urlDeleteRemind(String id) {
		String str = m_serverAddress + "medicine_deleteById.action?";
		str += ("id=" + id);
		return str;
	}

	/**
	 * 添加提醒
	 *
	 * @param userId
	 * @param familyMemberId
	 * @param startTime
	 * @param endTime
	 * @param reminderTime
	 * @param medicineName
	 * @param dosage
	 * @param reminderWay
	 * @param phoneNumber
	 * @param reminderByMeal
	 * @return
	 */
	public static String urlAddRemind(String userId, String familyMemberId,
									  String startTime, String endTime, String reminderTime,
									  String medicineName, String dosage, String reminderWay,
									  String reminderByMeal) {
		String str = m_serverAddress + "medicine_addMedicineReminder.action?";
		str += ("userId=" + userId + "&familyMemberId=" + familyMemberId
				+ "&startTime=" + startTime + "&endTime=" + endTime
				+ "&reminderTime=" + reminderTime + "&medicineName="
				+ medicineName + "&dosage=" + dosage + "&reminderWay="
				+ reminderWay + "&reminderByMeal=" + reminderByMeal);
		return str;
	}

	/**
	 * 修改提醒
	 *
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param reminderTime
	 * @param medicineName
	 * @param dosage
	 * @param reminderWay
	 * @param phoneNumber
	 * @param reminderByMeal
	 * @return
	 */
	public static String urlEditRemind(String id, String startTime,
									   String endTime, String reminderTime, String medicineName,
									   String dosage, String reminderWay, String reminderByMeal) {
		String str = m_serverAddress + "medicine_editMedicineReminder.action?";
		str += ("id=" + id + "&startTime=" + startTime + "&endTime=" + endTime
				+ "&reminderTime=" + reminderTime + "&medicineName="
				+ medicineName + "&dosage=" + dosage + "&reminderWay="
				+ reminderWay + "&reminderByMeal=" + reminderByMeal);
		return str;
	}

	/**
	 * 绑定手机号
	 *
	 * @param captchaNo
	 * @param phoneNumber
	 * @param userId
	 * @return
	 */
	public static String urlPhone(String captchaNo, String phoneNumber,
								  String userId) {
		String str = m_serverAddress + "stbUser_bindPhoneNumber.action?";
		str += ("captchaNo=" + captchaNo + "&phoneNumber=" + phoneNumber
				+ "&id=" + userId);
		return str;
	}
	/**
	 * 绑定手机号但不保存，只做验证手机号
	 *
	 * @param captchaNo
	 * @param phoneNumber
	 * @param userId
	 * @return
	 */
	public static String thirdPartyBindPhone(String captchaNo, String phoneNumber,
											 String userId) {
		String str = m_serverAddress + "stbUser_thirdPartyBindPhoneNumber.action?";
		str += ("captchaNo=" + captchaNo + "&phoneNumber=" + phoneNumber
				+ "&id=" + userId);
		return str;
	}

	/**
	 * 录入资料
	 *
	 * @param device
	 * @param familyMemberRoleName
	 * @param measureTime
	 * @param weight体重
	 * @param bloodGlucose血糖
	 * @param stepNum步数
	 * @param distance距离
	 * @param calorie卡路里
	 * @param temperature体温
	 * @param diastolicPressure低压
	 * @param systolicPressure高压
	 * @param pulse脉搏
	 * @param moistureRate水分率
	 * @param fatPercentage脂肪率
	 * @param visceralFatRating内脏脂肪等级
	 * @param muscleVolume肌肉量
	 * @param beforOrAfter
	 *            //0-餐前 1-餐后
	 * @param startSleepTime入睡时间
	 * @param endSleepTime醒来时间
	 * @return private int origin = 0;// 数据来源 0-机顶盒测量,1-手动输入
	 */
	public static String urlInputInfo(int device, String userID,
									  String familyMemberRoleName, String measureTime, String weight,
									  String bloodGlucose, String stepNum, String distance,
									  String calorie, String temperature, String diastolicPressure,
									  String systolicPressure, String pulse, String moistureRate,
									  String fatPercentage, String visceralFatRating,
									  String muscleVolume, int beforOrAfter, String startSleepTime,
									  String endSleepTime) {
		String str = m_serverAddress + "stbhealth_manual.action?";
		switch (device) {
			case 4000101:
				// 健康称 4000101
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&measureTime=" + measureTime + "&weight=" + weight + "&origin=1");
				break;
			case 4000102:
				// 脂肪仪 4000102
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&measureTime=" + measureTime + "&moistureRate="
						+ moistureRate + "&fatPercentage=" + fatPercentage
						+ "&visceralFatRating=" + visceralFatRating
						+ "&muscleVolume=" + muscleVolume + "&origin=1");
				break;

			case 4000103:
				// 血压计 4000103
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&measureTime=" + measureTime + "&diastolicPressure="
						+ diastolicPressure + "&systolicPressure="
						+ systolicPressure + "&pulse=" + pulse + "&origin=1");

				break;
			case 4000104:
				// 血糖仪 4000104
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&measureTime=" + measureTime + "&bloodGlucose="
						+ bloodGlucose + "&beforOrAfter=" + beforOrAfter + "&origin=1");
				break;

			case 4000105:
				// 耳温枪 4000105
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&measureTime=" + measureTime + "&temperature="
						+ temperature + "&origin=1");
				break;

			case 4000106:
				// 计步器 4000106
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&measureTime=" + measureTime + "&stepNum=" + stepNum
						+ "&distance=" + distance + "&calorie=" + calorie + "&origin=1");
				break;
			case 4000107:
				// 睡眠 4000107
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&startSleepTime=" + startSleepTime + "&endSleepTime="
						+ endSleepTime + "&origin=1");
				break;
			default:
				// 健康称 4000101
				str += ("device=" + device + "&familyMemberRoleName="
						+ familyMemberRoleName + "&userID=" + userID
						+ "&measureTime=" + measureTime + "&weight=" + weight + "&origin=1");
				break;
		}

		return str;
	}

	/**
	 *
	 * @param device
	 * @param deepSleepMinutes 深睡时长
	 * @param totalSleepMinutes 睡眠总时长
	 * @param lightSleepMinutes 浅睡
	 * @param activityCalories  卡路里
	 * @param activitySteps     步数
	 * @param activityMinutes   运动时间
	 * @param heartRate         心率
	 * @param familyMemeberId   用户家庭id
	 * @return
	 */
	public static String urlShouHuanData(int device,int deepSleepMinutes, int totalSleepMinutes,
										 int lightSleepMinutes,int activityCalories ,int activitySteps, int activityMinutes,
										 int heartRate, String familyMemeberId) {
		String str = m_serverAddress + "rest/watch/upload/data/sleep?";
		switch (device) {

			case 4000107:
				// 睡眠 4000107
				str += ("deepSleepMinutes=" + deepSleepMinutes + "&totalSleepMinutes="
						+ totalSleepMinutes + "&lightSleepMinutes=" + lightSleepMinutes
						+ "&heartRate=" + heartRate + "&familyMemeberId=" + familyMemeberId);
				break;
			case 4000106:
				// 计步器 4000106
				str += ("activityCalories=" + activityCalories + "&activitySteps="
						+ activitySteps + "&activityMinutes=" + activityMinutes+ "&heartRate=" + heartRate);
				break;
			default:
				// 睡眠 4000101
				str += ("deepSleepMinutes=" + deepSleepMinutes + "&totalSleepMinutes="
						+ totalSleepMinutes + "&lightSleepMinutes=" + lightSleepMinutes
						+ "&heartRate=" + heartRate + "&familyMemeberId=" + familyMemeberId);
				break;
		}
		return str;
	}
	/**
	 *
	 * @return
	 */
	public static String urlShouHuanData2(int device) {
		String str = m_serverAddress;
		switch (device) {
			case 4000107:
				// 睡眠 4000107
				str += "rest/watch/upload/data/sleep?";
				break;
			case 4000106:
				// 计步器 4000106
				str += "rest/watch/upload/data/activity?";
				break;
			default:
				// 睡眠 4000107
				str += "rest/watch/upload/data/sleep?";
				break;
		}
		return  str;
	}

	/**
	 * 绑定机顶盒编号
	 *
	 * @param stbSerialNo
	 * @param userId
	 * @return
	 */
	public static String urlJiDingHe(String userID, String stbSerialNo) {
		String str = m_serverAddress + "stbUser_bindstb.action?";
		str += ("id=" + userID + "&stbSerialNo=" + stbSerialNo);
		return str;
	}

	/**
	 * 保存个人信息
	 *
	 * @param id
	 *            :9448
	 * @param avatar
	 *            =File文件
	 * @param provinceId
	 *            = 省代码
	 * @param cityId
	 *            = 市代码
	 * @param areaId
	 *            = 县代码
	 * @param nickName
	 *            = 昵称
	 */
	public static String urlSaveInfo = m_serverAddress
			+ "stbUser_portraitEdit.action?";
	public static String urlSaveInfo2 = m_serverAddress
			+ "stbUser_thirdPartySavePersonal.action?";

	// 不修改头像
	public static String urlSaveInfo2(String userID, String provinceId,
									  String cityId, String areaId, String nickName, String sign) {
		String str = m_serverAddress + "stbUser_portraitEdit.action?";
		str += "id=" + userID;
		if (!provinceId.equals("")) {
			str += "&provinceId=" + provinceId;
		}
		if (!cityId.equals("")) {
			str += "&cityId=" + cityId;
		}
		if (!areaId.equals("")) {
			str += "&areaId=" + areaId;
		}
		str += ("&nickName=" + nickName + "&sign=" + sign);
		System.out.println("str=" + str);
		return str;
	}
	// 不修改头像（第三方登录）
	public static String urlSavePersonal(String userID, String phoneNumber,String provinceId,
										 String cityId, String areaId, String nickName, String sign) {
		String str = m_serverAddress + "stbUser_thirdPartySavePersonal.action?";
		str += "id=" + userID;
		str += "&phoneNumber=" + phoneNumber;
		if (!provinceId.equals("")) {
			str += "&provinceId=" + provinceId;
		}
		if (!cityId.equals("")) {
			str += "&cityId=" + cityId;
		}
		if (!areaId.equals("")) {
			str += "&areaId=" + areaId;
		}
		str += ("&nickName=" + nickName + "&sign=" + sign);
		System.out.println("str=" + str);
		return str;
	}

	//上传头像并保存信息
	public static String postImage2(String url, Map<String, String> params,
									Map<String, File> files) {
		String result = "";
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		URL uri;
		try {
			uri = new URL(url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) uri.openConnection();
			conn.setConnectTimeout(outTime);
			conn.setReadTimeout(outTime); // 缓存的最长时间
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
			conn.connect();
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			// 发送文件数据
			if (files != null)
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\""
							+ file.getKey() + "\"; filename=\""
							+ file.getValue().getName() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());

					is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			in = conn.getInputStream();
			if (res == 200) {
				StringBuilder sb2 = new StringBuilder();
				int ch;
				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
				result = sb2.toString();
			} else {
				result = "ERROR";
			}
			outStream.close();
			conn.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "ERROR";
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(outStream != null){
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return result;
	}






	/**
	 * 健康咨讯类别
	 */
	// public static String urlInfoType = m_serverAddress
	// + "stb_getDict.action?type=11";
	public static String urlInfoType(String userId, String cooperateIdentify) {
		String str = m_serverAddress
				+ "stb_getDictForHealthInformation.action?";
		str += ("userId=" + userId + "&cooperateIdentify=" + cooperateIdentify + "&type=11");
		return str;
	}

	/**
	 * 单个成员的健康咨讯类别
	 */
	public static String urlInfoTypeByMemberId(String userId,
											   String cooperateIdentify, String familyMemberId) {
		String str = m_serverAddress
				+ "stb_getDictForHealthInformation.action?";
		str += ("userId=" + userId + "&cooperateIdentify=" + cooperateIdentify
				+ "&type=11" + "&familyMemberId=" + familyMemberId);
		return str;
	}

	/**
	 * 健康咨讯二级+详情
	 *
	 * @param userId
	 * @param informationType
	 * @return
	 */
	public static String urlInfoDetails(String userId, String informationType,
										String cooperateIdentify) {
		String str = m_serverAddress
				+ "stbInformation_getSecondInformation.action?";
		str += ("userId=" + userId + "&informationType=" + informationType
				+ "&cooperateIdentify=" + cooperateIdentify);
		return str;
	}

	/**
	 * 单个成员的健康咨讯二级+详情
	 *
	 * @param userId
	 * @param informationType
	 * @param familyMemberId
	 * @return
	 */
	public static String urlInfoDetailsOne(String userId,
										   String informationType, String familyMemberId,
										   String cooperateIdentify) {
		String str = m_serverAddress
				+ "stbInformation_getSecondInformation.action?";
		str += ("userId=" + userId + "&informationType=" + informationType
				+ "&familyMemberId=" + familyMemberId + "&cooperateIdentify=" + cooperateIdentify);
		return str;
	}

	/**
	 * 删除资讯
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	public static String urlInfoDelete(String userId, String id) {
		String str = m_serverAddress + "stbInformation_deleteById.action?";
		str += ("userId=" + userId + "&id=" + id);
		return str;
	}

	/**
	 * 全部已读
	 *
	 * @param userId
	 * @return
	 */
	public static String urlInfoIsAllRead(String userId) {
		String str = m_serverAddress + "stbInformation_editToRead.action?";
		str += ("userId=" + userId);
		return str;
	}

	/**
	 * 单个成员的全部已读
	 *
	 * @param userId
	 * @param familyMemberId
	 * @return
	 */
	public static String urlInfoIsAllReadMember(String userId,
												String familyMemberId) {
		String str = m_serverAddress + "stbInformation_editToRead.action?";
		str += ("userId=" + userId + "&familyMemberId=" + familyMemberId);
		return str;
	}

	/**
	 * 单条已读
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	public static String urlInfoIsOneRead(String userId, String id) {
		String str = m_serverAddress + "stbInformation_onceRead.action?";
		str += ("userId=" + userId + "&id=" + id);
		return str;
	}

	/**
	 * 健康档案
	 *
	 * @param userID
	 * @param familyMemberRoleName
	 * @param searchDate
	 *            2014-05-21
	 * @param device
	 *            4000101
	 * @return
	 */
	public static String urlHealthRecord(String userID,
										 String familyMemberRoleName, String searchDate, int device) {
		String str = m_serverAddress + "stbhealth_getHealth.action?";
		if (searchDate.equals("")) {
			str += ("userID=" + userID + "&familyMemberRoleName="
					+ familyMemberRoleName + "&device=" + device);
		} else {
			str += ("userID=" + userID + "&familyMemberRoleName="
					+ familyMemberRoleName + "&searchDate=" + searchDate
					+ "&device=" + device);
		}

		return str;
	}

	/**
	 * 最近6次健康档案
	 *
	 * @param userID
	 * @param familyMemberRoleName
	 * @param device
	 * @return
	 */
	public static String urlHealthRecord2(String userID,
										  String familyMemberRoleName, int device) {
		String str = m_serverAddress + "stbhealth_getHealth.action?";
		str += ("userID=" + userID + "&familyMemberRoleName="
				+ familyMemberRoleName + "&device=" + device + "&searchSize=6");

		return str;
	}

	/**
	 * 家庭列表
	 *
	 * @param userId
	 * @return
	 */
	public static String urlFamilyList(String userId) {
		String str = m_serverAddress
				+ "stbFamilyMember_searchFamilyMember.action?";
		str += ("userId=" + userId);

		return str;
	}

	/**
	 * 获取验证码（添加成员）
	 *
	 * @param mobile
	 * @return
	 */
	public static String urlYZMMember(String mobile) {
		String str = m_serverAddress + "rest/sms/sendsms?";
		str += ("mobile=" + mobile);
		return str;
	}

	/**
	 * 获取验证码（注册）
	 *
	 * @param mobile
	 * @return
	 */
	public static String urlYZMRegister(String mobile) {
		String str = m_serverAddress + "rest/sms/user/sendsms?";
		str += ("mobile=" + mobile);
		return str;
	}

	/**
	 * 校验验证码
	 *
	 * @param mobile
	 * @param verifyCode
	 * @return
	 */
	public static String urlJiaoYanYZM(String mobile, String verifyCode) {
		String str = m_serverAddress + "rest/sms/verify?";
		str += ("mobile=" + mobile + "&verifyCode=" + verifyCode);
		return str;
	}

	/**
	 * 添加家庭成员
	 *
	 * @param userId
	 *            用户id
	 * @param gender
	 *            0-女 1-男
	 * @param birthday
	 *            1988-05-21
	 * @param familyMemberRoleName
	 * @param weight
	 * @param height
	 * @param stepSize
	 * @param waist
	 * @return
	 */
	public static String urlAddFamilyMember(String userId, String gender,
											String birthday, String familyMemberRoleName, String weight,
											String height, String stepSize, String waist, String mobile) {
		String str = m_serverAddress
				+ "stbFamilyMember_addFamilyMember.action?";
		str += ("userId=" + userId + "&gender=" + gender + "&birthday="
				+ birthday + "&familyMemberRoleName=" + familyMemberRoleName
				+ "&weight=" + weight + "&height=" + height + "&stepSize="
				+ stepSize + "&waist=" + waist + "&mobile=" + mobile);
		return str;
	}

	/**
	 * 修改家庭成员
	 *
	 * @param userId
	 * @param id
	 * @param gender
	 * @param birthday
	 *            1988-05-21
	 * @param weight
	 * @param height
	 * @param stepSize
	 * @param waist
	 * @return
	 */
	public static String urlEditFamilyMember(String userId, long id,
											 String gender, String birthday, String weight, String height,
											 String stepSize, String waist, String mobile) {
		String str = m_serverAddress
				+ "stbFamilyMember_editFamilyMember.action?";
		str += ("userId=" + userId + "&id=" + id + "&gender=" + gender
				+ "&birthday=" + birthday + "&weight=" + weight + "&height="
				+ height + "&stepSize=" + stepSize + "&waist=" + waist
				+ "&mobile=" + mobile);
		return str;
	}

	/**
	 * 删除家庭成员
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	public static String urlDeleteFamilyMember(String userId, long id) {
		String str = m_serverAddress
				+ "stbFamilyMember_deleFamilyMember.action?";
		str += ("userId=" + userId + "&id=" + id);

		return str;
	}

	/**
	 * 版本更新
	 *
	 * @param reqType
	 *            reqType=700001 机顶盒reqType=700002 安卓手机
	 * @return
	 */
	public static String urlCheckApk(String reqType) {
		String str = m_serverAddress + "version_getVersion.action?";
		str += ("reqType=" + reqType);

		return str;
	}

	/**
	 * 通知设定
	 *
	 * @param id
	 *            userId
	 * @param informationPush
	 * @param medicinePush
	 * @return
	 */
	public static String urlNotify(String id, int informationPush,
								   int medicinePush) {
		String str = m_serverAddress + "stbUser_noticeEdit.action?";
		str += ("id=" + id + "&informationPush=" + informationPush
				+ "&medicinePush=" + medicinePush);

		return str;
	}

	/**
	 * 获取通知设定状态
	 *
	 * @param id
	 *            userId
	 * @return
	 */
	public static String urlNotifyState(String id) {
		String str = m_serverAddress + "stbUser_getNoticeStatus.action?";
		str += ("id=" + id);
		return str;
	}

	/**
	 * 获取消息列表
	 *
	 * @param userId
	 * @type 1==>服药提醒2==>健康消息3==>系统消息
	 * @return
	 */
	public static String urlSysMsg(String userId, int type) {
		String str = m_serverAddress + "stbHisttory_getHistory.action?";
		str += ("userId=" + userId + "&type=" + type + "&deviceCode=100001");
		return str;
	}

	/**
	 * 删除单条消息
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	public static String urlMsgDel(String userId, String id) {
		String str = m_serverAddress + "stbHisttory_deleteHistory.action?";
		str += ("userId=" + userId + "&id=" + id);
		return str;
	}

	/**
	 * 单条已读消息
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	public static String urlMsgReadOne(String userId, String id) {
		String str = m_serverAddress + "stbHisttory_onceRead.action?";
		str += ("userId=" + userId + "&id=" + id);
		return str;
	}

	/**
	 * 全部已读消息
	 *
	 * @param userId
	 * @type 1==>服药提醒2==>健康消息3==>系统消息
	 * @return
	 */
	public static String urlMsgReadAll(String userId, int type) {
		String str = m_serverAddress + "stbHisttory_allRead.action?";
		str += ("userId=" + userId + "&type=" + type);
		return str;
	}

	/**
	 * 单个成员最近一次的健康档案（所有设备）
	 *
	 * @param userId
	 * @param familyMemberId
	 * @return
	 */
	public static String urlMemberRecordList(String userId,
											 String familyMemberId) {
		String str = m_serverAddress + "stbhealth_getLastHealth.action?";
		str += ("userID=" + userId + "&familyMemberId=" + familyMemberId);
		return str;
	}

	/**
	 * 退出到登陆
	 *
	 * @param userId
	 * @return
	 */
	public static String urlExit(String userId) {
		String str = m_serverAddress + "stblogin_logout.action?";
		str += ("id=" + userId);
		return str;
	}

	/**
	 * 个人信息
	 *
	 * @param userId
	 * @return
	 */
	public static String urlPersonalInfo(String userId) {
		String str = m_serverAddress + "stbUser_getInfo.action?";
		str += ("id=" + userId);
		return str;
	}

	/**
	 * 获取目标
	 *
	 * @param familyMemberId
	 * @return
	 */
	// http://172.16.0.55:8080/portal/rest/app/getFamilyMemberGoal?familyMemberId=1007402
	public static String urlGetGoalInfo(String familyMemberId) {
		String str = m_serverAddress + "rest/app/getFamilyMemberGoal?";
		str += ("familyMemberId=" + familyMemberId);
		return str;

	}

	/**
	 * 提交目标，参数为familyMemberId=1007402&goalDeepMinutes=360&goalSteps=10000&
	 * goalWeight=60
	 *
	 * @return
	 */
	public static String urlCommitGoalInfo() {
		String str = m_serverAddress + "rest/app/ditFamilyMemberGoal?";
		return str;

	}

	/**
	 * 绑定设备的获取支持设备接口
	 *
	 * @param userId
	 * @return
	 */
	public static String urlGetBindEquipment() {
		String str = m_serverAddress + "rest/app/getHandRingDevice";
		return str;
	}

	/**
	 * 设备管理的获取设备列表接口
	 *
	 * @param userId
	 * @return
	 */
	// public static String urlGetEquipmentList(String userId) {
	// String str = m_serverAddress + "rest/app/getHandRingList?";
	// str += ("userId=" + userId + "&serialNo=");
	// return str;
	// }
	public static String urlGetEquipmentList(String userId) {
		String str = m_serverAddress + "rest/app/v2/getHandRingList?";
		str += ("userId=" + userId + "&serialNo=");
		return str;
	}

	/**
	 * 创建竞赛
	 *
	 * @param userId
	 * @param number
	 * @param startTime
	 * @param endTime
	 * @param model
	 * @param type
	 * @param manifesto
	 * @return
	 */
	public static String urlAddCompetition(String userId, String number,
										   String startTime, String endTime, String model, String type,
										   String manifesto) {
		String str = m_serverAddress + "rest/app/addRace?";
		str += ("userId=" + userId + "&number=" + number + "&startTime="
				+ startTime + "&endTime=" + endTime + "&model=" + model
				+ "&type=" + type + "&manifesto=" + manifesto);
		return str;
	}

	/**
	 * 创建竞赛并邀请好友加入接口
	 *
	 * @param userId
	 * @param friendId
	 * @param number
	 * @param startTime
	 * @param endTime
	 * @param model
	 * @param type
	 * @param manifesto
	 * @return
	 */
	public static String urlAddRaceForFriend(String userId, String friendId,
											 String number, String startTime, String endTime, String model,
											 String type, String manifesto) {
		String str = m_serverAddress + "rest/app/addRaceForFriend?";
		str += ("userId=" + userId + "&friendId=" + friendId + "&number="
				+ number + "&startTime=" + startTime + "&endTime=" + endTime
				+ "&model=" + model + "&type=" + type + "&manifesto=" + manifesto);
		return str;
	}

	/**
	 * 获取竞赛列表
	 *
	 * @param userId
	 * @param pageNum
	 * @return
	 */
	public static String urlGetCompetitionList(String userId, int pageNum) {
		String str = m_serverAddress + "rest/app/getRaceList?";
		str += ("userId=" + userId + "&pageNum=" + pageNum + "&pageSize=10");
		return str;
	}

	/**
	 * 获取用户未开始的竞赛列表
	 *
	 * @param userId
	 * @param pageNum
	 * @return
	 */
	public static String urlGetMyRaceForFriend(String userId, String friendId,
											   int pageNum) {
		String str = m_serverAddress + "rest/app/getMyRaceForFriend?";
		str += ("userId=" + userId + "&friendId=" + friendId + "&pageNum="
				+ pageNum + "&pageSize=10");
		return str;
	}

	/**
	 * 获取我的竞赛列表
	 *
	 * @param userId
	 * @param pageNum
	 * @return
	 */
	public static String urlGetMyCompetitionList(String userId, int pageNum) {
		String str = m_serverAddress + "rest/app/getMyRace?";
		str += ("userId=" + userId + "&pageNum=" + pageNum + "&pageSize=10");
		return str;
	}

	/**
	 * 获取竞赛详情
	 *
	 * @param userId
	 * @param raceId
	 * @return
	 */
	public static String urlGetCompetitionDetail(String userId, String raceId) {
		String str = m_serverAddress + "rest/app/getRaceInfo?";
		str += ("userId=" + userId + "&raceId=" + raceId);
		return str;
	}

	/**
	 * 加入竞赛
	 *
	 * @param userId
	 * @param raceId
	 * @return
	 */
	public static String urlJoinCompetition(String userId, String raceId) {
		String str = m_serverAddress + "rest/app/joinRace?";
		str += ("userId=" + userId + "&raceId=" + raceId);
		return str;
	}

	/**
	 * 加入已有竞赛
	 *
	 * @param userId
	 * @param raceId
	 * @return
	 */
	public static String urlJoinRaceCompetition(String userId, String friendId,
												String raceId) {
		String str = m_serverAddress + "rest/app/joinRaceForFriend?";
		str += ("userId=" + userId + "&friendId=" + friendId + "&raceId=" + raceId);
		return str;
	}

	/**
	 * 获取评论
	 *
	 * @param userId
	 * @param raceId
	 * @return
	 */
	public static String urlGetCommentList(String userId, String raceId) {
		String str = m_serverAddress + "rest/app/getCommentList?";
		str += ("userId=" + userId + "&raceId=" + raceId);
		return str;
	}

	/**
	 * 发送评论
	 *
	 * @param userId
	 * @param raceId
	 * @param comment
	 * @return
	 */
	public static String urlSendComment(String userId, String raceId,
										String comment) {
		String str = m_serverAddress + "rest/app/addComment?";
		str += ("userId=" + userId + "&raceId=" + raceId + "&comment=" + comment);
		return str;
	}

	/**
	 * 获取好友列表
	 *
	 * @param userId
	 * @return
	 */
	public static String urlGetFriendsList(String userId) {
		String str = m_serverAddress + "rest/app/getMyFriends?";
		str += ("userId=" + userId);
		return str;
	}

	/**
	 * 获取附近（1000米以内的人）
	 *
	 * @param userId
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static String urlGetNearlist(String userId, double lat, double lon) {
		String str = m_serverAddress + "rest/app/getNearbyUserList?";
		str += ("userId=" + userId + "&lat=" + lat + "&lon=" + lon);
		return str;
	}

	/**
	 * 更新地理位置
	 *
	 * @param userId
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static String urlUpdateGeohash(String userId, String lat, String lon) {
		String str = m_serverAddress + "rest/app/updateGeohash?";
		str += ("userId=" + userId + "&lat=" + lat + "&lon=" + lon);
		return str;
	}

	/**
	 * 取得好友（附近的人）个人信息
	 *
	 * @param userId
	 * @param friendUserId
	 * @return
	 */
	public static String urlGetMyFriendInfo(String userId, String friendUserId) {
		String str = m_serverAddress + "rest/app/getMyFriendInfo?";
		str += ("userId=" + userId + "&friendUserId=" + friendUserId);
		return str;
	}

	/**
	 * 添加好友
	 *
	 * @param userId
	 * @param friendUserId
	 * @return
	 */
	public static String urlAddMyFriend(String userId, String friendUserId) {
		String str = m_serverAddress + "rest/app/addMyFriend?";
		str += ("userId=" + userId + "&friendUserId=" + friendUserId);
		return str;
	}

	/**
	 * 添加留言
	 *
	 * @param userId
	 * @param friendUserId
	 * @param message
	 * @return
	 */
	public static String urlAddLeaveMessage(String userId, String friendUserId,
											String message) {
		String str = m_serverAddress + "rest/app/addMessage?";
		str += ("userId=" + userId + "&friendUserId=" + friendUserId
				+ "&message=" + message);
		return str;
	}

	/**
	 * 查看留言
	 *
	 * @param userId
	 * @param friendUserId
	 * @return
	 */
	public static String urlGetLeaveMessage(String userId, String friendUserId) {
		String str = m_serverAddress + "rest/app/getMessage?";
		str += ("userId=" + userId + "&friendUserId=" + friendUserId);
		return str;
	}

	/**
	 * 获取资讯厂商
	 *
	 * @param userId
	 * @return
	 */
	public static String urlGetInformationThird(String userId) {
		String str = m_serverAddress
				+ "stbInformation_getThirdPartyProviderTabs.action?";
		str += ("userId=" + userId);
		return str;
	}

	/**
	 * 服务首页
	 *
	 * @param userId
	 * @return
	 */
	public static String urlGetInformationThirdAndMedicineNotice(String userId) {
		String str = m_serverAddress
				+ "stbInformation_getThirdPartyProviderTabsAndMedicineNotice.action?";
		str += ("userId=" + userId);
		return str;
	}

	/**
	 * 单个成员的资讯厂商
	 *
	 * @param userId
	 * @param familyMemberId
	 * @return
	 */
	public static String urlGetInformationThirdByMember(String userId,
														String familyMemberId) {
		String str = m_serverAddress
				+ "stbInformation_getThirdPartyProviderTabs.action?";
		str += ("userId=" + userId + "&familyMemberId=" + familyMemberId);
		return str;
	}

	/**
	 * 获取消息页未读个数
	 *
	 * @param userId
	 * @return
	 */
	public static String urlGetUnreadNoticeCount(String userId) {
		String str = m_serverAddress
				+ "stbHisttory_getUnreadNoticeCount.action?";
		str += ("userId=" + userId);
		return str;
	}

	/**
	 * 最近测量
	 *
	 * @param userId
	 * @return
	 */
	public static String urlGetLastHealth(String userId) {
		String str = m_serverAddress + "rest/app/getLastHealth?";
		str += ("userId=" + userId + "&serialNo=");
		return str;
	}

	/**
	 * 单个成员的最近测量
	 *
	 * @param familyMemberId
	 * @return
	 */
	public static String urlGetLastHealthByMember(String familyMemberId) {
		String str = m_serverAddress
				+ "rest/app/getLastHealthByFamilyMemberId?";
		str += ("familyMemberId=" + familyMemberId);
		return str;
	}

	/**
	 * 取得圈子消息
	 *
	 * @param userId
	 * @param pageNum
	 * @return
	 */
	public static String urlGetCircleMessage(String userId, int pageNum) {
		String str = m_serverAddress + "rest/app/getCircleMessage?";
		str += ("userId=" + userId + "&pageNum=" + pageNum + "&pageSize=10");
		return str;
	}

	/**
	 * 删除圈子消息
	 *
	 * @param userId
	 * @param circleMessageId
	 * @return
	 */
	public static String urlDelCircleMessage(String userId,
											 String circleMessageId) {
		String str = m_serverAddress + "rest/app/delCircleMessage?";
		str += ("userId=" + userId + "&circleMessageId=" + circleMessageId);
		return str;
	}

	/**
	 * 将圈子消息的留言类型的消息设置成已读
	 *
	 * @param userId
	 * @param circleMessageId
	 * @return
	 */
	public static String urlUpdateCircleMessageIsRead(String userId,
													  String circleMessageId) {
		String str = m_serverAddress + "rest/app/updateCircleMessage2IsRead?";
		str += ("userId=" + userId + "&circleMessageId=" + circleMessageId);
		return str;
	}

	/**
	 * 修改密码
	 *
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public static String urlUpdatePassword(String userId, String oldPassword,
										   String newPassword) {
		String str = m_serverAddress + "rest/app/updatePassword?";
		str += ("userId=" + userId + "&oldPassword=" + oldPassword
				+ "&newPassword=" + newPassword);
		return str;
	}

	/**
	 * 根据城市名称获取天气
	 *
	 * @param cityname
	 * @return
	 */
	public static String urlGetWeather(String cityname) {
		String str = "http://apistore.baidu.com/microservice/weather?";
		str += ("cityname=" + cityname);
		return str;
	}
	/**
	 * 根据城市名称获取天气
	 *
	 * @param cityname
	 * @return
	 */
	public static String urlGetWeather2(String cityname,String password , String day) {
		String str = weather_url_base + "xml.php?";
		str += ("cityname=" + cityname + "&password=" + password + "&day=" +day);
		return str;
	}

	/**
	 * 取得手环当天测量的睡眠数据
	 *
	 * @param familyMemberId
	 * @return
	 */
	public static String urlGetSleepData(String familyMemberId, String theDay) {
		String str = m_serverAddress + "rest/app/getLastSleepInfo?";
		if (theDay.equals("")) {
			str += ("familyMemberId=" + familyMemberId);
		} else {
			str += ("familyMemberId=" + familyMemberId + "&theDay=" + theDay);
		}

		return str;
	}

	/**
	 * 取得手环当天测量的运动数据
	 *
	 * @param familyMemberId
	 * @return
	 */
	public static String urlGetSportData(String familyMemberId, String theDay) {
		String str = m_serverAddress + "rest/app/getLastActivityInfo?";
		if (theDay.equals("")) {
			str += ("familyMemberId=" + familyMemberId);
		} else {
			str += ("familyMemberId=" + familyMemberId + "&theDay=" + theDay);
		}

		return str;
	}

	/**
	 * 机顶盒帮助说明
	 *
	 * @return
	 */
	public static String urlGetJiDingHeHelp() {
		String str = m_serverAddress + "app/help/bindHelp.jsp";
		return str;
	}

	/**
	 * 咕咚授权
	 */
	public static String urlOauth_codoon() {
		String str = codoon_aouth + "client_id=" + codoon_appkey
				+ "&redirect_uri=" + codoon_redirect_uri
				+ "from=mobile&response_type=code&scope=user,sports";
		return str;
	}

	/**
	 * 获取 咕咚 token
	 *
	 * @param code
	 * @param familyMemberId
	 * @param deviceId
	 * @return
	 */
	public static String urlGetToken(String code, String familyMemberId,
									 String deviceId) {
		String str = m_serverAddress + "rest/codoon/verify?"
				+ "familyMemberId=" + familyMemberId + "&deviceId=" + deviceId
				+ "&code=" + code;
		return str;
	}

	/**
	 * 获取未绑定的家庭成员
	 *
	 * @param userId
	 * @return
	 */
	public static String urlGetFamilyMemberNobind(String userId) {
		String str = m_serverAddress + "rest/family/member/list/nobind?";
		str += ("userId=" + userId);
		return str;
	}

	/**
	 * 同步数据
	 *
	 * @param familyMemberId
	 * @param accessToken
	 * @return
	 */
	public static String urlSyncAllData(String familyMemberId,
										String accessToken) {
		String str = m_serverAddress + "rest/codoon/syncAllData?";
		str += ("familyMemberId=" + familyMemberId + "&accessToken=" + accessToken);
		return str;

	}

	/**
	 * 绑定手表
	 *
	 * @param deviceId
	 * @param familyMemeberId
	 * @return
	 */
	public static String urlBindWatch(String deviceId, String familyMemeberId) {
		String str = m_serverAddress + "rest/watch/bind?";
		str += ("deviceId=" + deviceId + "&familyMemeberId=" + familyMemeberId);
		return str;

	}

	/**
	 * 配件解绑
	 *
	 * @param familyMemberId
	 * @return
	 */
	public static String urlUnBind(String familyMemberId) {
		String str = m_serverAddress + "rest/token/unbind?";
		str += ("familyMemberId=" + familyMemberId);
		return str;

	}
	/**
	 * 获取第三方服务订购列表信息
	 * @param cooperateIdentify
	 * @param familyId
	 * @return
	 */
	public static String urlGetOrderServiceList(String cooperateIdentify,String familyId) {
		String str = m_serverAddress + "rest/app/getServicePackageGeneral?";
		str += ("serialNo=&cooperateIdentify=" + cooperateIdentify+"&familyId="+familyId);
		return str;

	}
	/**
	 * 订购
	 * @param cooperateIdentify
	 * @param familyId
	 * @param startDate
	 * @param endDate
	 * @param serviceID
	 * @param price
	 * @param subscriberID
	 * @return
	 */
	public static String urlOrderService(String cooperateIdentify,String familyId,String startDate,String endDate,String serviceID,String price,String subscriberID) {
		String str = m_serverAddress + "rest/"+cooperateIdentify+"/registerGeneral?";
		str += ("serialNo=&familyId=" + familyId+"&startDate="+startDate+"&endDate="+endDate+"&isPackage=true&serviceID="+serviceID+"&price="+price+"&subscriberID="+subscriberID);
		return str;

	}
	/**
	 * 退订
	 * @param cooperateIdentify
	 * @param familyId
	 * @param servicePackageId
	 * @return
	 */
	public static String urlUnOrderService(String cooperateIdentify,String familyId,String servicePackageId) {
		String str = m_serverAddress + "rest/"+cooperateIdentify+"/unsubscribe?";
		str += ("familyId=" + familyId+"&servicePackageId="+servicePackageId);
		return str;

	}
	/**
	 * 找回密码
	 * @param userId
	 * @param phoneNumber
	 * @param code 验证码
	 * @param password
	 * @return
	 */
	public static String urlFindPass(String phoneNumber,String code,String password) {
		String str = m_serverAddress + "regist_phoneChangePwd.action?";
		str += ("phoneNumber=" + phoneNumber+"&phoneCaptcha="+code+"&password="+password);
		return str;

	}
	/**
	 * 解析家庭列表
	 *
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static List<Object> getFamilyList(JSONArray ja) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String addTime = jo.optString("addTime");
			int age = jo.optInt("age");
			String birthday = jo.optString("birthday");
			String familyMemberRoleName = jo.optString("familyMemberRoleName");
			String gender = jo.optString("gender");
			double height = jo.optDouble("height");
			long id = jo.optLong("id");
			double stepSize = jo.optDouble("stepSize");
			double waist = jo.optDouble("waist");
			double weight = jo.optDouble("weight");
			String mobile = jo.optString("mobile");
			boolean masterFamilyMember = jo.optBoolean("masterFamilyMember");
			int device_name = 4000101;
			if (!jo.isNull("device_name")) {
				String aa = jo.optString("device_name");
				if (aa.equals("balanceDevice")) {
					device_name = 4000101;
				} else if (aa.equals("bloodGlucoseDevice")) {
					device_name = 4000104;
				} else if (aa.equals("bloodPressureDevice")) {
					device_name = 4000103;
				} else if (aa.equals("fatMonitorDevice")) {
					device_name = 4000102;
				} else if (aa.equals("temperatureDevice")) {
					device_name = 4000105;
				} else {
					device_name = 4000101;
				}
			}
			FamilyMember member = new FamilyMember(id, gender, birthday,
					familyMemberRoleName, weight, height, stepSize, waist,
					addTime, age, mobile, masterFamilyMember, device_name);
			list.add(member);
		}
		return list;
	}

	public static List<FamilyMember> getFamilyListNoBind(JSONArray ja)
			throws JSONException {
		List<FamilyMember> list = new ArrayList<FamilyMember>();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			long id = jo.optLong("id");
			String gender = jo.optString("gender");
			String birthday = jo.optString("birthday");
			String familyMemberRoleName = jo.optString("familyMemberRoleName");
			String addTime = jo.optString("addTime");
			int age = jo.optInt("age");
			double height = jo.optDouble("height");
			double stepSize = jo.optDouble("stepSize");
			double waist = jo.optDouble("waist");
			double weight = jo.optDouble("weight");
			boolean masterFamilyMember = jo.optBoolean("masterFamilyMember");
			FamilyMember member = new FamilyMember(id, gender, birthday,
					familyMemberRoleName, weight, height, stepSize, waist,
					addTime, age, "", masterFamilyMember, 4000101);
			list.add(member);
		}
		return list;
	}

	/**
	 * 获取单个成员最近一次的健康记录
	 *
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static HealthRecord getRecordInfo(JSONArray ja) throws JSONException {
		int stepNum = 0;
		double distance = 0.0;
		double calorie = 0.0;
		double weight = 0.0;
		double temperature = 0.0;
		double moistureRate = 0.0;
		double fatPercentage = 0.0;
		double muscleVolume = 0.0;
		double visceralFatRating = 0.0;
		double diastolicPressure = 0.0;
		double systolicPressure = 0.0;
		double pulse = 0.0;
		double bloodGlucose = 0.0;
		String dateJKC = "";
		String dateXYJ = "";
		String dateXTY = "";
		String dateJBQ = "";
		String dateEWQ = "";
		String dateZFY = "";
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String name = "";
			name = jo.optString("devinceName");
			if (name.trim().equals("计步器")) {
				stepNum = jo.optInt("stepNum");
				distance = jo.optDouble("distance");
				calorie = jo.optDouble("calorie");
				dateJBQ = jo.optString("prettyMeasureTime");
			} else if (name.trim().equals("健康秤")) {
				weight = jo.optDouble("weight");
				dateJKC = jo.optString("prettyMeasureTime");
			} else if (name.trim().equals("耳温枪")) {
				temperature = jo.optDouble("temperature");
				dateEWQ = jo.optString("prettyMeasureTime");
			} else if (name.trim().equals("脂肪仪")) {
				moistureRate = jo.optDouble("moistureRate");
				fatPercentage = jo.optDouble("fatPercentage");
				muscleVolume = jo.optDouble("muscleVolume");
				visceralFatRating = jo.optDouble("visceralFatRating");
				dateZFY = jo.optString("prettyMeasureTime");
			} else if (name.trim().equals("血压计")) {
				diastolicPressure = jo.optDouble("diastolicPressure");
				systolicPressure = jo.optDouble("systolicPressure");
				pulse = jo.optDouble("pulse");
				dateXYJ = jo.optString("prettyMeasureTime");
			} else if (name.trim().equals("血糖仪")) {
				bloodGlucose = jo.optDouble("bloodGlucose");
				dateXTY = jo.optString("prettyMeasureTime");
			}
		}
		HealthRecord info = new HealthRecord(weight, bloodGlucose, pulse,
				diastolicPressure, systolicPressure, fatPercentage,
				visceralFatRating, moistureRate, muscleVolume, stepNum,
				distance, calorie, temperature, dateJKC, dateXYJ, dateXTY,
				dateJBQ, dateEWQ, dateZFY);
		return info;
	}
}
