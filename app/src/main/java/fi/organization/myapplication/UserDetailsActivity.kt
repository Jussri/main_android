package fi.organization.myapplication


import android.content.Intent
import com.bumptech.glide.Glide
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class UserDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val firstNameTextView = findViewById<TextView>(R.id.user_firstName)
        val lastNameTextView = findViewById<TextView>(R.id.user_lastName)
        val ageTextView = findViewById<TextView>(R.id.user_age)
        val phoneTextView = findViewById<TextView>(R.id.user_phone)
        val userImageView = findViewById<ImageView>(R.id.user_image)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        val updateButton = findViewById<Button>(R.id.update_button)

        //Retrieve user details from intent
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val age = "Age: " + intent.getStringExtra("age")
        val phone = "Phone: " +  intent.getStringExtra("phone")
        val imageUrl = intent.getStringExtra("image")

        //Set user details to UI elements
        firstNameTextView.text = firstName
        lastNameTextView.text = lastName
        ageTextView.text = age
        phoneTextView.text = phone

        //Load user image using Glide
        Glide.with(this)
            .load(imageUrl)
            .into(userImageView)

        //Set click listener for delete button
        deleteButton.setOnClickListener {
            showDeleteButton()
        }

        //Set click listener for update button
        updateButton.setOnClickListener {
            showUpdateButton()
        }
    }

    //Show confirmation dialog for deleting user
    private fun showDeleteButton() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this user?")
        builder.setPositiveButton("Yes") { _, _ ->

            //Perform toast for deleting user
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()

            //Return to user activity page
            val intent = Intent(this, UserActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun showUpdateButton() {
        Toast.makeText(this, "User information updated", Toast.LENGTH_SHORT).show()
    }

}