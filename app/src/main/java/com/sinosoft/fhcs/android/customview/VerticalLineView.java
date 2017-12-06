package com.sinosoft.fhcs.android.customview;
/**
 * 设定目标界面划线
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VerticalLineView extends View {
	// 绘制文字坐标
	private float currentX;
	private float currentY;
	private String text;

	//画竖线坐标
	private float drawlineYStart;
	private float drawlineYEnd;
	private float drawlineXStart;
	private float drawlineXEnd;;
	private int color=Color.BLACK;
	private int textSize=30;
	public VerticalLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setLineAndPoint(int color,int textSize,float Xstart,float Ystart,float Xend,float Yend,float textX,float textY,String text)
	{
		this.color=color;
		this.textSize=textSize;
		this.drawlineXStart=Xstart;
		this.drawlineXEnd=Xend;
		this.drawlineYStart=Ystart;
		this.drawlineYEnd=Yend;
		this.currentX=textX;
		this.currentY=textY;
		this.text=text;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// canvas.drawColor(Color.WHITE);//设置背景颜色
//		Paint paint = new Paint();
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setAntiAlias(true);// 去锯齿
//		paint.setColor(color);// 颜色
//		paint.setTextSize(textSize); // 文字大小
//		paint.setStrokeWidth(2.5f);
//		canvas.drawLine(drawlineXStart,drawlineYStart , drawlineXEnd, drawlineYEnd,paint);
//		Paint paint2=new Paint();
//		paint2.setStyle(Paint.Style.STROKE);
//		paint2.setAntiAlias(true);// 去锯齿
//		paint2.setColor(color);// 颜色
//		paint2.setTextSize(textSize); // 文字大小
//		canvas.drawText(text, currentX, currentY, paint2);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 去锯齿
		paint.setColor(color);// 颜色
		paint.setTextSize(textSize); // 文字大小
		canvas.drawLine(drawlineXStart,drawlineYStart , drawlineXEnd, drawlineYEnd,paint);
		if(text == null){
			text = "";
		}
		canvas.drawText(text, currentX, currentY, paint);


	}

}
