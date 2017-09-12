package com.github.mrmitew.bodylog.framework.di.activity

import android.support.v4.app.FragmentActivity

import dagger.MembersInjector

interface ActivityComponent<A : FragmentActivity> : MembersInjector<A>
