package com.androdu.bosta.data.api.model

import java.io.Serializable

data class User(
    val address: Address,
    val company: Company,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String,
) : Serializable {
    val formattedAddress: String
        get() = "${address.street}, ${address.suite}, ${address.city}, ${address.zipcode}"
}