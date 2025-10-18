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
@Path("reservacion")
public class OperacionesReservacion {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Context
    private UriInfo context;

    public List<Reservacion> Consultar() {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reservacion";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Reservacion u = new Reservacion();
                u.setReservacionId(rs.getInt("reservacion_id")); // si tu clase tiene este campo
                u.setClienteId(rs.getInt("cliente_id"));
                u.setEmpleadoId(rs.getInt("empleado_id"));
                u.setHotelId(rs.getInt("hotel_id"));
                u.setFechaEntrada(rs.getDate("fecha_entrada").toLocalDate());
                u.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
                u.setCostoTotal(rs.getDouble("costo_total"));                
                u.setEstado(rs.getString("estado"));

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
    public List<Reservacion> listar() {
        return (Consultar());
    }

    @POST
@Path("/agregar")
@Produces("application/json")
@Consumes("application/json")
public Response agregar(Reservacion u) {
    // SQL con el nombre exacto de la tabla y columnas
    String sql = "INSERT INTO reservacion (cliente_id, empleado_id, hotel_id, fecha_entrada, fecha_salida, costo_total, estado) "
               + "VALUES (?,?,?,?,?,?,?)";

    try {
        // Obtener conexión
        con = cn.getConnection();
        if (con == null) {
            throw new Exception("No se pudo establecer conexión a la base de datos");
        }

        // Preparar la sentencia
        ps = con.prepareStatement(sql);

        // Validar fechas antes de insertar
        if (u.getFechaEntrada() == null || u.getFechaSalida() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Las fechas no pueden ser nulas\"}")
                    .build();
        }

        // Asignar valores a la sentencia
        ps.setInt(1, u.getClienteId());
        ps.setInt(2, u.getEmpleadoId());
        ps.setInt(3, u.getHotelId());
        ps.setDate(4, java.sql.Date.valueOf(u.getFechaEntrada()));   // LocalDate → java.sql.Date
        ps.setDate(5, java.sql.Date.valueOf(u.getFechaSalida()));
        ps.setDouble(6, u.getCostoTotal());
        ps.setString(7, u.getEstado());

        // Ejecutar INSERT
        int rows = ps.executeUpdate();

        if (rows > 0) {
            return Response.ok()
                    .entity("{\"message\": \"Reservación agregada correctamente\"}")
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

    public Response modificar(Reservacion u) {
        String sql = "update Reservacion set cliente_id=?, empleado_id=?, hotel_id=?, fecha_entrada=?, fecha_salida=?, costo_total=?, estado=? where reservacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(8, u.getReservacionId());
            ps.setInt(1, u.getClienteId());
            ps.setInt(2, u.getEmpleadoId());
            ps.setInt(3, u.getHotelId());
            ps.setDate(4, java.sql.Date.valueOf(u.getFechaEntrada()));
            ps.setDate(5, java.sql.Date.valueOf(u.getFechaSalida()));
            ps.setDouble(6, u.getCostoTotal());
            ps.setString(7, u.getEstado());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Reservacion modificada correctamente\"}")
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
    @Path("/consultar/{id}")
    public List<Reservacion> consulta(@PathParam("id") int ReservacionId) {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "select * from Reservacion where reservacion_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, ReservacionId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Reservacion u = new Reservacion();
                u.setReservacionId(rs.getInt("reservacion_id"));
                u.setClienteId(rs.getInt("reservacion_id"));
                u.setEmpleadoId(rs.getInt("empleado_id"));
                u.setHotelId(rs.getInt("hotel_id"));
                u.setFechaEntrada(rs.getDate("fecha_entrada").toLocalDate());
                u.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
                u.setCostoTotal(rs.getDouble("costo_total"));
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
    public Response eliminar(@PathParam("id") int ReservacionId) {
        String sql = "delete from Empleado where empleado_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, ReservacionId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Reservacion eliminada correctamente\"}")
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
    public OperacionesReservacion() {
    }

}
