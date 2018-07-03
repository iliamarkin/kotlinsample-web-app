package ru.markin.kotlinsample.domain.transaction

data class TransactionCategory(

        var id: Int = 0,

        var income: Boolean = true,

        var name: String = ""
)