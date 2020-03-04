package com.sdxxtop.appmedia.model

import com.sdxxtop.guardianapp.model.http.net.RetrofitHelper

abstract class BaseModel {

    fun getApi() = RetrofitHelper.getApi()
}