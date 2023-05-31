package fi.organization.myapplication


import android.content.Intent
import com.bumptech.glide.Glide
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * This displays the details of a user
 * and it allows you to delete or update the user's details.
 */
class UserDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        //Initialize UI elements
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

        //Click listener for delete button
        deleteButton.setOnClickListener {
            deleteButton()
        }

        //Click listener for update button
        updateButton.setOnClickListener {
            updateButton()
        }
    }

    /**
    * Show confirmation dialog for deleting user
    */
    private fun deleteButton() {
        AlertDialog.Builder(this)
        .setMessage("Are you sure you want to delete this user?")
        .setPositiveButton("Yes") { _, _ ->

            //Display toast message after deleting user
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()

            //Return to user activity page
            val intent = Intent(this, UserActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        .setNegativeButton("No", null)
        .show()
    }

    /**
    * Shows a dialog box for editing user details and updates the UI
    */
    private fun updateButton() {

        //Inflate layout for dialog box
        val updateView = View.inflate(this, R.layout.edit_user_details, null)

        //Initialize UI elements
        val firstNameEdit = updateView.findViewById<EditText>(R.id.edit_firstName)
        val lastNameEdit = updateView.findViewById<EditText>(R.id.edit_lastName)
        val ageEdit = updateView.findViewById<EditText>(R.id.edit_age)
        val phoneEdit = updateView.findViewById<EditText>(R.id.edit_phone)

        //Dialog box
        AlertDialog.Builder(this)
            .setTitle("Edit User Details")
            .setView(updateView)
            .setPositiveButton("Save") { _, _ ->

                //When save button is clicked, retrieve the updated user details
                val updatedFirstName = firstNameEdit.text.toString()
                val updatedLastName = lastNameEdit.text.toString()
                val updatedAge = "Age: " +  ageEdit.text.toString()
                val updatedPhone = "Phone: " +  phoneEdit.text.toString()

                //Update the UI with the updated details
                val firstNameTextView = findViewById<TextView>(R.id.user_firstName)
                val lastNameTextView = findViewById<TextView>(R.id.user_lastName)
                val ageTextView = findViewById<TextView>(R.id.user_age)
                val phoneTextView = findViewById<TextView>(R.id.user_phone)

                firstNameTextView.text = updatedFirstName
                lastNameTextView.text = updatedLastName
                ageTextView.text = updatedAge
                phoneTextView.text = updatedPhone

                //Display toast message after deleting user
                Toast.makeText(this,
                    "User details have been updated", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}