package io.cosmostation.aptos

import com.develop.mnemonic.MnemonicUtils
import io.cosmostation.aptos.api.ApiService
import io.cosmostation.aptos.key.AptosKey
import io.cosmostation.aptos.model.EdDSAKeyPair
import io.cosmostation.aptos.model.Network
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class AptosClient {
    companion object {
        val instance = AptosClient()
        val api: ApiService
            get() = ApiService.create()

        fun initialize() {
            setupBouncyCastle()
        }

        fun configure(network: Network) {
            instance.currentNetwork = network
        }

        private fun setupBouncyCastle() {
            val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) ?: return

            if (provider::class.java == BouncyCastleProvider::class.java) {
                return
            }

            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
            Security.insertProviderAt(BouncyCastleProvider(), 1)
        }
    }

    var currentNetwork: Network = Network.Mainnet()

    fun generateMnemonic(): String = MnemonicUtils.generateMnemonic()

    fun getAddress(mnemonic: String) = AptosKey.getAptosAddress(mnemonic)

    fun getKeyPair(mnemonic: String) = AptosKey.getKeyPair(mnemonic)

    fun sign(keyPair: EdDSAKeyPair, data: ByteArray) = AptosKey.sign(keyPair, data)
}
