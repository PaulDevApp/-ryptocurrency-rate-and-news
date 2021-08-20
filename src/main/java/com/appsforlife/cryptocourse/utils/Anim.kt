package com.appsforlife.cryptocourse.utils

import android.view.View

fun setAnim(view: View, long: Long) {
    view.alpha = 0f
    view.animate().setDuration(long).alpha(1.0f).start()
}