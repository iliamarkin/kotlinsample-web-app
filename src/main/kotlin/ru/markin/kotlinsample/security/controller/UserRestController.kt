package ru.markin.kotlinsample.security.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.markin.kotlinsample.security.JwtTokenUtil
import ru.markin.kotlinsample.security.util.JwtUser
import javax.servlet.http.HttpServletRequest

@RestController
class UserRestController {

    @Value("\${jwt.header}")
    private lateinit var tokenHeader: String

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private lateinit var userDetailsService: UserDetailsService

    @RequestMapping(value = ["user"], method = [(RequestMethod.GET)])
    fun getAuthenticatedUser(request: HttpServletRequest): JwtUser {
        val token = request.getHeader(this.tokenHeader).substring(7)
        val username = this.jwtTokenUtil.getUsernameFromToken(token)
        return this.userDetailsService.loadUserByUsername(username) as JwtUser
    }
}