package ru.markin.kotlinsample.service.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.domain.transaction.Balance
import ru.markin.kotlinsample.repository.transaction.BalanceRepository

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
class BalanceService {

    @Autowired
    private lateinit var balanceRepository: BalanceRepository

    @Transactional
    fun getUserBalance(userId: Int): Balance {

        val balance = this.balanceRepository.findById(userId)

        return Balance(userId, balance.map { it.balance }.orElse(0.0))
    }

    @Transactional
    fun addToBalance(userId: Int, sum: Double): Balance {

        val balance = this.balanceRepository.findById(userId)

        return if (balance.isPresent) {
            val updatedBalance = this.balanceRepository.save(balance.get().apply {
                this.balance += sum
            })
            Balance(userId, updatedBalance.balance)
        } else {
            val savedBalance = this.balanceRepository
                    .save(ru.markin.kotlinsample.model.transaction.Balance().apply {
                        this.id = userId
                        this.balance += sum
                    })
            Balance(savedBalance.id, savedBalance.balance)
        }
    }
}