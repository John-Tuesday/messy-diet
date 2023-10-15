package org.calamarfederal.feature.bmi.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.calamarfederal.feature.bmi.data.source.local.UserHeightMassLocalSource
import org.calamarfederal.physical.measurement.*

/**
 * Locally backed storage for user's Height and Mass
 */
interface UserHeightMassRepository {
    /**
     * provides efficient access to latest in storage
     *
     * **do not cache**
     */
    val heightFlow: Flow<Length>

    /**
     * provides efficient access to latest in storage
     *
     * **do not cache**
     */
    val massFlow: Flow<Mass>

    /**
     * Atomically updates stored height to [length]
     */
    suspend fun setHeight(length: Length)

    /**
     * Atomically updates stored mass to [mass]
     */
    suspend fun setMass(mass: Mass)
}

internal class UserHeightRepositoryImplementation(
    private val heightMassLocalSource: UserHeightMassLocalSource,
) : UserHeightMassRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val heightFlow: Flow<Length> = heightMassLocalSource.heightMassFlow.mapLatest { it.meters.meters }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val massFlow: Flow<Mass> = heightMassLocalSource.heightMassFlow.mapLatest { it.grams.grams }

    override suspend fun setHeight(length: Length) {
        heightMassLocalSource.updateHeightWeight { it.copy(meters = length.inMeters()) }
    }

    override suspend fun setMass(mass: Mass) {
        heightMassLocalSource.updateHeightWeight { it.copy(grams = mass.inGrams()) }
    }
}
