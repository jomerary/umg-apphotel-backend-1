/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import java.sql.*;
/**
 *
 * @author merar
 */
public class Conexion {
    Connection con;
    public Conexion (){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:33060/app_hotel_db_v3?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "secret"
            );
            System.out.println("Connection success");
            
        }catch (Exception e){
            System.out.println("Error" + e.getMessage());
            e.printStackTrace(); // Muy importante para ver la causa real
            con = null;
        }
    }
    public Connection getConnection(){
        return con;
    }
}
