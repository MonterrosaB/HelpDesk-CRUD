package rodrigo.monterrosa.helpd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.Conexion
import modelo.dtLogin

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Elementos
        val lbRegistrarse = findViewById<TextView>(R.id.lbRegisterP)
        val btnInicar = findViewById<Button>(R.id.btnIniciarS)

        val txtUser = findViewById<EditText>(R.id.txtUsuarioL)
        val txtPassword = findViewById<EditText>(R.id.txtContraseñaL)

        btnInicar.setOnClickListener{

            val pantallaPrincipal = Intent(this, MainActivity::class.java)

            CoroutineScope(Dispatchers.IO).launch {

                val objConexion = Conexion().cadenaConexion()

                val loginValidacion = objConexion?.prepareStatement("select * from Usuario where NUser = ? and Contraseña = ?")!!

                loginValidacion.setString(1,txtUser.text.toString())
                loginValidacion.setString(2,txtPassword.text.toString())

                val resultado = loginValidacion.executeQuery()

                val loginD = mutableListOf<dtLogin>()

                if (resultado.next()) {
                    val idUsuario =(resultado.getString("IdUsuario"))
                    val datos = dtLogin(idUsuario)

                    loginD.add(datos)

                    pantallaPrincipal.putExtra("IdUsuario", idUsuario)
                    startActivity(pantallaPrincipal)

                    txtUser.setText("")
                    txtPassword.setText("")
                }
                else{
                    println("Usuario no econtrado")
                }
            }
        }

        lbRegistrarse.setOnClickListener {
            val pRegistro = Intent(this ,register::class.java)
            startActivity(pRegistro)
        }
    }
}