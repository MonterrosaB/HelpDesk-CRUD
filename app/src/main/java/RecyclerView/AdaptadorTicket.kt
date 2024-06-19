package RecyclerView

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.Conexion
import modelo.dtTicket
import rodrigo.monterrosa.helpd.R
import rodrigo.monterrosa.helpd.detalleTicket

class AdaptadorTicket(var Datos: List<dtTicket>):RecyclerView.Adapter<ViewHolderTicket>() {

    fun actualizarListaDespuesDeEditar(id: String, nEstado: String){
        val index = Datos.indexOfFirst{it.idTicket == id}
        Datos[index].estado = nEstado
        notifyItemChanged(index)
    }

    fun actualizarEstado( uuid: String, nuevoEstado: String){
        GlobalScope.launch(Dispatchers.IO){
            //conexion
            val objConexion = Conexion().cadenaConexion()

            //2- Creo una variable que contenga un PrepareStatement
            val updateCancion = objConexion?.prepareStatement("update Ticket set Estado = ? where IdTicket = ?")!!
            updateCancion.setString(1, nuevoEstado)
            updateCancion.setString(2, uuid)
            updateCancion.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()

        }
    }

        fun eliminarDatos(Titulo: String, posicion: Int){
            val listaDatos = Datos.toMutableList()
            listaDatos.removeAt(posicion)

            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = Conexion().cadenaConexion()

                //PrepareStatement para elimiar
                val dTicket = objConexion?.prepareStatement("delete from Ticket where Titulo = ?")!!
                dTicket.setString(1,Titulo)
                dTicket.executeUpdate()

                val commit = objConexion.prepareStatement("commit")
                commit.executeUpdate()
            }
            Datos = listaDatos.toList()
            notifyItemRemoved(posicion)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int): ViewHolderTicket {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        return ViewHolderTicket(vista)
    }

    override fun onBindViewHolder(holder: ViewHolderTicket, position: Int) {

        val item = Datos[position]
        holder.lbIdC.text = item.idTicket
        holder.lbTituloC.text = item.titulo
        holder.lbAutorC.text = item.nombre
        holder.lbEmailC.text = item.email

        if (item.estado == "A") {
            holder.caEstado.setCardBackgroundColor(Color.parseColor("#0a544b")) // Example color for "A"
        } else {
            holder.caEstado.setCardBackgroundColor(Color.parseColor("#808080")) // Example color for other states
        }

        holder.icBorrar.setOnClickListener{
            //contexto
            val context = holder.itemView.context

            //alerta
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro que desear borrar el ticket?")

            builder.setPositiveButton("Si"){ dialog, wich ->
                eliminarDatos(item.titulo, position)
            }
            builder.setNegativeButton("No"){ dialog, wich ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

            holder.icEditar.setOnClickListener{
                //contexto
                val context = holder.itemView.context

                //creamos la alerta con su titulo y mensaje
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Editar Ticket")
                builder.setMessage("Actualiza el estado:")

                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL

                //spinner
                val estadoSpinner = Spinner(context)
                val opcionesSpinner = arrayOf("Activo", "Finalizado")
                estadoSpinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, opcionesSpinner)
                val estadoPosicion = opcionesSpinner.indexOf(item.estado)
                if (estadoPosicion >= 0) {
                    estadoSpinner.setSelection(estadoPosicion)
                }
                layout.addView(estadoSpinner)
                builder.setView(layout)

                //Guardamos los cambios
                builder.setPositiveButton("Guardar Cambio") {
                        dialog, which ->
                    var nuevoEstado = estadoSpinner.selectedItem.toString()
                    if(nuevoEstado == "Activo"){
                        nuevoEstado = "A"
                    }
                    else nuevoEstado = "F"
                    actualizarEstado(item.idTicket, nuevoEstado)
                    actualizarListaDespuesDeEditar(item.idTicket, nuevoEstado )
                }

                builder.setNegativeButton("Cancelar") {
                        dialog, which ->
                    dialog.dismiss()
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val pantalla = Intent(context, detalleTicket::class.java)
            pantalla.putExtra(
                "Titulo", item.titulo
            )
            pantalla.putExtra(
                "IdTicket", item.idTicket
            )
            pantalla.putExtra(
                "Nombre", item.nombre
            )
            pantalla.putExtra(
                "Email", item.email
            )
            pantalla.putExtra(
                "Estado", item.estado
            )
            pantalla.putExtra(
                "Descripcion", item.descripcion
            )
            context.startActivity(pantalla)

        }
        }
    override fun getItemCount() = Datos.size

    fun actualizarDatos(nuevaLista: List<dtTicket>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }
    }

