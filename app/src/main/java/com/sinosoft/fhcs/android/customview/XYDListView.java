package com.sinosoft.fhcs.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/11/1 15:26
 */

public class XYDListView extends ListView {

    public XYDListView(Context context) {
        super(context);
    }

    public XYDListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XYDListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
