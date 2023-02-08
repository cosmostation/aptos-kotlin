package io.cosmostation.aptos.model

sealed class Network(val name: String, val rpcUrl: String) {
    class Devnet : Network("Devnet", "https://fullnode.devnet.aptoslabs.com")
    class Mainnet : Network("Mainnet", "https://rpc.mainnet.aptos.fernlabs.xyz/")
}