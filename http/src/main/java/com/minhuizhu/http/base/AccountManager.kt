package com.minhuizhu.http.base

import com.minhuizhu.common.cache.ACache
import com.minhuizhu.common.util.App

import java.io.Serializable

/**
 * Created by zhuminh on 2017/3/16.
 */

class AccountManager private constructor() {

    private var userBaseData: UserBaseData? = null
    val isLogin: Boolean
        get() = null != userBaseData && null != userBaseData!!.uin

    val uin: String?
        get() = userBaseData?.uin
    val loginSession: String?
        get() = userBaseData?.loginSession
    private object inner {
        internal var instance = AccountManager()
    }

    init {
        userBaseData = ACache.get(App.getContext()).getData<UserBaseData>(USER_BASE_DATA)
    }


    private inner class UserBaseData : Serializable {
/*
        id int PRIMARY key AUTO_INCREMENT,
        account varchar(20),
        salt  varchar(32),
        nick_name  varchar(20),
        user_level int ,
        score int ,
        login_session varchar(32)*/

        internal var uin: String? = null
        internal var account: String? = null
        internal var loginSession: String? = null
        internal var salt: String? = null
        internal var nickName: String? = null
        internal var level: String? = null
        internal var score: String? = null
        internal var session: String? = null
    }

    companion object {
        private val USER_BASE_DATA = "user_base_data"
        val instance: AccountManager
            get() = inner.instance
    }
}
