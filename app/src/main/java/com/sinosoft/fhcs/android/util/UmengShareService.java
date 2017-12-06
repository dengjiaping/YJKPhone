package com.sinosoft.fhcs.android.util;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
/**
 * @CopyRight: SinoSoft.
 * @Description: 分享类
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */


public class UmengShareService {
	private static final String appkey_weixin = "wxb34fea6d3ef789ba";// 微信
	// appkey
	private static final String appkey_yixin = "yx53bb9325dd0549efa72e8ad3e91c5761";// 易信appkey

	public static void share(final Activity activity, String desc, String hOST, UMImage image, UMShareListener umShareListener) {
		final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
				{
						SHARE_MEDIA.YIXIN,SHARE_MEDIA.YIXIN_CIRCLE,SHARE_MEDIA.WEIXIN,
						SHARE_MEDIA.WEIXIN_CIRCLE//,SHARE_MEDIA.SINA
				};
		new ShareAction(activity).setDisplayList( displaylist )
				.withText( "翼家康" )
				.withTitle(desc)
				.withTargetUrl(""+hOST)
				.withMedia( image )
				.setListenerList(umShareListener)
				.open();

	}
/*	
	public static void share(final Activity activity, String desc, UMImage image, UMShareListener umShareListener) {
		new ShareAction(activity).setDisplayList(SHARE_MEDIA.YIXIN,SHARE_MEDIA.YIXIN_CIRCLE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA)
		.setContentList(new ShareContent(),new ShareContent())
		.withMedia(image)
		.setListenerList(umShareListener)
		.open();
		
	}
*/

}
