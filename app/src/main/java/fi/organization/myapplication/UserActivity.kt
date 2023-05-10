package fi.organization.myapplication



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class UserActivity : AppCompatActivity() {

    private lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        textView = findViewById(R.id.textView)
        showUsers()
    }

    //Retrieves the list of users from the API and displays it in the UI
    private fun showUsers() {
        thread {
            val url = getUrl("https://dummyjson.com/users/")
            val mp = ObjectMapper()
            val myObject: UserJsonObject = mp.readValue(url, UserJsonObject::class.java)
            val persons: MutableList<Person>? = myObject.users
            var names = ""
            persons?.forEach { person ->
                names += person.firstName + " " + person.lastName + "\n"
            }
            runOnUiThread {
                textView.text = names
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









