package ru.markin.kotlinsample.model.security

import javax.persistence.*
import javax.validation.constraints.NotNull

enum class AuthorityName {
    ROLE_USER, ROLE_ADMIN
}

@Entity
@Table(name = "AUTHORITY")
class Authority {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_sequence")
    @SequenceGenerator(name = "authority_sequence", sequenceName = "authority_sequence", allocationSize = 1)
    var id: Int = 0

    @Column(name = "NAME", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    var name: AuthorityName = AuthorityName.ROLE_USER

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    var users: Set<User> = HashSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}