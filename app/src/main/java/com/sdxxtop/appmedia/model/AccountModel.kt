package com.sdxxtop.appmedia.model

import com.sdxxtop.guardianapp.app.Constants
import com.sdxxtop.guardianapp.model.bean.AutoLoginBean
import com.sdxxtop.guardianapp.model.bean.LoginBean
import com.sdxxtop.guardianapp.model.bean.RequestBean
import com.sdxxtop.guardianapp.model.http.callback.IRequestCallback
import com.sdxxtop.guardianapp.model.http.net.Params
import com.sdxxtop.guardianapp.model.http.util.RxUtils
import com.sdxxtop.guardianapp.model.http.util.SpUtil
import io.reactivex.Observable

class AccountModel : BaseModel() {
    fun autoLogin(callback: IRequestCallback<AutoLoginBean?>?) {
        val params = Params()
        params.put("at", SpUtil.getString(Constants.AUTO_TOKEN))
        val observable = getApi().postLoginAutoLogin(params.data)
        val disposable = RxUtils.handleDataHttp<AutoLoginBean>(observable, object :
            IRequestCallback<AutoLoginBean?> {
            override fun onSuccess(autoLoginBean: AutoLoginBean?) {
                callback?.onSuccess(autoLoginBean)
            }

            override fun onFailure(code: Int, error: String) {
                callback?.onFailure(code, error)
            }
        })
        //释放观察者
//        addSubscribe(disposable)
    }

    fun login(mobile: String, authCode: String, callback: IRequestCallback<LoginBean?>?) {
        val params =
            Params()
        params.put("mb", mobile)
        params.put("ac", authCode)
        params.put("pi", "0")
        val requestBeanObservable = getApi().postLoginMobileLogin(params.data)
        val disposable = RxUtils.handleDataHttp(
            requestBeanObservable,
            object : IRequestCallback<LoginBean?> {
                override fun onSuccess(loginBean: LoginBean?) {

                    callback?.onSuccess(loginBean)
                }

                override fun onFailure(code: Int, error: String) {
                    callback?.onFailure(code, error)
                }
            })
        //释放观察者
//        addSubscribe(disposable)
    }
}