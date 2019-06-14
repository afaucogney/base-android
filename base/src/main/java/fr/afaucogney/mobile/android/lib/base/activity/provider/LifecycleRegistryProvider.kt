package fr.afaucogney.mobile.android.lib.base.activity.provider

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import javax.inject.Provider

class LifecycleRegistryProvider(var activity: LifecycleOwner) :
    Provider<LifecycleRegistry> {

    override fun get(): LifecycleRegistry = LifecycleRegistry(activity)
}
