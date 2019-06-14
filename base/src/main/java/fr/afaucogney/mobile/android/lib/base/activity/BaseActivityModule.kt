package fr.afaucogney.mobile.android.lib.base.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import fr.afaucogney.mobile.android.lib.base.activity.provider.LifecycleRegistryProvider
import toothpick.config.Module

class BaseActivityModule(activity: AppCompatActivity) : Module() {
    init {
        bind(LifecycleRegistry::class.java)
            .toProviderInstance(LifecycleRegistryProvider(activity as LifecycleOwner))
    }
}
