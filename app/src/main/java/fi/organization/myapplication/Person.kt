package fi.organization.myapplication

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//Data class Person with properties.
@JsonIgnoreProperties(ignoreUnknown = true)
data class Person(
    var firstName: String? = null, var lastName: String? = null,
    var image: String? = null,
    var age: String? = null, var phone: String? = null
)