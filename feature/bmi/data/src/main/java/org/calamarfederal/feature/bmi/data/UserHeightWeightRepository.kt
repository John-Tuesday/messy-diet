package org.calamarfederal.feature.bmi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import org.calamarfederal.messydiet.measure.Length
import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.meters
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

internal const val HeightWeightStoreFileName = "height-weight-store"

internal val Context.heightWeightStore: DataStore<HeightWeight> by dataStore(
    fileName = HeightWeightStoreFileName,
    serializer = HeightWeightStoreSerializer,
)

@Serializable
data class HeightWeight @OptIn(ExperimentalSerializationApi::class) constructor(
    @ProtoNumber(1)
    val meters: Double = 0.00,
    @ProtoNumber(2)
    val grams: Double = 0.00,
)

object HeightWeightStoreSerializer : Serializer<HeightWeight> {
    override val defaultValue: HeightWeight = HeightWeight()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): HeightWeight {
        return input.use {
            ProtoBuf.decodeFromByteArray(it.readBytes())
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: HeightWeight, output: OutputStream) {
        output.use {
            it.write(ProtoBuf.encodeToByteArray(t))
        }
    }
}

interface UserHeightLocalSource {
    val heightWeightFlow: Flow<HeightWeight>
    suspend fun updateHeightWeight(heightWeightSetter: (HeightWeight) -> HeightWeight)
}

class UserHeightLocalSourceImplementation @Inject constructor(
    private val heightWeightStore: DataStore<HeightWeight>,
) : UserHeightLocalSource {
    override val heightWeightFlow: Flow<HeightWeight> = heightWeightStore.data
    override suspend fun updateHeightWeight(heightWeightSetter: (HeightWeight) -> HeightWeight) {
        heightWeightStore.updateData(heightWeightSetter)
    }
}

interface UserHeightWeightRepository {
    val heightFlow: Flow<Length>
    val weightFlow: Flow<Weight>

    suspend fun setHeight(length: Length)
    suspend fun setWeight(weight: Weight)
}

class UserHeightRepositoryImplementation @Inject constructor(
    private val heightWeightLocalSource: UserHeightLocalSource,
) : UserHeightWeightRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val heightFlow: Flow<Length> = heightWeightLocalSource.heightWeightFlow.mapLatest { it.meters.meters }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val weightFlow: Flow<Weight> = heightWeightLocalSource.heightWeightFlow.mapLatest { it.grams.grams }

    override suspend fun setHeight(length: Length) {
        heightWeightLocalSource.updateHeightWeight { it.copy(meters = length.inMeters()) }
    }

    override suspend fun setWeight(weight: Weight) {
        heightWeightLocalSource.updateHeightWeight { it.copy(grams = weight.inGrams()) }
    }
}
