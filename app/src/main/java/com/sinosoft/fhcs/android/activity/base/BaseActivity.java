package com.sinosoft.fhcs.android.activity.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.util.AppStatusManager;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.LogUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.sinosoft.fhcs.android.util.Constant.ACTION_RESTART_APP;
import static com.sinosoft.fhcs.android.util.Constant.KEY_HOME_ACTION;
import static com.sinosoft.fhcs.android.util.Constant.STATUS_FORCE_KILLED;
import static com.sinosoft.fhcs.android.util.Constant.STATUS_NORMAL;


/**
 * activity的基类
 * 继承此类可以实现的功能：
 * 1、创建基本的handler，
 * 2、dialog提示框显示和隐藏
 * 3、api联网
 * 4、数据库操作
 * 5、配置文件的读写
 */
public abstract class BaseActivity extends FragmentActivity {

    /**
     * 页面dialog的旋转框
     */
    private Dialog dialog;
    private String tag="BaseActivity";


    private MyReceiver receiver;

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch(AppStatusManager.getInstance().getAppStatus()) {
            case STATUS_FORCE_KILLED:
                restartApp();
                break;
            case STATUS_NORMAL:
                setUpViewAndData();
                break;
        }
        dialog = initDialog();
        tag = this.getClass().getSimpleName();
        //retentively();
        registerBroadcast();

    }
    protected abstract void setUpViewAndData();
    protected void restartApp() {
        Intent intent =new Intent(this,MainActivity.class);
        intent.putExtra(KEY_HOME_ACTION,ACTION_RESTART_APP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // 初始化dialog：设置dialog显示view和dialog的亮度
    private Dialog initDialog() {
        Dialog result = null;
        result = new Dialog(BaseActivity.this, R.style.theme_dialog_alert);
        result.setContentView(R.layout.dialog_layout);
        result.setCancelable(true);
			/*
			 * result.setOnCancelListener(new
			 * android.content.DialogInterface.OnCancelListener() { public void
			 * onCancel(DialogInterface dialog) { BaseActivity.this.finish(); } });
			 */
        WindowManager.LayoutParams lp = result.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        result.getWindow().setAttributes(lp);
        result.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return result;
    }

    // 显示dialog
    public void showLoadingDialog() {
        /**
         * 条件是窗体还没有关闭，并且dialog不为空
         */
        if (!isFinishing() && dialog != null) {
            try {
                /**
                 * 显示对话框
                 */
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *   隐藏dialog条件：判断当前Activity是否已经销毁，dialog是否初始化，dialog是否在显示
     */
    public void hidenDialog() {
        if (!isFinishing() && dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 提示网名错误
     */
    public void showShortToastNetError() {
        FRToast.showToast(this,getString(R.string.string_neterror_toast));
        //toast.show(getString(R.string.string_neterror_toast), Toast.LENGTH_SHORT);
        /*Toast.makeText(getApplicationContext(), "当前网络不可用,请检查您的网络连接!", Toast.LENGTH_SHORT)
                .show();*/
    }

    /**
     * 短时间显示提示
     * @param message
     */
    public void showShortToast(String message) {
        FRToast.showToast(this,message,Toast.LENGTH_SHORT);
       // toast.show(message, Toast.LENGTH_SHORT);
        /*Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();*/
    }

    /**
     * 长时间显示提示
     * @param message
     */
    public void showLongToast(String message) {
        FRToast.showToast(this,message,Toast.LENGTH_LONG);
        //toast.show(message, Toast.LENGTH_LONG);
       /* Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();*/
    }


    /**
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {
        super.finish();
//			overridePendingTransition(R.anim.fade_short_in, R.anim.fade_short_out);
    }

    /**
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//			overridePendingTransition(R.anim.fade_short_in, R.anim.fade_short_out);
    }

    private ProgressDialog tipsDialog;

    /**
     * 显示带文字提示框的方法
     * @param content
     */
    public void showTipsDialog(String content) {
        if (tipsDialog != null) {
            tipsDialog.dismiss();
            tipsDialog = null;
        }
        tipsDialog = ProgressDialog.show(this, "", content, true, true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }
    /**
     * 显示带文字提示框的方法
     * @param content
     */
    public void showTipsDialog(String content, DialogInterface.OnCancelListener onCancelListener) {
        if (tipsDialog != null) {
            tipsDialog.dismiss();
            tipsDialog = null;
        }
     /*   tipsDialog = ProgressDialog.show(this, "", content, true, true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });*/
        tipsDialog = ProgressDialog.show(this, "", content, true, true,onCancelListener);
    }

    /**
     * 隐藏提示旋转框
     */
    public void hideTipsDialog() {
        try {
            // 如果view finish了，此操作可能异常
            if (tipsDialog != null) {
                //隐藏提示框
                tipsDialog.dismiss();
                tipsDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 系统默认的startactivity方法保留
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//			overridePendingTransition(R.anim.fade_short_in, R.anim.fade_short_out);
    }

    /**
     * 重载startActivity
     * @param toClass
     * @param <T>
     */
    public <T> void startActivity(Class<T> toClass) {
        Intent intent = new Intent();
        if(toClass != null){
            intent.setClass(getApplicationContext(), toClass);
            startActivity(intent);
        }
    }
    /**
     * 重载startActivity
     * @param toClass 将要打开的activity
     * @param param 传递的参数
     * @param <T> 泛型
     */
    public <T> void startActivity(Class<T> toClass,Map<String,String> param) {
        Intent intent = new Intent();
        if(param != null){
            /**
             * 遍历map里面的数据
             * 分别放入到intent当中
             */
            Set<Map.Entry<String, String>> entrySet = param.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                /**
                 * 获取相应的键值对
                 */
                String key = next.getKey();
                String value = next.getValue();
                intent.putExtra(key,value);
            }
        }
        if(toClass != null){
            /**
             * 设置intent跳转窗体的信息
             */
            intent.setClass(getApplicationContext(), toClass);
            startActivity(intent);
        }
    }

    private void registerBroadcast() {
        // 注册广播接收者
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("exit_app");
        registerReceiver(receiver,filter);
    }
    private void unregisterBroadcast() {
        // 取消注册广播接收者
        unregisterReceiver(receiver);
    }
    class MyReceiver extends BroadcastReceiver {

        /**
         * @see BroadcastReceiver#onReceive(Context, Intent)
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("exit_app")){
                LogUtils.e("xyd","退出登陆");
                finish();
            }
        }
    }

    /**
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    //自动隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        try {
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 当某个activity变得“容易”被系统销毁时，该activity的onSaveInstanceState就会被执行，
     * 除非该activity是被用户主动销毁的，例如当用户按BACK键的时候
     * 一个原则：即当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用
     * 情景：
     * 1. 当用户按下HOME键时
     * 2. 长按HOME键，选择运行其他的程序时。
     * 3. 按下电源按键（关闭屏幕显示）时。
     * 4. 从activity A中启动一个新的activity时。
     * 5. 屏幕方向切换时，例如从竖屏切换到横屏时。
     * 以上情景触发该函数，并且开发者可以保存一些数据状态
     */
    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        LogUtils.i("HELLO", "HELLO：当Activity被销毁的时候，不是用户主动按back销毁，例如按了home键");
        super.onSaveInstanceState(savedInstanceState);
        try {
            *//*OkHttpClient okHttpClient = RetrofitProvidede.getmOkHttpClient();
            savedInstanceState.putSerializable(getString(R.string.string_bundle_okhttp), (Serializable) okHttpClient);
            BaseBean baseBean = new BaseBean();
            baseBean.setDataObject(retrofit);
            savedInstanceState.putSerializable(getString(R.string.string_bundle_retrofit),baseBean);*//*
            Gson gson = new Gson();
            String toJson = gson.toJson(retrofit);
            savedInstanceState.putString(getString(R.string.string_bundle_retrofit),toJson);

        } catch (Exception e) {
            LogUtils.e("activity保存失败了");

        }
    }*/

    /**
     * onSaveInstanceState方法和onRestoreInstanceState方法“不一定”是成对的被调用的，
     * onRestoreInstanceState被调用的前提是，
     * activity A“确实”被系统销毁了，而如果仅仅是停留在有这种可能性的情况下，
     * 则该方法不会被调用，例如，当正在显示activity A的时候，用户按下HOME键回到主界面，
     * 然后用户紧接着又返回到activity A，这种情况下activity A一般不会因为内存的原因被系统销毁，
     * 故activity A的onRestoreInstanceState方法不会被执行
     */
   /* @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.i("HELLO", "HELLO:如果应用进程被系统咔嚓，则再次打开应用的时候会进入");
        try {
            *//*OkHttpClient okHttpClient = RetrofitProvidede.getmOkHttpClient();
            savedInstanceState.putSerializable(getString(R.string.string_bundle_okhttp), (Serializable) okHttpClient);
            BaseBean baseBean = new BaseBean();
            baseBean.setDataObject(retrofit);
            ArrayList<Parcelable> parcelables = new ArrayList<>();
            savedInstanceState.putSerializable(getString(R.string.string_bundle_retrofit),baseBean);*//*
            Gson gson = new Gson();
            String toJson = gson.toJson(retrofit);
            savedInstanceState.putString(getString(R.string.string_bundle_retrofit),toJson);
        } catch (Exception e) {
            LogUtils.e("activity保存失败了");

        }
    }*/

}
