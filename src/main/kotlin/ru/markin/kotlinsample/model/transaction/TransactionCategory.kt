package ru.markin.kotlinsample.model.transaction

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "tr_category")
class TransactionCategory {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tr_category_sequence")
    @SequenceGenerator(name = "tr_category_sequence",
            sequenceName = "tr_category_sequence",
            allocationSize = 1)
    var id: Int = 0

    @Column(name = "INCOME", length = 32)
    var income: Boolean = true

    @Column(name = "NAME", length = 100, unique = true)
    @Size(min = 4, max = 100)
    var name: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransactionCategory

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}