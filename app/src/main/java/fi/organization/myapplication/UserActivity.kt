package fi.organization.myapplication



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


/**
 * This displays a list of users retrieved from  API.
 * It can add new users and navigate to a users details.
 */
class UserActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //Initialize UI elements
        listView = findViewById(R.id.listView)
        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        //Click listener for add button
        addButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()

            //Check if input fields are empty
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {

                //Add user and clears the input fields
                Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                firstNameEditText.text.clear()
                lastNameEditText.text.clear()
            } else {

                //Display toast message if input fields are empty
                Toast.makeText(this, "Please fill out form", Toast.LENGTH_SHORT).show()
            }
        }

        //Initialize adapter and set it to ListView
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, ArrayList<String>())
        listView.adapter = adapter

        //Get list of users and displays them
        showUsers()
    }

    /**
    * Retrieves a list of users from API and displays it in UI
    */
    private fun showUsers() {
        thread {
            val url = getUrl("https://dummyjson.com/users/")
            val map = ObjectMapper()

            //Turn JSON response into a UserJsonObject using ObjectMapper
            val myObject: UserJsonObject = map.readValue(url, UserJsonObject::class.java)
            val persons: MutableList<Person>? = myObject.users
            val names: MutableList<String> = mutableListOf()
            val personDetails: MutableList<Person> = mutableListOf()

            //Go through each person from the API response
            persons?.forEach { person ->
                //Creates a name string
                names.add(person.firstName + " " + person.lastName)
                personDetails.add(person)
            }

            runOnUiThread {

                //Update listview with user names
                adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
                listView.adapter = adapter

                //Handle click event on ListView
                listView.setOnItemClickListener { _, _, position, _ ->
                    val selectedPerson = personDetails[position]

                    //Go to UserDetailsActivity and pass selected details using intent
                    val intent = Intent(this, UserDetailsActivity::class.java)
                    intent.putExtra("firstName", selectedPerson.firstName)
                    intent.putExtra("lastName", selectedPerson.lastName)
                    intent.putExtra("age", selectedPerson.age)
                    intent.putExtra("phone", selectedPerson.phone)
                    intent.putExtra("image", selectedPerson.image)
                    startActivity(intent)
                }
            }
        }
    }

    /**
    * Function fetches data from a URL and return it as a string
    */
    private fun getUrl(url: String) : String {
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




