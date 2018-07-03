package ru.markin.kotlinsample.model.transaction

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tr_history")
class Transaction {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tr_history_sequence")
    @SequenceGenerator(name = "tr_history_sequence",
            sequenceName = "tr_history_sequence",
            allocationSize = 1)
    var id: Int = 0

    @Column(name = "USER_ID")
    var userId: Int = 0

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    var category: TransactionCategory = TransactionCategory()

    @Column(name = "TRANSACTION_DATE")
    @Temporal(value = TemporalType.TIMESTAMP)
    var transactionDate: Date = Date()

    @Column(name = "SUM")
    var sum: Double = 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}