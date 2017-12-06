package com.sinosoft.fhcs.android.activity

import android.content.Intent
import com.sinosoft.fhcs.android.R
import com.sinosoft.fhcs.android.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth_web.*
class AuthWebActivity : BaseActivity() {


    override fun setUpViewAndData() {
        setContentView(R.layout.activity_auth_web)
        initView()
    }

    override fun onResume() {
        super.onResume()
        wv_auth.onResume()
    }

    override fun onPause() {
        super.onPause()
        wv_auth.onPause()
    }

    private fun initView() {
        val deviceSn = intent.getStringExtra("deviceSn")
        wv_auth.setBindResultListener { p0 ->
            var intent = Intent()
            intent.putExtra("result", p0)
            setResult(2, intent)
            finish()
        }
        wv_auth.setBindResultNewListener { p0, p1 ->
            var intent = Intent()
            intent.putExtra("result", p0)
            setResult(2, intent)
            finish()
        }
        wv_auth.setDeviceSn(deviceSn)
    }

    override fun onDestroy() {
        super.onDestroy()
        wv_auth.destroy()
    }


}
