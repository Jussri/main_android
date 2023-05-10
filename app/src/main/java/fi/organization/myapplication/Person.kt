package fi.organization.myapplication

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//Data class Person with properties firstName and lastName.
@JsonIgnoreProperties(ignoreUnknown = true)
data class Person(
    var firstName: String? = null, var lastName: String? = null
)