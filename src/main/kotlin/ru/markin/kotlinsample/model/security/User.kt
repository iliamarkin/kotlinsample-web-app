package ru.markin.kotlinsample.model.security

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.collections.HashSet

@Entity
@Table(name = "USERS")
class User(
        @Id
        @Column(name = "ID")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
        @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
        var id: Int = 0,

        @Column(name = "USERNAME", length = 50, unique = true)
        @Size(min = 4, max = 50)
        var username: String = "",

        @Column(name = "PASS", length = 100)
        @Size(min = 6, max = 100)
        var password: String = "",

        @Column(name = "FIRSTNAME", length = 50)
        @Size(min = 2, max = 50)
        var firstName: String = "",

        @Column(name = "LASTNAME", length = 50)
        @Size(min = 2, max = 50)
        var lastName: String = "",

        @Column(name = "EMAIL", length = 50)
        @Size(min = 4, max = 50)
        var email: String = "",

        @Column(name = "ENABLED")
        @NotNull
        var enabled: Boolean = false,

        @Column(name = "LASTPASSRESETDATE")
        @Temporal(TemporalType.TIMESTAMP)
        var lastPasswordResetDate: Date = Date(),

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "USER_AUTHORITY",
                joinColumns = [(JoinColumn(name = "USER_ID", referencedColumnName = "ID"))],
                inverseJoinColumns = [(JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID"))])
        var authorities: Set<Authority> = HashSet()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}