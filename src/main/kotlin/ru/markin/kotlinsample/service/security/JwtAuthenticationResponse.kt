package ru.markin.kotlinsample.service.security

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class JwtAuthenticationResponse(
        @JsonProperty(value = "access_token") val accessToken: String
) : Serializable