package fr.afaucogney.mobile.android.lib.base.activity

import android.app.Application
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import fr.afaucogney.mobile.android.lib.base.viewmodel.ViewModelFactory
import timber.log.Timber
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.smoothie.module.SmoothieActivityModule
import toothpick.smoothie.module.SmoothieSupportActivityModule

abstract class BaseActivity :
    AppCompatActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    protected abstract val injectionModule: Module

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    @get:LayoutRes
    protected abstract val layoutResourceId: Int

    ///////////////////////////////////////////////////////////////////////////
    // LYFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    @CallSuper
    override fun onStart() {
        Timber.tag(this.javaClass.simpleName).i("onStart")
        super.onStart()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(this.javaClass.simpleName).i("onCreate")
        super.onCreate(savedInstanceState)
        beforeLayoutSetup()
        setContentView(layoutResourceId)
        this.afterLayoutSetup()
    }

    @CallSuper
    open fun beforeLayoutSetup() {
        injectDependencies()
    }

    @CallSuper
    open fun afterLayoutSetup() {
    }

    @CallSuper
    override fun onResume() {
        Timber.tag(this.javaClass.simpleName).i("onResume")
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        Timber.tag(this.javaClass.simpleName).i("onPause")
        super.onPause()
    }

    @CallSuper
    override fun onDestroy() {
        Timber.tag(this.javaClass.simpleName).i("onDestroy")
        Toothpick.closeScope(this)
        super.onDestroy()
    }

    @CallSuper
    override fun onStop() {
        Timber.tag(this.javaClass.simpleName).i("onStop")
        super.onStop()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // HELPER
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun injectDependencies() {
        val activityScope = Toothpick.openScopes(application, this)
        activityScope.installModules(
            SmoothieSupportActivityModule(this),
            SmoothieActivityModule(this),
            BaseActivityModule(this),
            injectionModule
        )
        Toothpick.inject(this, activityScope)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // EXTENSIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun <T : ViewModel> AppCompatActivity.getViewModel(viewModelClass: Class<T>): T =
        ViewModelProviders
            .of(
                this,
                Toothpick
                    .openScopes(application as Application)
                    .getInstance(ViewModelFactory::class.java)
            )
            .get(viewModelClass)
}
