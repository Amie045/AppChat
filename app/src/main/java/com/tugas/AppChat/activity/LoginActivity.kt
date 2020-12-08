package com.tugas.AppChat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.tugas.AppChat.MainActivity
import com.tugas.AppChat.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setTextChangedListener(edt_email, til_email)
        setTextChangedListener(edt_password, til_password)
        progress_layout.setOnTouchListener { v , event -> true }

        btn_login.setOnClickListener{
            onLogin()
        }

        txt_signup.setOnClickListener {
            onSignup()
        }
    }



    private fun setTextChangedListener(edt: TextInputEditText?, til: TextInputLayout?) {
        edt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                til?.isErrorEnabled = false
            }

        })
    }


    private fun onLogin() {
        var proceed = true
        if (edt_email.text.isNullOrEmpty()){
            til_email.error = "Membutuhkan Email"
            til_email.isErrorEnabled = true
            proceed = false
        }

        if (edt_password.text.isNullOrEmpty()){
            til_password.error = "Membutuhkan Password"
            til_password.isErrorEnabled = true
            proceed = false
        }

        if (proceed) {
            progress_layout.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(
                edt_email.text.toString(),
                edt_password.text.toString()
            )
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        progress_layout.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Login Error: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                .addOnFailureListener {e ->
                    progress_layout.visibility = View.GONE
                    e.printStackTrace()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener (firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener (firebaseAuthListener)
    }

    private fun onSignup() {
        startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        finish()
    }
}