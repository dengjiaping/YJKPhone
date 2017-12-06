package com.sinosoft.fhcs.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ViewHolder {
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
					   int position)
	{
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 *
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
								 ViewGroup parent, int layoutId, int position)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder(context, parent, layoutId, position);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
		}
		return holder;
	}

	public View getConvertView()
	{
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 *
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为控件设置tag(便于url设置tag)
	 *
	 * @param viewId
	 * @return
	 */
	public ViewHolder setViewTag(int viewId, int postion)
	{
		View view = getView(viewId);
		view.setTag(postion);
		return this;
	}
	/**
	 * 为TextView设置字符串
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text)
	{
		TextView view = getView(viewId);
		if(text == null || text.length() == 0 || text.equals("")){
			view.setText("");
		}else{
			view.setText(text);
		}
		return this;
	}
	/**
	 * 为控件设置tag(便于用控件获取item的索引)
	 *
	 * @param viewId
	 * @param postion
	 * @return
	 */
	public ViewHolder setTag(int viewId, int postion)
	{
		TextView view = getView(viewId);
		view.setTag(postion);
		return this;
	}
	/**
	 * 为控件设置tag(便于用控件获取item的索引)
	 *
	 * @param viewId
	 * @return
	 */
	public Object getTag(int viewId)
	{
		TextView view = getView(viewId);
		return view.getTag();
	}
	/**
	 * 为控件设置tag(便于url设置tag)
	 *
	 * @param viewId
	 * @param imageUrl
	 * @return
	 */
	public ViewHolder setTag(int viewId, String imageUrl)
	{
		ImageView view = getView(viewId);
		view.setTag(imageUrl);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param mipmapId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int mipmapId)
	{
		ImageView view = getView(viewId);
		view.setImageResource(mipmapId);

		return this;
	}
	/**
	 * 为背景颜色
	 *
	 * @param viewId
	 * @param color
	 * @return
	 */
	public ViewHolder setBackGrounColor(int viewId, int color)
	{
		View view = getView(viewId);
		view.setBackgroundColor(color);

		return this;
	}
	/**
	 * 设置字体颜色
	 *
	 * @param viewId
	 * @param color
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, int color)
	{
		TextView view = getView(viewId);
		view.setTextColor(color);

		return this;
	}
	/**
	 * 设置背景为空
	 *
	 * @param viewId
	 * @param
	 * @return
	 */
	public void setImageResourceNull(int viewId)
	{
		View view = getView(viewId);
		view.setBackgroundDrawable(null);
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm)
	{
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param
	 * @return
	 */
	public ViewHolder setImageByUrl(int viewId, int  draw)
	{
		ImageView imageView = getView(viewId);
		CommonUtil.downloadIcon2View("file://"+draw,imageView, R.drawable.pictures_no,R.drawable.pictures_no);
		//ImageLoader.getInstance().displayImage("file://"+url, (ImageView) getView(viewId));
		return this;
	}
	/**
	 * 为ImageView设置网络图片
	 *
	 * @param viewId
	 * @param
	 * @return
	 */
	public ViewHolder setImageByInternetUrl(int viewId, String url)
	{
		//ImageLoader.getInstance().displayImage(url, (ImageView) getView(viewId));
		ImageView imageView = getView(viewId);
		CommonUtil.downloadIcon2View(url,imageView,R.drawable.pictures_no,R.drawable.pictures_no);
		return this;
	}
	/**
	 * 设置图片的显示与隐藏
	 *
	 * @param viewId
	 * @param
	 * @return
	 */
	public ViewHolder setViewVisible(int viewId,boolean isShow)
	{

		View view = getView(viewId);
		if(isShow){
			view.setVisibility(View.VISIBLE);
		}else{
			view.setVisibility(view.GONE);
		}
		return this;
	}


	/**
	 * view设置监听事件
	 * @param viewId view的id
	 * @param clickListener 回调事件
	 */
	public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
		View view = getView(viewId);
		view.setOnClickListener(clickListener);
	}
	/**
	 * checkBox设置监听事件
	 * @param viewId view的id
	 * @param onCheckedChangeListener 回调事件
	 */
	public void setCheckBoxOnClickListener(int viewId, CheckBox.OnCheckedChangeListener onCheckedChangeListener) {
		CheckBox view = getView(viewId);
		view.setOnCheckedChangeListener(onCheckedChangeListener);
	}
	/**
	 * checkBox设置值
	 * @param viewId view的id
	 * @param b 是否发对勾 true：对勾
	 */
	public void setCheckBoxBoolean(int viewId, boolean b) {
		CheckBox view = getView(viewId);
		view.setChecked(b);
	}
	/**
	 * checkBox设置值
	 * @param viewId view的id
	 * @param b 是否发对勾 true：对勾
	 */
	public void setChecked(int viewId, String b) {
		CheckBox view = getView(viewId);
		if(b.equals("Y")){
			view.setChecked(true);
			//view.setText("已自检");
		}else if(b.equals("N")){
			view.setChecked(false);
			//view.setText("自检");

		}
	}

	/**
	 * 设置输入框的数据监听
	 * @param viewId
	 * @param watcher
	 */
	public void setTextWatcher(int viewId, TextWatcher watcher){
		EditText text = getView(viewId);
		text.addTextChangedListener(watcher);
	}

	/**
	 * 为TextView设置字符串
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setEditText(int viewId, String text)
	{
		EditText view = getView(viewId);
		view.setText(text);
		return this;
	}
	public int getPosition()
	{
		return mPosition;
	}

	/**
	 * 给textview设置时间
	 * @param viewId
	 * @param date
	 */
	public void setTextviewDate(int viewId, Long date,String strFormat){
		SimpleDateFormat format = new SimpleDateFormat(strFormat);
		String strDate = format.format(new Date(date));
		TextView view = getView(viewId);
		view.setText(strDate);
	}
	/**
	 * 给textview设置时间
	 * @param viewId
	 * @param date
	 */
	public void setTextviewDate(int viewId, Date date,String strFormat){
		SimpleDateFormat format = new SimpleDateFormat(strFormat);
		String strDate = format.format(date);
		TextView view = getView(viewId);
		view.setText(strDate);
	}

}
