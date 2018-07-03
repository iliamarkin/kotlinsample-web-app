package ru.markin.kotlinsample.rest.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.markin.kotlinsample.domain.transaction.Balance
import ru.markin.kotlinsample.domain.transaction.Transaction
import ru.markin.kotlinsample.domain.transaction.TransactionCategory
import ru.markin.kotlinsample.service.security.UserAuthorityService
import ru.markin.kotlinsample.service.transaction.TransactionService
import javax.servlet.http.HttpServletRequest

@RestController
class TransactionRestController {

    @Value("\${jwt.header}")
    private lateinit var tokenHeader: String

    @Autowired
    private lateinit var userAuthorityService: UserAuthorityService

    @Autowired
    private lateinit var transactionService: TransactionService

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(value = ["/transactions/add"], method = [(RequestMethod.POST)])
    fun addTransaction(request: HttpServletRequest, @RequestBody transaction: Transaction): Balance {

        val token = request.getHeader(this.tokenHeader)

        val userId = this.userAuthorityService.getCurrentUserId(token)

        return this.transactionService
                .addTransaction(transaction.apply { this.userId = userId })
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(value = ["/transactions/history/{page}"], method = [(RequestMethod.GET)])
    fun getTransactionsHistory(request: HttpServletRequest, @PathVariable page: Int): List<Transaction> {

        val token = request.getHeader(this.tokenHeader)

        val userId = this.userAuthorityService.getCurrentUserId(token)

        return this.transactionService.getTransactionHistory(userId, page)
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(value = ["/transactions/categories"], method = [(RequestMethod.GET)])
    fun getTransactionCategories(): List<TransactionCategory> {

        return this.transactionService.getTransactionCategories()
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @RequestMapping(value = ["/transactions/categories/add"], method = [(RequestMethod.POST)])
    fun addTransactionCategory(@RequestBody category: TransactionCategory): Int {

        return this.transactionService.addTransactionCategory(category)
    }
}