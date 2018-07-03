package ru.markin.kotlinsample.domain.transaction

import com.fasterxml.jackson.annotation.JsonIgnore

data class Balance(

        @JsonIgnore
        var userId: Int = 0,

        var balance: Double = 0.0
)