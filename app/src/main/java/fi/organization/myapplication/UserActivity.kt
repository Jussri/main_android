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

class UserActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        listView = findViewById(R.id.listView)
        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        //Click listener for add button
        addButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()

            //Check if text fields are empty
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                firstNameEditText.text.clear()
                lastNameEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter first name and last name", Toast.LENGTH_SHORT).show()
            }
        }

        //Initialize adapter and set it to ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList<String>())
        listView.adapter = adapter

        showUsers()
    }

    //Retrieves a list of users from API and displays it in UI
    private fun showUsers() {
        thread {
            val url = getUrl("https://dummyjson.com/users/")
            val mp = ObjectMapper()
            val myObject: UserJsonObject = mp.readValue(url, UserJsonObject::class.java)
            val persons: MutableList<Person>? = myObject.users
            val names: MutableList<String> = mutableListOf()
            val personDetails: MutableList<Person> = mutableListOf()

            persons?.forEach { person ->
                names.add(person.firstName + " " + person.lastName)
                personDetails.add(person)
            }

            runOnUiThread {

                //Update adapter with user names
                adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
                listView.adapter = adapter

                //Handle item click event on ListView
                listView.setOnItemClickListener { _, _, position, _ ->
                    val selectedPerson = personDetails[position]

                    //Go to UserDetailsActivity and pass selected user details via intent
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

    //Function fetches data from a URL and return it as a string
    private fun getUrl(url: String) : String {
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection
        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        var input = ""

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









