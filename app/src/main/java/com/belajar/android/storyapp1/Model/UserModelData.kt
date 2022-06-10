package com.belajar.android.storyapp1.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModelData(
    val token: String,
    val isLogin: Boolean
):Parcelable

@Parcelize
data class Stories(
    var photoUrl: String? = null,
    var name: String? = null,
    var description: String? = null
):Parcelable
