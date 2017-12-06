package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 评论列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2015年1月22日.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.CommentInfo;

import java.util.ArrayList;
import java.util.List;

public class CommentListviewAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<CommentInfo>myList=new ArrayList<CommentInfo>();
	private DisplayImageOptions options;
	public CommentListviewAdapter(Context context,List<CommentInfo>list) {
		this.mContext = context;
		this.myList=list;
		mInflater = LayoutInflater.from(mContext);
		initOptions();
	}
	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar)
				.showImageForEmptyUri(R.drawable.avatar)
				.showImageOnFail(R.drawable.avatar)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

	}
	public int getCount() {
		if(myList.size()==0){
			return 0;
		}else{
			return myList.size();
		}
	}

	public Object getItem(int position) {
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.commentlist_listview_item,
					null);
		}
		CircleImageView img = (CircleImageView) convertView
				.findViewById(R.id.comment_listviewitem_iv_img);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.comment_listviewitem_tv_name);
		TextView tvContent = (TextView) convertView
				.findViewById(R.id.comment_listviewitem_tv_content);
		TextView tvDate = (TextView) convertView
				.findViewById(R.id.comment_listviewitem_tv_date);
		View line=convertView.findViewById(R.id.comment_listviewitem_view_line);
		if(position==0){
			line.setVisibility(View.GONE);
		}else{
			line.setVisibility(View.VISIBLE);
		}
		tvName.setText(myList.get(position).getName().toString().trim());
		tvContent.setText(myList.get(position).getContent().toString().trim());
		tvDate.setText(myList.get(position).getDate().toString().trim());
		ImageLoader.getInstance().displayImage(myList.get(position).getUrl(), img, options);
		return convertView;
	}
}
