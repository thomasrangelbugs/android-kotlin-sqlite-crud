package com.example.aplicacaodbapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacaodbapp.R
import com.example.aplicacaodbapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.login_title)

        binding.buttonLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val usuario = binding.editTextUsuario.text?.toString()?.trim().orEmpty()
        val senha = binding.editTextSenha.text?.toString()?.trim().orEmpty()

        binding.layoutUsuario.error = null
        binding.layoutSenha.error = null

        var hasError = false

        if (usuario.isBlank()) {
            binding.layoutUsuario.error = getString(R.string.required_field)
            hasError = true
        }

        if (senha.isBlank()) {
            binding.layoutSenha.error = getString(R.string.required_field)
            hasError = true
        }

        if (hasError) {
            return
        }

        if (usuario == DEFAULT_USER && senha == DEFAULT_PASSWORD) {
            startActivity(Intent(this, ItemListActivity::class.java))
            finish()
        } else {
            Snackbar.make(binding.root, R.string.login_error, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val DEFAULT_USER = "admin"
        private const val DEFAULT_PASSWORD = "1234"
    }
}

