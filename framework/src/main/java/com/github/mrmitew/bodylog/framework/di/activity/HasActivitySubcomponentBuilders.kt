package com.github.mrmitew.bodylog.framework.di.activity

import android.app.Activity

interface HasActivitySubcomponentBuilders {
    fun getActivityComponentBuilder(activityClass: Class<out Activity>): ActivityComponentBuilder<*, *>?
}