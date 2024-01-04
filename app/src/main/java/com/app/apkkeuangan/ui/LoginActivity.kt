package com.app.apkkeuangan.ui

import SharedPref
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.app.apkkeuangan.database.AppDatabase
import com.app.apkkeuangan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //deklarasi database untuk memanggil database
        database = AppDatabase.getInstance(this)
        sharedPref = SharedPref(this)

        //menjalankan klik, button login
        binding.btnSignIn.setOnClickListener {
            //val variabel yang tidak akan pernah berubah valuenya
            // var bisa di rubah valuenya
            val username = binding.etUsernameSignIn.text.toString().trim()
            val password = binding.etPasswordSignIn.text.toString().trim()
            val user = database.userDao().loginUser(username,password)
            if (CheckValidation(username,password)) {
                let {
                    user.observe(this, Observer {
                        if (it == null) {
                            Toast.makeText(this, "Masukkan Email Dan Password secara benar", Toast.LENGTH_SHORT).show()
                        } else {
                            sharedPref.setLogIn(true)
                            val username = user.value?.username.toString()
                            val uid = user.value?.uid.toString()
                            sharedPref.setUsername(username)
                            sharedPref.setUid(uid)
                            Toast.makeText(this, "yeah berhasil login", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                    })
                }
            }


        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }
    private fun CheckValidation(username: String, pass: String): Boolean {
        if(username.isEmpty()){
            binding.etUsernameSignIn.error = "Masukkan Username Anda"
            binding.etUsernameSignIn.requestFocus()
        } else if (pass.isEmpty()) {
            binding.etPasswordSignIn.error = "Masukkan Password Anda"
            binding.etPasswordSignIn.requestFocus()
        } else {
            binding.etPasswordSignIn.error = null
            return true
        }
        Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
        return false
    }
}