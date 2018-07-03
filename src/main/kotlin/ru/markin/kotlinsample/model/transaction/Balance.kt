package ru.markin.kotlinsample.model.transaction

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "BALANCE")
class Balance {

    @Id
    @Column(name = "USER_ID")
    var id: Int = 0

    @Column(name = "CURRENT_BALANCE")
    var balance: Double = 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Balance

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}