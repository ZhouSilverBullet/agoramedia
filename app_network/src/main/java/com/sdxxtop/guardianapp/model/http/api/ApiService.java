package com.sdxxtop.guardianapp.model.http.api;


import com.sdxxtop.guardianapp.model.bean.AutoLoginBean;
import com.sdxxtop.guardianapp.model.bean.LoginBean;
import com.sdxxtop.guardianapp.model.bean.RequestBean;
import com.sdxxtop.guardianapp.model.bean.RtcRequestBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    String BASE_URL = "http://envir.test.sdxxtop.com/api/";

    //////////首页/////////
    @FormUrlEncoded
    @POST("login/autoLogin")
    Observable<RequestBean<AutoLoginBean>> postLoginAutoLogin(@Field("data") String data);

    @FormUrlEncoded
    @POST("login/mobileLogin")
    Observable<RequestBean<LoginBean>> postLoginMobileLogin(@Field("data") String data);

    @FormUrlEncoded
    @POST("grid/audio/rtm")
    Observable<RequestBean<RtcRequestBean>> postAudioRtc(@Field("userid") String data);
}
