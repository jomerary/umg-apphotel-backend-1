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
import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * REST Web Service
 *
 * @author merar
 */
@Path("detalleReservacion")
public class OperacionesDetalleReservacion {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Context
    private UriInfo context;

    public List<DetalleReservacion> Consultar() {
        List<DetalleReservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Reservacion";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                DetalleReservacion u = new DetalleReservacion();
                u.setDetalleId(rs.getInt("detalle_id")); // si tu clase tiene este campo
                u.setReservacionId(rs.getInt("reservacion_id"));
                u.setHabitacionId(rs.getInt("habitacion_id"));
                u.setPrecioPorNoche(rs.getDouble("precio_por_noche"));
                u.setCantidadNoches(rs.getInt("cantidad_noches"));
                u.setSubtotal(rs.getDouble("subtotal"));
                

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
    public List<DetalleReservacion> listar() {
        return (Consultar());
    }

    @POST
@Path("/agregar")
@Produces("application/json")
@Consumes("application/json")
public Response agregar(DetalleReservacion u) {
    // SQL con el nombre exacto de la tabla y columnas
    String sql = "INSERT INTO Detalle_Reservacion (reservacion_id, habitacion_id, precio_por_noche, cantidad_noches, subtotal) " + "VALUES (?,?,?,?,?)";

    try {
        // Obtener conexión
        con = cn.getConnection();
        if (con == null) {
            throw new Exception("No se pudo establecer conexión a la base de datos");
        }

        // Preparar la sentencia
        ps = con.prepareStatement(sql);
        // Asignar valores a la sentencia
        ps.setInt(1, u.getReservacionId());
        ps.setInt(2, u.getHabitacionId());
        ps.setDouble(3, u.getPrecioPorNoche());
        ps.setInt(4, u.getCantidadNoches());
        ps.setDouble(5, u.getSubtotal());

        // Ejecutar INSERT
        int rows = ps.executeUpdate();

        if (rows > 0) {
            return Response.ok()
                    .entity("{\"message\": \"DetalleReservación agregada correctamente\"}")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"No se pudo insertar la reservación\"}")
                    .build();
        }

    } catch (Exception e) {
        e.printStackTrace(); // Para depuración en el servidor
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                .build();
    }
}
    @PUT
    @Path("/modificar")

    public Response modificar(DetalleReservacion u) {
        String sql = "update Detalle_Reservacion set reservacion_id=?, habitacion_id=?, precio_por_noche=?, cantidad_noches=?, subtotal=? where detalle_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(6, u.getDetalleId());
            ps.setInt(1, u.getReservacionId());
            ps.setInt(2, u.getHabitacionId());
            ps.setDouble(3, u.getPrecioPorNoche());           
            ps.setInt(4, u.getCantidadNoches());
            ps.setDouble(5, u.getSubtotal());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"DetalleReservacion modificada correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                    .build();
        }
    }

    @GET
@Path("consultar/{id}")
@Produces(MediaType.APPLICATION_JSON)
public Response consultaResponse(@PathParam("id") int DetalleId) {
    try {
        // --- prueba: no usar BD aún, crea objeto hardcodeado ---
        List<DetalleReservacion> lista = new ArrayList<>();
        DetalleReservacion u = new DetalleReservacion();
        u.setDetalleId(DetalleId);
        u.setReservacionId(5);
        u.setHabitacionId(2);
        u.setPrecioPorNoche(100.0);
        u.setCantidadNoches(2);
        u.setSubtotal(200.0);
        lista.add(u);
        return Response.ok(lista).build();
    } catch (Exception e) {
        e.printStackTrace();
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stack = sw.toString();
        // devolver 500 con stack (temporal para debug)
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"" + e.getMessage() + "\", \"stack\":\"" + stack.replace("\"","'") + "\"}")
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int DetalleReservacionId) {
        String sql = "delete from Detalle_Reservacion where reservacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, DetalleReservacionId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"DetalleReservacion eliminada correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
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
    public OperacionesDetalleReservacion() {
    }

}
