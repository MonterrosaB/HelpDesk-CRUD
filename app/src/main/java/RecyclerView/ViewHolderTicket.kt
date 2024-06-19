package RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import rodrigo.monterrosa.helpd.R

class ViewHolderTicket(view: View): RecyclerView.ViewHolder(view) {

    var lbTituloC: TextView = view.findViewById(R.id.lbTitulo)
    var lbIdC: TextView = view.findViewById(R.id.lbTicket)
    var lbAutorC: TextView = view.findViewById(R.id.lbAutor)
    var lbEmailC: TextView = view.findViewById(R.id.lbEmail)
    var caEstado: CardView = view.findViewById(R.id.cEstado)
    var icEditar: ImageView = view.findViewById(R.id.icEdit)
    var icBorrar: ImageView = view.findViewById(R.id.icDelete)

}