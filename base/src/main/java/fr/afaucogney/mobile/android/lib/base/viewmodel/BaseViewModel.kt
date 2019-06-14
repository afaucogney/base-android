package fr.afaucogney.mobile.android.lib.base.viewmodel

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.uber.autodispose.LifecycleEndedException
import com.uber.autodispose.LifecycleScopeProvider
import fr.afaucogney.mobile.android.lib.base.viewmodel.event.ViewModelLifeCycleEvent
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import toothpick.Toothpick

abstract class BaseViewModel(app: Application) :
    LifecycleObserver,
    LifecycleScopeProvider<ViewModelLifeCycleEvent>,
    ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val lifecycleSubject: BehaviorSubject<ViewModelLifeCycleEvent> = BehaviorSubject.create()

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////

    init {
        Timber.tag(this.javaClass.simpleName).i("init")
        lifecycleSubject.onNext(ViewModelLifeCycleEvent.ON_CREATED)
        Timber.tag(this.javaClass.simpleName).i("onCreated")
        val viewModelScope = Toothpick.openScopes(app, this)
        Toothpick.inject(this, viewModelScope)
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    @CallSuper
    override fun onCleared() {
        Timber.tag(this.javaClass.simpleName).i("onCleared")
        lifecycleSubject.onNext(ViewModelLifeCycleEvent.ON_CLEARED)
        Toothpick.closeScope(this)
        super.onCleared()
    }

    ///////////////////////////////////////////////////////////////////////////
    // AUTODISPOSE
    ///////////////////////////////////////////////////////////////////////////

    override fun lifecycle(): Observable<ViewModelLifeCycleEvent> = lifecycleSubject.hide()

    override fun peekLifecycle(): ViewModelLifeCycleEvent? = lifecycleSubject.value

    override fun correspondingEvents(): Function<ViewModelLifeCycleEvent, ViewModelLifeCycleEvent> {
        return Function { testLifecycle ->
            when (testLifecycle) {
                ViewModelLifeCycleEvent.ON_CREATED -> ViewModelLifeCycleEvent.ON_CLEARED
                ViewModelLifeCycleEvent.ON_CLEARED -> throw LifecycleEndedException()
            }
        }
    }
}
