package com.sinosoft.fhcs.android.service;

/**
 * @CopyRight: SinoSoft.
 * @Description: 健康资讯服务
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sinosoft.fhcs.android.activity.InformationActivity;
import com.sinosoft.fhcs.android.entity.InformationChild;
import com.sinosoft.fhcs.android.entity.InformationGroup;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.socialize.utils.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InformationService extends Service {
	private String sb = null;
	private String taskUrl;
	private String userId;
	private String memberId = "memberId";
	private String cooperateIdentify;
	private int request_index;
	private boolean isSuccess;
	private InformationGroup item_listGround;
	private InformationChild item_listChild;
	private List<InformationChild> list_child;
	private List<InformationGroup> list_group;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			taskUrl = intent.getStringExtra("taskUrl");
			Log.e("资讯一级", taskUrl);
			request_index = intent.getIntExtra("request", -1);
			userId = intent.getStringExtra("userId");
			memberId = intent.getStringExtra("memberId");
			cooperateIdentify=intent.getStringExtra("cooperateIdentify");
			new Thread() {
				public void run() {
					try {
						sb = HttpManager.getStringContent(taskUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!sb.equals("ERROR")) {
						isSuccess = JsonParser(sb.toString(), request_index);
						if (isSuccess) {
							for (int a = 0; a < list_group.size(); a++) {
								try {
									if (memberId.equals("memberId")) {
										sb = HttpManager
												.getStringContent(HttpManager
														.urlInfoDetails(
																userId,
																list_group
																		.get(a)
																		.getS_id(),cooperateIdentify));
										Log.e("资讯二级", HttpManager
												.urlInfoDetails(userId,
														list_group.get(a)
																.getS_id(),cooperateIdentify));
									} else {
										sb = HttpManager
												.getStringContent(HttpManager
														.urlInfoDetailsOne(
																userId,
																list_group
																		.get(a)
																		.getS_id(),
																memberId,cooperateIdentify));
										Log.e("资讯二级个人", HttpManager
												.urlInfoDetailsOne(userId,
														list_group.get(a)
																.getS_id(),
														memberId,cooperateIdentify));
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
								boolean isSuccess = JsonParser(sb.toString(),
										Constant.Json_Request_Onetask);
								if (isSuccess) {
									item_listGround = new InformationGroup(
											list_group.get(a).getS_id(),
											list_group.get(a).getS_title(),
											list_group.get(a).getUnreadCount(),
											list_child,list_group.get(a).isThirdPartyOrder());
									if (memberId.equals("memberId")) {
										InformationActivity.list_Ground
												.add(item_listGround);
									} else {
										InformationActivity.list_Ground
												.add(item_listGround);
									}
								} else {
									list_child = new ArrayList<InformationChild>();
									item_listGround = new InformationGroup(
											list_group.get(a).getS_id(),
											list_group.get(a).getS_title(),
											list_group.get(a).getUnreadCount(),
											list_child,list_group.get(a).isThirdPartyOrder());
									if (memberId.equals("memberId")) {
										InformationActivity.list_Ground
												.add(item_listGround);
									} else {
										InformationActivity.list_Ground
												.add(item_listGround);
									}
								}

							}
							if (memberId.equals("memberId")) {
								InformationActivity.handler.obtainMessage(
										Constant.Json_Return_Success)
										.sendToTarget();
							} else {
								InformationActivity.handler.obtainMessage(
										Constant.Json_Return_Success)
										.sendToTarget();
							}
							stopSelf();
						}
					} else {
						if (memberId.equals("memberId")) {
							InformationActivity.handler.obtainMessage(
									Constant.Json_Return_Fail).sendToTarget();
						} else {
							InformationActivity.handler.obtainMessage(
									Constant.Json_Return_Fail).sendToTarget();
						}
						stopSelf();
					}
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * 根据Json——String（字符串）得到List集合
	 */
	private boolean JsonParser(String jsonStr, int requestIndex) {
		switch (requestIndex) {
			case Constant.Json_Request_Alltask:
				// group
				try {
					list_group = new ArrayList<InformationGroup>();
					JSONObject jo = new JSONObject(jsonStr);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String entity = jo.optString("data");
						JSONObject jo2 = new JSONObject(entity);
						JSONArray ja = jo2.getJSONArray("informationType");
						boolean thirdPartyOrder=jo2.optBoolean("thirdPartyOrder");
						if (ja.length() == 0) {
							return false;
						} else {
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo3 = ja.getJSONObject(i);
								String id = jo3.optString("id");
								int unreadCount = jo3.optInt("unreadCount");
								String name = jo3.optString("name");
								item_listGround = new InformationGroup(id, name,unreadCount,thirdPartyOrder);
								list_group.add(item_listGround);
							}
							return true;
						}
					} else {
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			case Constant.Json_Request_Onetask:
				// child
				try {
					list_child = new ArrayList<InformationChild>();
					JSONObject jo = new JSONObject(jsonStr);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject entity = jo.getJSONObject("data");
						if (!entity.isNull("healthInformationList")) {
							JSONArray ja = entity
									.getJSONArray("healthInformationList");
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo2 = ja.getJSONObject(i);
								String familyMemberRoleName = jo2
										.optString("familyMemberRoleName");
								String id = jo2.optString("id");
								String html = "";
								if (!jo2.isNull("html")) {
									html = jo2.optString("html");
								}
								String imageText = "";
								if (!jo2.isNull("imageText")) {
									imageText = jo2.optString("imageText");
								}
								String informationContent = "";
								if (!jo2.isNull("informationContent")) {
									informationContent = jo2
											.optString("informationContent");
								}
								String informationType = jo2
										.optString("informationType");
								String pubdate = jo2.optString("pubdate");
								boolean readState = jo2.getBoolean("readState");
								String syncInformationSerialNo = "";
								if (!jo2.isNull("syncInformationSerialNo")) {
									syncInformationSerialNo = jo2
											.optString("syncInformationSerialNo");
								}
								String title = "";
								if (!jo2.isNull("title")) {
									title = jo2.optString("title");
								}
								String video = "";
								if (!jo2.isNull("video")) {
									video = jo2.optString("video");
								}
								String voice = "";
								if (!jo2.isNull("voice")) {
									voice = jo2.optString("voice");
								}
								String gender="1";
								if(!jo2.isNull("gender")){
									gender=jo2.optString("gender");
								}
								String cooperateIdentify=jo2.optString("cooperateIdentify");
								item_listChild = new InformationChild(id, html,
										imageText, video, voice,
										syncInformationSerialNo,
										familyMemberRoleName, informationContent,
										informationType, pubdate, title, readState,gender,cooperateIdentify);
								list_child.add(item_listChild);
							}
							return true;
						} else {
							return false;
						}

					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
		}
		return false;
	}

}
