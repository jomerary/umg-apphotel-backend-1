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
import jakarta.ws.rs.core.Response;
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
        String sql = "SELECT * FROM habitacion";

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

                lista.add(u); 
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
    public Response agregar(Habitaciones u) {
        String sql = "insert into habitacion (hotel_id ,tipo_habitacion_id ,numero_habitacion, estado) values (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, u.getHotelId());
            ps.setInt(2, u.getTipoHabitacionId());
            ps.setString(3, u.getNumeroHabitacion());
            ps.setString(4, u.getEstado());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Habitacion creada correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Algo Ocurrio\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/modificar")
    public Response modificar(Habitaciones u) {
        String sql = "update habitacion set hotel_id=?, tipo_habitacion_id=?, numero_habitacion=?, estado=? where habitacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(5, u.getHabitacionId());
            ps.setInt(1, u.getHotelId());
            ps.setInt(2, u.getTipoHabitacionId());
            ps.setString(3, u.getNumeroHabitacion());
            ps.setString(4, u.getEstado());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Habitacion modificada correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Algo Ocurrio\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/consultar/{id}")
    public List<Habitaciones> consultar(@PathParam("id") int HabitacionId) {
        List<Habitaciones> lista = new ArrayList<>();
        String sql = "select * from habitacion where habitacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, HabitacionId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Habitaciones u = new Habitaciones();
                u.setHabitacionId(rs.getInt("habitacion_id"));
                u.setHotelId(rs.getInt("hotel_id"));
                u.setTipoHabitacionId(rs.getInt("tipo_habitacion_id"));
                u.setNumeroHabitacion(rs.getString("numero_habitacion"));
                u.setEstado(rs.getString("estado"));

                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;

    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response  eliminar(@PathParam("id") int habitacion_id) {
        String sql = "delete from Habitacion where habitacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, habitacion_id);
             int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Habitacion eliminada correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Algo Ocurrio\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                    .build();
        }
    }
    

    /**
     * Creates a new instance of OperacionesHotel
     */
    public OperacionesHabitaciones() {
    }

}
