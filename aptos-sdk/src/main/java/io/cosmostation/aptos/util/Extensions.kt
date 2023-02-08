package io.cosmostation.aptos.util

import net.i2p.crypto.eddsa.Utils

fun String.decodeHex(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    val byteIterator = chunkedSequence(2).map { it.toInt(16).toByte() }.iterator()

    return ByteArray(length / 2) { byteIterator.next() }
}

fun ByteArray.byteToHex(): String {
    return Utils.bytesToHex(this)
}