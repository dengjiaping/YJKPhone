package com.sinosoft.fhcs.android.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.OrderServiceInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderServiceListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<OrderServiceInfo> myList = new ArrayList<OrderServiceInfo>();
	private String familyId = "";

	public OrderServiceListAdapter(Context context, String familyId,
								   List<OrderServiceInfo> list) {
		this.mContext = context;
		this.myList = list;
		this.familyId = familyId;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if (myList.size() == 0) {
			return 0;
		} else {
			return myList.size();
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.orderservicelist_listview_item, null);

			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.orderservice_item_tv_title);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.orderservice_item_tv_content);
			viewHolder.tvPrice = (TextView) convertView
					.findViewById(R.id.orderservice_item_tv_price);
			viewHolder.tvDate = (TextView) convertView
					.findViewById(R.id.orderservice_item_tv_date);
			viewHolder.tvTuiGuang = (TextView) convertView
					.findViewById(R.id.orderservice_item_tv_tuiguang);
			viewHolder.btnOrder = (Button) convertView
					.findViewById(R.id.orderservice_item_btn_order);
			viewHolder.btnUnOrder = (Button) convertView
					.findViewById(R.id.orderservice_item_btn_unorder);
			viewHolder.layoutIsOrder = (LinearLayout) convertView
					.findViewById(R.id.orderservice_item_layout_isorder);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final OrderServiceInfo orderServiceInfo = myList.get(position);
		if (null != orderServiceInfo) {
			if (orderServiceInfo.isOrder()) {
				viewHolder.btnUnOrder.setVisibility(View.VISIBLE);
				viewHolder.layoutIsOrder.setVisibility(View.VISIBLE);
				viewHolder.btnOrder.setVisibility(View.GONE);
				viewHolder.tvDate.setText(orderServiceInfo.getDate());
			} else {
				viewHolder.btnOrder.setVisibility(View.VISIBLE);
				viewHolder.btnUnOrder.setVisibility(View.GONE);
				viewHolder.layoutIsOrder.setVisibility(View.GONE);
			}
			if (orderServiceInfo.isTuiGuang()) {
				viewHolder.tvTuiGuang.setVisibility(View.VISIBLE);
			} else {
				viewHolder.tvTuiGuang.setVisibility(View.GONE);
			}
			viewHolder.tvTitle.setText(orderServiceInfo.getTitle().toString());
			try {
				String temp = orderServiceInfo.getContent()
						.replace("\\n", "\n");
				viewHolder.tvContent.setText(temp);
			} catch (Exception e) {
				viewHolder.tvContent.setText(orderServiceInfo.getContent()
						.toString());
			}
			viewHolder.tvPrice.setText(orderServiceInfo.getPrice().toString());
			final int mPosition = position;
			viewHolder.btnOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd");
					String date = simpleDateFormat.format(calendar.getTime());
					String subscriberID = "" + 1000
							+ (int) (Math.random() * 9000);
					OrderOrUnOrderRequest orderRequest = new OrderOrUnOrderRequest(
							mPosition, HttpManager.urlOrderService(
							orderServiceInfo.getType(), familyId, date,
							date, orderServiceInfo.getId(),
							orderServiceInfo.getPrice().toString()
									.trim(), subscriberID), 0);
					orderRequest.execute();
				}
			});
			viewHolder.btnUnOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(mContext,
							AlertDialog.THEME_HOLO_LIGHT)
							.setCancelable(false)
							.setTitle("温馨提示")
							.setMessage("您确定要退订服务吗?")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											OrderOrUnOrderRequest orderRequest = new OrderOrUnOrderRequest(
													mPosition,
													HttpManager.urlUnOrderService(
															orderServiceInfo
																	.getType(),
															familyId,
															orderServiceInfo
																	.getId()),
													1);
											orderRequest.execute();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();
				}
			});
		}

		return convertView;
	}

	// 网络请求
	private class OrderOrUnOrderRequest extends AsyncTask<Object, Void, String> {
		private String url;
		private ProgressDialog progressDialog;// 进度条
		private int mPosition;
		private int type = 0;// 0 订购 1 退订

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(mContext);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		public OrderOrUnOrderRequest(int position, String url, int type) {
			super();
			this.mPosition = position;
			this.url = url;
			this.type = type;
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			Log.e("OrderServiceUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				Toast.makeText(mContext, "服务器响应超时!", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject jo = new JSONObject(result);

					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						if (type == 0) {
							JSONObject jo2 = jo.getJSONObject("data");
							System.out.println("jo2==="+jo2);
							String startDate = "";
							if (!jo2.isNull("startDate")) {
								startDate = jo2.optString("startDate");
							}
							String endDate = "";
							if (!jo2.isNull("endDate")) {
								endDate = jo2.optString("endDate");
							}
							String date = "";
							if (!startDate.equals("") && !endDate.equals("")) {
								date = startDate + "-" + endDate;
							}
							Toast.makeText(mContext, "订购服务成功！",
									Toast.LENGTH_SHORT).show();
							myList.get(mPosition).setOrder(true);
							myList.get(mPosition).setDate(date);
						} else {
							Toast.makeText(mContext, "退订服务成功！",
									Toast.LENGTH_SHORT).show();
							myList.get(mPosition).setOrder(false);
						}

						notifyDataSetChanged();
					} else {
						if (type == 0) {
							Toast.makeText(mContext, "订购服务失败！",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(mContext, "退订服务失败！",
									Toast.LENGTH_SHORT).show();
						}

					}
				} catch (JSONException e) {
					if (type == 0) {
						Toast.makeText(mContext, "订购服务失败！", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(mContext, "退订服务失败！", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
			Constant.exitProgressDialog(progressDialog);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvContent;
		private TextView tvDate;
		private TextView tvPrice;
		private TextView tvTuiGuang;
		private Button btnOrder;
		private Button btnUnOrder;
		private LinearLayout layoutIsOrder;
	}

}
