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
/**
 * REST Web Service
 *
 * @author merar
 */
@Path("tipoHabitacion")
public class OperacionesHabitacion {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    @Context
    private UriInfo context;
    public List<TipoHabitacion> Consultar() {
        List<TipoHabitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tipo_Habitacion";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                TipoHabitacion u = new TipoHabitacion();
                u.setTipoHabitacionId(rs.getInt("tipo_habitacion_id")); // si tu clase tiene este campo
                u.setDescripcion(rs.getString("descripcion"));
                u.setPrecioBase(rs.getDouble("precio_base"));
                u.setCapacidad(rs.getInt("capacidad"));               
                u.setFecha_creacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                
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
    public List<TipoHabitacion> listar() {
        return (Consultar());
    }

    @POST
    @Path("/agregar")
    @Produces("application/json")
    @Consumes("application/json")
    public int agregar(TipoHabitacion u) {
        String sql = "insert into Tipo_Habitacion(descripcion,precio_base,capacidad) values (?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            
            ps.setString(1, u.getDescripcion());
            ps.setDouble(2, u.getPrecioBase());
            ps.setInt(3, u.getCapacidad());            
            ps.executeUpdate();
            return 1;
        } catch (Exception e) {
            return 0;
        }


    
}
    @PUT
    @Path("/modificar")
    
    public int modificar(TipoHabitacion u){
        String sql= "update Tipo_Habitacion set descripcion=?, precio_base=?, capacidad=? where tipo_habitacion_id=?";
        try
        {
        con = cn.getConnection();
        ps=con.prepareStatement(sql);
        ps.setInt(4, u.getTipoHabitacionId());
        ps.setString(1, u.getDescripcion());
        ps.setDouble(2, u.getPrecioBase());
        ps.setInt(3, u.getCapacidad());       
        ps.executeUpdate();
        return 1;
        }catch(Exception e){
            return 0;
        }
    }
    @GET
    @Path("/consultar/{id}")
    public List<TipoHabitacion> consultar (@PathParam("id") int TipoHabitacionId){
        List<TipoHabitacion> lista = new ArrayList<>();
        String sql="select * from Tipo_Habitacion where tipo_habitacion_id=?";
        try{
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, TipoHabitacionId);
            rs=ps.executeQuery();
            while(rs.next())
            {
                TipoHabitacion u = new TipoHabitacion();
                u.setTipoHabitacionId(rs.getInt("tipo_habitacion_id"));
                u.setDescripcion(rs.getString("descripcion"));
                u.setPrecioBase(rs.getDouble("precio_base"));
                u.setCapacidad(rs.getInt("capacidad"));                
                lista.add(u);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lista;
        
    }
    @DELETE
    @Path("/eliminar/{id}")
    public int eliminar(@PathParam("id") int tipo_habitacion_id){
        String sql="delete from Tipo_Habitacion where tipo_habitacion_id=?";
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            ps.setInt(1, tipo_habitacion_id);
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
    public OperacionesHabitacion() {
    }

    
   
}



