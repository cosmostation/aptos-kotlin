package io.cosmostation.aptos.model

import com.google.gson.annotations.SerializedName

data class SubmitRequest(
    @SerializedName("sender") var sender: String,
    @SerializedName("sequence_number") var sequence_number: String,
    @SerializedName("max_gas_amount") var max_gas_amount: String,
    @SerializedName("gas_unit_price") var gas_unit_price: String,
    @SerializedName("expiration_timestamp_secs") var expiration_timestamp_secs: String,
    @SerializedName("payload") var payload: Payload,
    @SerializedName("signature") var signature: Signature
)

data class EncodeRequest(
    @SerializedName("sender") var sender: String,
    @SerializedName("sequence_number") var sequence_number: String,
    @SerializedName("max_gas_amount") var max_gas_amount: String,
    @SerializedName("gas_unit_price") var gas_unit_price: String,
    @SerializedName("expiration_timestamp_secs") var expiration_timestamp_secs: String,
    @SerializedName("payload") var payload: Payload,
)

data class Payload(
    @SerializedName("type") var type: String,
    @SerializedName("function") var function: String,
    @SerializedName("type_arguments") var type_arguments: List<String>,
    @SerializedName("arguments") var arguments: List<String>?,
)

data class Signature(
    @SerializedName("type") var type: String,
    @SerializedName("public_key") var public_key: String,
    @SerializedName("signature") var signature: String,
)

data class Account(var sequence_number: String, var authentication_key: String)

data class AccountResource(var AccountResources: AccountResources)

data class AccountResources(
    @SerializedName("type") var type: String,
    @SerializedName("data") var data: Data
)

data class Data(
    @SerializedName("authentication_key") var authentication_key: String,
    @SerializedName("coin_register_events") var coin_register_events: coinRegisterEvents,
    @SerializedName("self_address") var self_address: String,
    @SerializedName("coin") var coin: Coin,
    @SerializedName("deposit_events") var deposit_events: depositEvents,
    @SerializedName("withdraw_events") var withdraw_events: withdrawEvents,
    @SerializedName("sequence_number") var sequence_number: String
)

data class Coin(
    @SerializedName("value") var value: String
)

data class depositEvents(
    @SerializedName("counter") var counter: String,
    @SerializedName("guid") var guid: id,
)

data class withdrawEvents(
    @SerializedName("counter") var counter: String,
    @SerializedName("guid") var guid: id,
)

data class coinRegisterEvents(
    @SerializedName("counter") var counter: String,
    @SerializedName("guid") var guid: id
)

data class id(
    @SerializedName("addr") var addr: String,
    @SerializedName("creation_num") var creation_num: String
)

data class Transaction(
    val type: String,
    val hash: String,
    val sender: String,
    val sequence_number: String,
    val max_gas_amount: String,
    val gas_unit_price: String,
    val expiration_timestamp_secs: String,
    val payload: Payload,
    val signature: Signature
)
