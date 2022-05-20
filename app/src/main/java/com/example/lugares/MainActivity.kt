package com.example.lugares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.lugares.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding //Con binding se accede a los elementos de la parte visual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this) //Inicializa el Firebase
        auth = Firebase.auth //Variable de autenticacion

        //Metodo para invokar login
        binding.btLogin.setOnClickListener{
            haceLogin();
        }
        //Metodo para invokar registro
        binding.btRegister.setOnClickListener{
            haceRegister();
        }
    }

    private fun haceRegister() {
        //Jala los datos de los text box
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Registro

        auth.createUserWithEmailAndPassword(email,clave) //Metodo que ya existe para authentication
            .addOnCompleteListener(this){ //Tarea asíncrona
            task ->
            if(task.isSuccessful){ //Si funca
                Log.d("Creando user...","¡Registrado pa!")
                val user = auth.currentUser //Recupera los datos del user
                actualiza(user) //Valida si el user se creo y pasar a la aplicacion
            }else{ //Si no funca
                Log.d("Creando user...","¡Hubo error y falló!")
                Toast.makeText(baseContext,"Falló!", Toast.LENGTH_LONG).show()
                actualiza(null)
            }
        }
    }

    private fun actualiza(user: FirebaseUser?) {
        if (user!=null){ //Si el user es diferente de null
            val intent = Intent(this, Principal::class.java) //Intent para mostrar la siguiente pantalla
            startActivity(intent)
        }
    }

    //Una vez autenticado no vuelve a pedir credenciales, a menos que se haga logout
    public override fun onStart() {
        super.onStart()
        val  usuario = auth.currentUser
        actualiza(usuario)
    }

    private fun haceLogin() {
        //Jala los datos de los text box
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Login, casi lo mismo que register

        auth.signInWithEmailAndPassword(email,clave) //Metodo que ya existe para hacer login
            .addOnCompleteListener(this){ //Async
                task ->
            if(task.isSuccessful){
                Log.d("Authenticando","¡Logueado pa!")
                val user = auth.currentUser
                actualiza(user)
            }else{
                Log.d("Autenticando","¡Hubo error y falló!")
                Toast.makeText(baseContext,"Falló!", Toast.LENGTH_LONG).show()
                actualiza(null)
            }
        }
    }
}