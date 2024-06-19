package rodrigo.monterrosa.helpd

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import oracle.net.jdbc.TNSAddress.Description

class detalleTicket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titulo = intent.getStringExtra("Titulo")
        val id = intent.getStringExtra("IdTicket")
        val nombre = intent.getStringExtra("Nombre")
        val email = intent.getStringExtra("Email")
        var estado = intent.getStringExtra("Estado")
        val descripcion = intent.getStringExtra("Descripcion")

        if (estado=="F"){
            estado = "Finalizado"
        }else estado = "Activo"

        findViewById<TextView>(R.id.txtTituloD).text = "$titulo"
        findViewById<TextView>(R.id.txtIdD).text = "$id"
        findViewById<TextView>(R.id.txtNombreD).text = "Autor: $nombre"
        findViewById<TextView>(R.id.txtEmailD).text = "Email: $email"
        findViewById<TextView>(R.id.txtEstado).text = "Estado: $estado"
        findViewById<TextView>(R.id.txtDescripcionD).text = "Descripción: $descripcion"

    }
}