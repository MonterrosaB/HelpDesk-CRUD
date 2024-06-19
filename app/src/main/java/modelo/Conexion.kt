package modelo

import java.sql.Connection
import java.sql.DriverManager

class Conexion {
    fun cadenaConexion(): Connection?{
        try {

            val ip = "jdbc:oracle:thin:@192.168.56.1:1521:xe"

            val user = "CHIKY"
            val password = "1144"

            val conexion = DriverManager.getConnection(ip, user, password)
            return conexion
        } catch (e: Exception){
            println("Este es el error $e.")
            return null
        }
    }
}