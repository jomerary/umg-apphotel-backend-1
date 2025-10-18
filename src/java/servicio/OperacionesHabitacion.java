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
        String sql = "SELECT * FROM tipo_habitacion";

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
    public List<TipoHabitacion> listar() {
        return (Consultar());
    }

    @POST
    @Path("/agregar")
    @Produces("application/json")
    @Consumes("application/json")
    public Response agregar(TipoHabitacion u) {
        String sql = "insert into tipo_habitacion(descripcion,precio_base,capacidad) values (?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, u.getDescripcion());
            ps.setDouble(2, u.getPrecioBase());
            ps.setInt(3, u.getCapacidad());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Tipo Habitacion creada correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Error Inesperado\"}")
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

    public Response modificar(TipoHabitacion u) {
        String sql = "update tipo_habitacion set descripcion=?, precio_base=?, capacidad=? where tipo_habitacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(4, u.getTipoHabitacionId());
            ps.setString(1, u.getDescripcion());
            ps.setDouble(2, u.getPrecioBase());
            ps.setInt(3, u.getCapacidad());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Tipo habitacion actualizado correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Error inesperado\"}")
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
    public List<TipoHabitacion> consultar(@PathParam("id") int TipoHabitacionId) {
        List<TipoHabitacion> lista = new ArrayList<>();
        String sql = "select * from tipo_habitacion where tipo_habitacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, TipoHabitacionId);
            rs = ps.executeQuery();
            while (rs.next()) {
                TipoHabitacion u = new TipoHabitacion();
                u.setTipoHabitacionId(rs.getInt("tipo_habitacion_id"));
                u.setDescripcion(rs.getString("descripcion"));
                u.setPrecioBase(rs.getDouble("precio_base"));
                u.setCapacidad(rs.getInt("capacidad"));
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;

    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int tipo_habitacion_id) {
        String sql = "delete from tipo_habitacion where tipo_habitacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, tipo_habitacion_id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Tipo habitacion eliminado correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Error inesperado\"}")
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
    public OperacionesHabitacion() {
    }

}
