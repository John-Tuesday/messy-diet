package org.calamarfederal.messydiet.food.data.central.di

import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider

internal val testDi = DI {
    import(backendDiModule, allowOverride = true)

    bindProvider(tag = NETWORK_DISPATCHER_TAG, overrides = true) { Dispatchers.Default }
}
