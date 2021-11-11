package com.example.facebookloginactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONException
import java.util.*

/*
Referred links :
https://www.metizsoft.com/blog/facebook-login-integration-in-android
https://developers.facebook.com/docs/facebook-login/android/
**/

class FacebookLoginActivity : AppCompatActivity() {

    lateinit var button: LoginButton
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fb_login)
        button = findViewById(R.id.login_btn)
        callbackManager = CallbackManager.Factory.create()
        //sets the permission to read the corresponding data from facebook
        button.setPermissions(
            listOf(
                "email",
                "public_profile"
            )
        )
        checkLoginStatus()
        //This facebook login button gives a callback whether login is successful or not
        button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            //If the result is success we get the user details by using access token
            override fun onSuccess(result: LoginResult?) {
                loadUserProfile(result?.accessToken)
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkLoginStatus() {
        //Checks whether user has been already logged in or not
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken())
        }
    }

    //Function to load data of the user
    private fun loadUserProfile(currentAccessToken: AccessToken?) {
        val request = GraphRequest.newMeRequest(
            currentAccessToken
        ) { `object`, response ->
            try {
                // Getting details from the GraphRequest object
                val firstName = `object`?.getString("first_name")
                val lastName = `object`?.getString("last_name")
                val email = `object`?.getString("email")
                val id = `object`?.getString("id")
                val imageUrl = "https://graph.facebook.com/$id/picture?type=large";

                val bundle = Bundle()
                bundle.putString("firstName", firstName)
                bundle.putString("lastName", lastName)
                bundle.putString("email", email)
                bundle.putString("image", imageUrl)
                moveToDetailsActivity(bundle)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email")
        request.parameters = parameters
        //executing graph request to get data from facebook API
        request.executeAsync()
    }

    private fun moveToDetailsActivity(bundle: Bundle) {
        Intent(this@FacebookLoginActivity, DetailsActivity::class.java).also {
            it.putExtra("info", bundle)
            startActivity(it)
        }
    }

    //Callback registers when clicking on the button
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}