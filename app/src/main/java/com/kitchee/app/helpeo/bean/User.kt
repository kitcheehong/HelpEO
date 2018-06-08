package com.kitchee.app.helpeo.bean



/**
 * Created by kitchee on 2018/5/31.
 * desc : user用户信息
 */

data class User(val user_id: Int = 0, val nickName: String? = null, val realName: String? = null, val age: Int = 0, val sex: Int = 0, val lastLoginTime: Long = 0, val registerTime: Long = 0)


