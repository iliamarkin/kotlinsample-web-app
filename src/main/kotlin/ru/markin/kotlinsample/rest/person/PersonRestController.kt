package ru.markin.kotlinsample.rest.person

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

data class Person(var name: String = "", var email: String = "")

@RestController
class PersonRestController {

    private var persons: MutableList<Person> = ArrayList()

    init {
        persons.add(Person("Hello", "World"))
        persons.add(Person("Foo", "Bar"))
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @RequestMapping(path = ["/persons"], method = [(RequestMethod.GET)])
    fun getPersons(): List<Person> {
        return persons
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @RequestMapping(path = ["/persons/{name}"], method = [(RequestMethod.GET)])
    fun getPerson(@PathVariable("name") name: String): Person? {
        return persons.firstOrNull { person -> name.equals(person.name, ignoreCase = true) }
    }
}