<img src="https://user-images.githubusercontent.com/85468864/217445684-125f0495-6f45-4c2d-b7ac-ed25a28477af.png" width="100%">

# Aptos-swift
https://github.com/cosmostation/aptos-swift

# Overview
This is an Aptos SDK that anyone can develop a wallet. It contributes to lowering entry barriers and vitalizing communities and networks. The quickest and easiest way to interact with Aptos!

# Features
This repository contains core functionality needed to create Aptos Wallet.
1. Anyone can create a wallet easily and quickly.
2. It can be customized as the user wants.
3. Lower entry barriers to wallet development.

# Requirements
* Android API 26
* Kotlin

# Dependency
```
implement 'com.github.cosmostation:aptos-kotlin:0.0.1'
```

# Initialize
Initialize for security
```kotlin
AptosClient.initialize()
```

# Description
Using API like below.
```kotlin
AptosClient.instance.{API}
```

Generate new mnemonic
```kotlin
fun generateMnemonic(): String
```

Get address from mnemonic
```kotlin
fun getAddress(mnemonic: String): String
```

Sign data bytearray with KeyPair
```kotlin
fun getKeyPair(mnemonic: String): EdDSAKeyPair
```

Generate signature
```kotlin
fun sign(keyPair: EdDSAKeyPair, data: ByteArray)
```

Get account
```kotlin
AptosClient.api.getAccount(address).execute()
```

Get accountResources
```kotlin
AptosClient.api.getAccountResources(address).execute()
```

Get account transactions
```kotlin
AptosClient.api.getAccountTransactions(address).execute()
```

Encode submission
```kotlin
AptosClient.api.encodeSubmission(encodeRequestMessage).execute()
```

Submit transaction
```kotlin
AptosClient.api.submitTransaction(submitRequestMessage).execute()
```
