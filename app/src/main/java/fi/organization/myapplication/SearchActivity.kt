package fi.organization.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * With this you can search for users based on first or last name
 * and display the search results in the UI.
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var searchBox: EditText
    private lateinit var searchResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Initialize UI elements
        searchBox = findViewById(R.id.search_box)
        searchResult = findViewById(R.id.search_result)

        //Click listener for search button
        findViewById<Button>(R.id.search_button).setOnClickListener {

            //Get the search text from the search box
            val searchText = searchBox.text?.toString() ?: ""

            //If the search text is not empty,
            //start a background thread to fetch data from URL
            if (searchText.isNotEmpty()) {
                thread {

                    //HTTP GET request to API URL and retrieve the JSON response
                    val url = getUrl("https://dummyjson.com/users/")
                    val map = ObjectMapper()

                    //Turn JSON response into a UserJsonObject using ObjectMapper
                    val myObject: UserJsonObject = map.readValue(url, UserJsonObject::class.java)
                    val persons: MutableList<Person>? = myObject.users

                    //Filter the list of persons based
                    //on whether first or last name contains the search text
                    var names = ""
                    persons?.filter { it.firstName?.contains(searchText,
                        ignoreCase = true) == true ||
                            it.lastName?.contains(searchText, ignoreCase = true) == true
                    }?.forEach { person ->
                        names += person.firstName + " " + person.lastName + "\n"
                    }

                    runOnUiThread {

                        //Update UI with search results matching names
                        searchResult.text = names
                    }
                }
            } else {

                //Display toast message if search box is empty
                Toast.makeText(this, "Text field empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Function fetches data from a URL and return it as a string
     */
    private fun getUrl(url: String): String {
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection
        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        var input = ""

        //Read the response and add it to input string
        reader.use {
            var line = it.readLine()

            while (line != null) {
                input += line
                line = it.readLine()
            }
        }
        reader.close()
        return input
    }
}
