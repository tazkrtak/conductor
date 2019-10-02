package com.tazkrtak.conductor.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.tazkrtak.conductor.R
import com.tazkrtak.conductor.util.Hash
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        login_button.setOnClickListener {

            val db = FirebaseFirestore.getInstance()
            val id = bus_id_edit_text.text.toString()
            val password = password_edit_text.text.toString()

            if (id.isEmpty()) {
                bus_id_edit_text.error = getString(R.string.id_required)
                return@setOnClickListener
            } else if (password.isEmpty()) {
                password_edit_text.error = getString(R.string.password_required)
                return@setOnClickListener
            }

            db.collection("buses")
                .document(id)
                .get().addOnSuccessListener {
                    if (!it.exists()) {
                        bus_id_edit_text.error = getString(R.string.id_error)
                        return@addOnSuccessListener
                    }
                    if (Hash.sha512(password) != it["password"].toString()) {
                        password_edit_text.error = getString(R.string.password_error)
                        return@addOnSuccessListener
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                }
        }

    }


}
