package org.calamarfederal.feature.bmi.data.source.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import java.io.InputStream
import java.io.OutputStream

internal const val HeightWeightStoreFileName = "height-weight-store"

internal val Context.heightMassStore: DataStore<HeightMass> by dataStore(
    fileName = HeightWeightStoreFileName,
    serializer = HeightWeightStoreSerializer,
)

@Serializable
internal data class HeightMass @OptIn(ExperimentalSerializationApi::class) constructor(
    @ProtoNumber(1)
    val meters: Double = 0.00,
    @ProtoNumber(2)
    val grams: Double = 0.00,
)

internal object HeightWeightStoreSerializer : Serializer<HeightMass> {
    override val defaultValue: HeightMass = HeightMass()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): HeightMass {
        return input.use {
            ProtoBuf.decodeFromByteArray(it.readBytes())
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: HeightMass, output: OutputStream) {
        output.use {
            it.write(ProtoBuf.encodeToByteArray(t))
        }
    }
}
