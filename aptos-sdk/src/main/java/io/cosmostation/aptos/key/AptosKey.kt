package io.cosmostation.aptos.key

import com.develop.mnemonic.MnemonicUtils
import io.cosmostation.aptos.model.EdDSAKeyPair
import net.i2p.crypto.eddsa.EdDSAEngine
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import net.i2p.crypto.eddsa.Utils
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec
import java.security.MessageDigest
import java.security.Signature
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object AptosKey {
    private const val MAC_SECRET_KEY = "ed25519 seed"
    private const val HMAC_SHA512_ALGORITHM_KEY = "HmacSHA512"
    private const val MESSAGE_DIGEST_ALGORITHM_SHA3_256 = "SHA3-256"
    private val Aptos_HD_PATH = listOf(44, 637, 0, 0, 0)
    private const val Aptos_ADDRESS_LENGTH = 64

    fun getAptosAddress(mnemonic: String, path: List<Int> = Aptos_HD_PATH): String {
        val keyPair = getKeyPair(mnemonic, path)
        val digest: MessageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM_SHA3_256)
        val digestBytes: ByteArray = digest.digest(keyPair.publicKey.abyte + ByteArray(1))
        val hex = Utils.bytesToHex(digestBytes)
        return "0x${hex.substring(0, Aptos_ADDRESS_LENGTH)}"
    }

    fun getKeyPair(mnemonic: String, path: List<Int> = Aptos_HD_PATH): EdDSAKeyPair {
        val seedBytes: ByteArray = MnemonicUtils.generateSeed(mnemonic, "")
        var pair = shaking(seedBytes, MAC_SECRET_KEY.toByteArray())
        path.forEach {
            val buffer = ByteArray(size = 4)
            write4BytesToBuffer(buffer,0, 0x80000000 + it)
            val pathBuffer = ByteArray(size = 1) + pair.first + buffer
            pair = shaking(pathBuffer, pair.second)
        }

        val keySpecs = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519)
        val privateKeySpec = EdDSAPrivateKeySpec(pair.first, keySpecs)
        val pubKeySpec = EdDSAPublicKeySpec(privateKeySpec.a, keySpecs)
        val publicKey = EdDSAPublicKey(pubKeySpec)
        val privateKey = EdDSAPrivateKey(privateKeySpec)
        return EdDSAKeyPair(privateKey, publicKey)
    }

    fun sign(keyPair: EdDSAKeyPair, data: ByteArray): ByteArray {
        val spec: EdDSAParameterSpec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519)
        val signature: Signature = EdDSAEngine(MessageDigest.getInstance(spec.hashAlgorithm))
        signature.initSign(keyPair.privateKey)
        signature.update(data)
        return signature.sign()
    }

    private fun shaking(key: ByteArray, chain: ByteArray): Pair<ByteArray, ByteArray> {
        val mac = Mac.getInstance(HMAC_SHA512_ALGORITHM_KEY)
        val secretKeySpec = SecretKeySpec(chain, HMAC_SHA512_ALGORITHM_KEY)
        mac.init(secretKeySpec)
        val result = mac.doFinal(key)
        return Pair(result.sliceArray(0..31), result.sliceArray(32..63))
    }

    private fun write4BytesToBuffer(buffer: ByteArray, offset: Int, data: Long) {
        buffer[offset + 0] = (data shr 24 and 0xff).toByte()
        buffer[offset + 1] = (data shr 16 and 0xff).toByte()
        buffer[offset + 2] = (data shr 8 and 0xff).toByte()
        buffer[offset + 3] = (data shr 0 and 0xff).toByte()
    }
}