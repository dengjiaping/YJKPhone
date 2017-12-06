package com.sinosoft.fhcs.android.activity.bottomfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.HttpManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sinosoft.fhcs.android.util.HttpManager.miao_shop_url_classifya;

public class ShopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.web_shop)
    WebView wvShop;
    Unbinder unbinder;
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String phoneNumber;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onResume() {
        titlebarTvTitle.setText("健康商城");
        // 从首选项获取userId
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "UserInfo", Context.MODE_PRIVATE);
        phoneNumber = prefs.getString("phoneNumber", "");
        if (phoneNumber == null || phoneNumber.equals("")) {
            FRToast.showToast(getActivity().getApplicationContext(), "您没有绑定过电话号码，请绑定之后再试！");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(miao_shop_url_classifya);
        builder.append("?user_id=" + phoneNumber);
        builder.append("&open_appid=mphmhch7a3zgcvbce0");
        String urlStr = builder.toString().trim();

        if (HttpManager.isNetworkAvailable(getActivity())) {

            String ua = wvShop.getSettings().getUserAgentString();
            wvShop.getSettings().setUserAgentString("TestUserAgent");
            wvShop.getSettings().setJavaScriptEnabled(true);
            wvShop.getSettings().setUseWideViewPort(true);
            wvShop.requestFocus();
            wvShop.getSettings().setSupportZoom(true);
            wvShop.getSettings().setDomStorageEnabled(true);
            String dir = wvShop.getContext().getDir("database", this.getActivity().MODE_PRIVATE).getPath();
            wvShop.getSettings().setDatabasePath(dir);
//        wvShop.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
            wvShop.setWebViewClient(new MyWebViewClient());
            wvShop.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            wvShop.getSettings().setDefaultTextEncodingName("UTF-8");
            wvShop.loadUrl(urlStr);
            wvShop.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                }
            });


        } else {
            // Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
            Toast.makeText(getActivity(), "您的网络没连接好，请检查后重试！",
                    Toast.LENGTH_SHORT).show();
        }

        super.onResume();


    }

    final class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //TLog.e("test", "url Override =====" + url + "  time: " + sDateFormat.format(new java.util.Date()));
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        // 页面出错
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            FRToast.showToast(getActivity().getApplicationContext(), "页面出错了");
        }
    }
}
