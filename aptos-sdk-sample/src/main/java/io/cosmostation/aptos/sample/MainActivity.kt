package io.cosmostation.aptos.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.cosmostation.aptos.AptosClient
import io.cosmostation.aptos.model.EncodeRequest
import io.cosmostation.aptos.model.Payload
import io.cosmostation.aptos.model.Signature
import io.cosmostation.aptos.model.SubmitRequest
import io.cosmostation.aptos.sample.databinding.ActivityMainBinding
import io.cosmostation.aptos.util.byteToHex
import io.cosmostation.aptos.util.decodeHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val preloadMnemonic =
        "sketch olympic symptom divorce regret biology strong ancient corn liar repair dust"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        binding.newMnemonic.setOnClickListener {
            loadMnemonic(AptosClient.instance.generateMnemonic())
        }

        binding.loadMnemonic.setOnClickListener {
            loadMnemonic(preloadMnemonic)
        }

        binding.getAccount.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AptosClient.api.getAccount(binding.address.text.toString()).execute().body()
            }
        }

        binding.getAccountResources.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AptosClient.api.getAccountResources(binding.address.text.toString()).execute().body()
            }
        }

        binding.getTransactions.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AptosClient.api.getAccountTransactions(binding.address.text.toString()).execute()
                    .body()
            }
        }

        binding.transfer.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val sender = binding.address.text.toString()
                val receiver = binding.address.text.toString()
                val mnemonic = binding.mnemonic.text.toString()
                val amount = "1000"
                val gasAmount = "101600"
                val gasPrice = "100"
                val expirationTime = Date().time + 1000 * 60
                val payload = Payload(
                    "entry_function_payload",
                    "0x1::coin::transfer",
                    listOf("0x1::aptos_coin::AptosCoin"),
                    listOf(receiver, amount)
                )
                val account = AptosClient.api.getAccount(sender).execute().body()
                val encodeRequest = EncodeRequest(
                    sender,
                    account!!.sequence_number,
                    gasAmount,
                    gasPrice,
                    expirationTime.toString(),
                    payload
                )
                val encodeResult = AptosClient.api.encodeSubmission(encodeRequest).execute().body()
                val keyPair = AptosClient.instance.getKeyPair(mnemonic)
                val signed = AptosClient.instance.sign(
                    keyPair, encodeResult!!.substringAfter("0x").decodeHex()
                )
                val signature = Signature(
                    "ed25519_signature",
                    "0x" + keyPair.publicKey.abyte.byteToHex(),
                    "0x" + signed.byteToHex()
                )
                val submitRequest = SubmitRequest(
                    sender,
                    account!!.sequence_number,
                    gasAmount,
                    gasPrice,
                    expirationTime.toString(),
                    payload,
                    signature
                )
                val result = AptosClient.api.submitTransaction(submitRequest).execute().body()
            }
        }
    }

    private fun loadMnemonic(mnemonic: String) {
        binding.mnemonic.text = mnemonic
        binding.address.text = AptosClient.instance.getAddress(mnemonic)
    }
}
