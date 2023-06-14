package com.kinandcarta.create.proxytoggle.repository.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.kinandcarta.create.proxytoggle.datastore.AppData
import java.io.InputStream
import java.io.OutputStream

object AppDataSerializer : Serializer<AppData> {

    override val defaultValue: AppData = AppData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppData {
        return try {
            AppData.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto for AppData.", e)
        }
    }

    override suspend fun writeTo(t: AppData, output: OutputStream) {
        t.writeTo(output)
    }
}
