package ru.markin.kotlinsample.domain.transaction

import com.fasterxml.jackson.annotation.JsonProperty

data class Transaction(

        @JsonProperty(value = "user_id")
        var userId: Int = 0,

        var category: TransactionCategory = TransactionCategory(),

        @JsonProperty(value = "transaction_date")
        var transactionDate: Long = 0,

        var sum: Double = 0.0
)