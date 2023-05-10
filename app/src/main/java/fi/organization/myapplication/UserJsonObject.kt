package fi.organization.myapplication

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// Data class representing a JSON object containing a mutable list of Person objects
@JsonIgnoreProperties(ignoreUnknown = true)
data class UserJsonObject(
    var users: MutableList<Person>? = null
)