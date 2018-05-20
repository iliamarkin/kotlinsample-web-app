package ru.markin.kotlinsample.security.util

import com.fasterxml.jackson.annotation.JsonProperty

data class AccountCredentials(
        var username: String = "",
        var password: String = ""
)

data class Account(
        var credentials: AccountCredentials = AccountCredentials(),

        @JsonProperty(value = "first_name")
        var firstName: String = "",

        @JsonProperty(value = "last_name")
        var lastName: String = "",

        var email: String = ""
)