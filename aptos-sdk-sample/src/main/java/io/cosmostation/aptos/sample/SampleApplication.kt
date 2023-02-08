package io.cosmostation.aptos.sample

import android.app.Application
import io.cosmostation.aptos.AptosClient
import io.cosmostation.aptos.model.Network

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AptosClient.initialize()
        AptosClient.configure(Network.Devnet())
    }
}