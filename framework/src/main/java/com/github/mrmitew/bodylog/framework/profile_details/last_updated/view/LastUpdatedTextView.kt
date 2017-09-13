package com.github.mrmitew.bodylog.framework.profile_details.last_updated.view

import android.app.Application
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.intent.GetProfileLastUpdatedIntent
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.model.LastUpdatedTextState
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.presenter.LastUpdatedPresenter
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.view.LastUpdatedView
import com.github.mrmitew.bodylog.framework.common.presenter.BasePresenterHolder
import com.github.mrmitew.bodylog.framework.common.view.BasePresentableTextView
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector
import io.reactivex.Observable
import javax.inject.Inject

class LastUpdatedTextView : BasePresentableTextView<LastUpdatedView, LastUpdatedTextState>, LastUpdatedView {
    class PresenterHolder(application: Application) : BasePresenterHolder<LastUpdatedView, LastUpdatedTextState>(application) {
        @Inject
        override lateinit var presenter: LastUpdatedPresenter

        override fun injectMembers(injector: PresenterHolderInjector) {
            injector.inject(this)
        }
    }

    override val view: LastUpdatedView = this

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun injectPresenterHolder(): PresenterHolder {
        return ViewModelProviders.of(context as AppCompatActivity).get(PresenterHolder::class.java)
    }

    override fun getProfileLastUpdatedIntent(): Observable<GetProfileLastUpdatedIntent>
            = Observable.just(GetProfileLastUpdatedIntent())

    override fun render(state: LastUpdatedTextState) {
        if (state.error != StateError.Empty.INSTANCE) {
            // TODO: 9/6/17
            println("render: ${state.error}")
        }

        text = state.lastUpdated
    }
}
