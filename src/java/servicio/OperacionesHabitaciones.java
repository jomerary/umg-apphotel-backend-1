/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package servicio;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.DELETE;
import java.util.HashSet;
import java.util.Set;
/**
 * REST Web Service
 *
 * @author merar
 */
@Path("habitaciones")
public class OperacionesHabitaciones {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    @Context
    private UriInfo context;
    public List<Habitaciones> Consultar() {
        List<Habitaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM Habitacion";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Habitaciones u = new Habitaciones();
                u.setHabitacionId(rs.getInt("habitacion_id")); // si tu clase tiene este campo
                u.setHotelId(rs.getInt("hotel_id"));
                u.setTipoHabitacionId(rs.getInt("tipo_habitacion_id"));
                u.setNumeroHabitacion(rs.getString("numero_habitacion"));  
                u.setEstado(rs.getString("estado"));               
                u.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                
                // Te amo mi vida hermosa <3
                
                //  aqui se encuentra el error, para que la lista muestre algo tiene que agregarle algo
                lista.add(u); // <----- linea faltante
            }

            System.out.println("Registros encontrados: " + lista.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
     
        
    }
    @GET
    @Path("/lista")
    public List<Habitaciones> listar() {
        return (Consultar());
    }

    @POST
    @Path("/agregar")
    @Produces("application/json")
    @Consumes("application/json")
    public int agregar(Habitaciones u) {
        String sql = "insert into Habitacion (hotel_id ,tipo_habitacion_id ,numero_habitacion, estado) values (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

                   
            ps.setInt(1, u.getHotelId());
            ps.setInt(2, u.getTipoHabitacionId());   
            ps.setString(3,u.getNumeroHabitacion());
            ps.setString(4, u.getEstado());
            ps.executeUpdate();
            return 1;
        } catch (Exception e) {
            return 0;
        }


    
}
    @PUT
    @Path("/modificar")
    
    public int modificar(Habitaciones u){
        String sql= "update Habitacion set hotel_id=?, tipo_habitacion_id=?, numero_habitacion=?, estado=? where habitacion_id=?";
        try
        {
        con = cn.getConnection();
        ps=con.prepareStatement(sql);
        ps.setInt(5, u.getHabitacionId());
        ps.setInt(1, u.getHotelId());
        ps.setInt(2, u.getTipoHabitacionId());
        ps.setString(3, u.getNumeroHabitacion());   
        ps.setString(4, u.getEstado());
        ps.executeUpdate();
        return 1;
        }catch(Exception e){
            return 0;
        }
    }
    @GET
    @Path("/consultar/{id}")
    public List<Habitaciones> consultar (@PathParam("id") int HabitacionId){
        List<Habitaciones> lista = new ArrayList<>();
        String sql="select * from Habitacion where habitacion_id=?";
        try{
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, HabitacionId);
            rs=ps.executeQuery();
            while(rs.next())
            {
                Habitaciones u = new Habitaciones();
                u.setHabitacionId(rs.getInt("habitacion_id"));
                u.setHotelId(rs.getInt("hotel_id"));
                u.setTipoHabitacionId(rs.getInt("tipo_habitacion_id"));
                u.setNumeroHabitacion(rs.getString("numero_habitacion"));  
                u.setEstado(rs.getString("estado"));
                
                lista.add(u);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lista;
        
    }
    @DELETE
    @Path("/eliminar/{id}")
    public int eliminar(@PathParam("id") int habitacion_id){
        String sql="delete from Habitacion where habitacion_id=?";
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            ps.setInt(1, habitacion_id);
            ps.executeUpdate();
            return 1;
        }catch(Exception e)
        {
            return 0;
        }
    }
    /**
     * Creates a new instance of OperacionesHotel
     */
    public OperacionesHabitaciones() {
    }

    
   
}




