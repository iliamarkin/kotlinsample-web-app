package ru.markin.kotlinsample.rest.balance

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.markin.kotlinsample.domain.transaction.Balance
import ru.markin.kotlinsample.service.security.UserAuthorityService
import ru.markin.kotlinsample.service.transaction.BalanceService
import javax.servlet.http.HttpServletRequest

@RestController
class BalanceRestController {

    @Value("\${jwt.header}")
    private lateinit var tokenHeader: String

    @Autowired
    private lateinit var balanceService: BalanceService

    @Autowired
    private lateinit var userAuthorityService: UserAuthorityService

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @RequestMapping(value = ["/balance"], method = [(RequestMethod.GET)])
    fun getUserBalance(request: HttpServletRequest): Balance {

        val token = request.getHeader(this.tokenHeader)

        val userId = this.userAuthorityService.getCurrentUserId(token)

        return this.balanceService.getUserBalance(userId)
    }
}