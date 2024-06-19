package rodrigo.monterrosa.helpd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.Conexion
import java.util.UUID

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Accionadores
        val lblogin = findViewById<TextView>(R.id.lbGoLonIn)
        val btnRegistrarse = findViewById<Button>(R.id.btnCrearC)

        //Campos
        val txtNombre = findViewById<TextView>(R.id.txtNombreR)
        val txtMail = findViewById<TextView>(R.id.txtEmailR)
        val txtUsuario = findViewById<TextView>(R.id.txtUsuarioR)
        val txtContraseña = findViewById<TextView>(R.id.txtContraseñaR)

        //Acción para crear cuenta
        btnRegistrarse.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val objConexion  = Conexion().cadenaConexion()

                val crearCuenta = objConexion?.prepareStatement("insert into Usuario values (?, ?, ?, ?, ?)")!!
                crearCuenta.setString(1, UUID.randomUUID().toString())
                crearCuenta.setInt(2, txtNombre.text.toString().toInt())
                crearCuenta.setString(3, txtMail.text.toString())
                crearCuenta.setString(4, txtUsuario.text.toString())
                crearCuenta.setString(5, txtContraseña.text.toString())
                crearCuenta.executeUpdate()
                finish()
            }
        }

        lblogin.setOnClickListener {
            val pantallaLogin = Intent(this, login::class.java)
            startActivity(pantallaLogin)
            finish()
        }
    }
}