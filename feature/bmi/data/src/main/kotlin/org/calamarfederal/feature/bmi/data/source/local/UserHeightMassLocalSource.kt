package org.calamarfederal.feature.bmi.data.source.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.calamarfederal.feature.bmi.data.source.local.datastore.HeightMass

internal interface UserHeightMassLocalSource {
    /**
     * provides efficient access to latest in storage
     *
     * **do not cache**
     */
    val heightMassFlow: Flow<HeightMass>

    /**
     * Atomically updates underlying value to the output of [heightMassSetter]
     */
    suspend fun updateHeightWeight(heightMassSetter: suspend (HeightMass) -> HeightMass)
}

internal class UserHeightMassLocalSourceImplementation(
    private val heightMassStore: DataStore<HeightMass>,
    private val ioDispatcher: CoroutineDispatcher,
) : UserHeightMassLocalSource {
    override val heightMassFlow: Flow<HeightMass> = heightMassStore.data
    override suspend fun updateHeightWeight(heightMassSetter: suspend (HeightMass) -> HeightMass) {
        withContext(ioDispatcher) {
            heightMassStore.updateData(heightMassSetter)
        }
    }
}
