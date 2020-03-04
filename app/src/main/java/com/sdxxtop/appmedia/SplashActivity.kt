package com.sdxxtop.appmedia

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sdxxtop.appmedia.model.AccountModel
import com.sdxxtop.guardianapp.model.bean.AutoLoginBean
import com.sdxxtop.guardianapp.model.http.callback.IRequestCallback

class SplashActivity : AppCompatActivity(), IRequestCallback<AutoLoginBean?> {

    val splashModel by lazy {
        AccountModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }

        setContentView(R.layout.activity_splash)


        splashModel.autoLogin(this)

    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onSuccess(t: AutoLoginBean?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onFailure(code: Int, error: String?) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
