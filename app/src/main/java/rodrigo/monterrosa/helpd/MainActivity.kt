package rodrigo.monterrosa.helpd

import RecyclerView.AdaptadorTicket
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.Conexion
import modelo.dtTicket
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pantallaCarga = Intent(this, loading::class.java)
        startActivity(pantallaCarga)

        val idUser = intent.getStringExtra("IdUsuario")


        val txtTitulo = findViewById<TextView>(R.id.txtTitulo)
        val txtDescripcion = findViewById<TextView>(R.id.txtDescripcion)
        val btnAgregar = findViewById<Button>(R.id.btnCrear)

        val rcvTicket =findViewById<RecyclerView>(R.id.rcvTicket)

        rcvTicket.layoutManager =LinearLayoutManager(this)

        fun mostrarTicket(): List<dtTicket>{
            val objConexion = Conexion().cadenaConexion()

            val statement =objConexion?.prepareStatement("SELECT * FROM Ticket INNER JOIN Usuario ON Ticket.IdUsuario = Usuario.IdUsuario" +
                    " WHERE Usuario.IdUsuario = ?")!!
            statement.setString(1, idUser)

            val resultSet = statement.executeQuery()

            val tickets = mutableListOf<dtTicket>()

            while (resultSet.next()){
                val idTicket = resultSet.getString("IdTicket")
                val titulo = resultSet.getString("Titulo")
                val descripcion = resultSet.getString("Descripcion")
                val nombre = resultSet.getString("Nombre")
                val email = resultSet.getString("Email")
                val estado = resultSet.getString("Estado")
                val ticket = dtTicket(idTicket, titulo, descripcion, nombre, email, estado)
                tickets.add(ticket)
            }
            return tickets
        }

        CoroutineScope(Dispatchers.IO).launch {
            val ticektsDB = mostrarTicket()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorTicket(ticektsDB)
                rcvTicket.adapter = adapter
            }
        }

        btnAgregar.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion =Conexion().cadenaConexion()

                val addTicket =objConexion?.prepareStatement("Insert into Ticket (IdTicket, Titulo, Descripcion, IdUsuario, Estado) VALUES(?, ?, ?, ?, 'A')")!!

                addTicket.setString(1, UUID.randomUUID().toString())
                addTicket.setString(2, txtTitulo.text.toString())
                addTicket.setString(3, txtDescripcion.text.toString())
                addTicket.setString(4, idUser.toString())
                addTicket.executeUpdate()

                val nuevosTickets = mostrarTicket()
                withContext(Dispatchers.Main) {
                    (rcvTicket.adapter as? AdaptadorTicket)?.actualizarDatos(nuevosTickets)
                    txtTitulo.setText("")
                    txtDescripcion.setText("")
                }
            }
        }

    }
}