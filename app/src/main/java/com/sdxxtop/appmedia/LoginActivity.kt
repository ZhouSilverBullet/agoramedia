package com.sdxxtop.appmedia

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdxxtop.appmedia.control.DelTextWatcher
import com.sdxxtop.appmedia.model.AccountModel
import com.sdxxtop.guardianapp.app.Constants
import com.sdxxtop.guardianapp.model.bean.LoginBean
import com.sdxxtop.guardianapp.model.http.callback.IRequestCallback
import com.sdxxtop.guardianapp.model.http.util.SpUtil
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), IRequestCallback<LoginBean?>, Handler.Callback {

    val splashModel by lazy {
        AccountModel()
    }
    val mHandler by lazy {
        Handler(this)
    }

    var isSending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        et_phone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        var mobile: String = SpUtil.getString(Constants.MOBILE)
        if (TextUtils.isEmpty(mobile)) {
            mobile = ""
        }
        et_phone.setText(mobile)
        et_phone.setSelection(mobile.length)


        btn_login.setOnClickListener(View.OnClickListener { toLogin() })


        et_phone.addTextChangedListener(DelTextWatcher(et_phone, iv_phone_del))
        et_code.addTextChangedListener(DelTextWatcher(et_code, iv_code_del))

        fl_code.setOnClickListener(View.OnClickListener { sendCode() })
    }

    private fun sendCode() {

        val trim: String = et_phone.getText().toString().trim({ it <= ' ' })
        if (TextUtils.isEmpty(trim)) {
            showToast("请输入正确的手机号码")
            return
        }
        if (!isSending) {
            isSending = true
            sendCodeSuccess()
        }
    }

    private fun handleCode(value: Int) {
        if (tv_code == null || ll_code == null) {
            return
        }
        if (isSending) {
            tv_time.setText(value.toString())
            tv_code.setVisibility(View.INVISIBLE)
            ll_code.setVisibility(View.VISIBLE)
        } else {
            tv_code.setVisibility(View.VISIBLE)
            ll_code.setVisibility(View.INVISIBLE)
        }
    }

    private fun toLogin() {
        val trim: String = et_phone.getText().toString().trim({ it <= ' ' })
        if (TextUtils.isEmpty(trim)) {
            showToast("请输入正确的手机号码")
            return
        }

        val code: String = et_code.getText().toString().trim({ it <= ' ' })
        if (TextUtils.isEmpty(code)) {
            showToast("验证码不能为空")
            return
        }

        login(trim, code)
    }

    private fun login(phone: String, code: String) {
        splashModel.login(phone, code, this)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(t: LoginBean?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onFailure(code: Int, error: String?) {
        showToast(error ?: "")
    }

    override fun handleMessage(msg: Message): Boolean {
        val value = msg.obj as Int
        if (tv_code != null) {
            handleCode(value)
        }

        if (value == 0) {
            isSending = false
            handleCode(value)
        } else {
            val message = Message.obtain()
            message.what = 100
            message.obj = value - 1
            mHandler.sendMessageDelayed(message, 1000)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeMessages(100)
    }

    /**
     * 成功发送验证码
     */
    fun sendCodeSuccess() {
        mHandler.obtainMessage(100, 60).sendToTarget()
    }

    /**
     * 发送验证码失败
     */
    fun sendCodeError() {
        isSending = false
    }
}
