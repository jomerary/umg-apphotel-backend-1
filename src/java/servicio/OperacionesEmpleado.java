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
@Path("empleado")
public class OperacionesEmpleado {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Context
    private UriInfo context;

    public List<Empleado> Consultar() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleado";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Empleado u = new Empleado();
                u.setEmpleadoId(rs.getInt("empleado_id")); // si tu clase tiene este campo
                u.setHotelId(rs.getInt("hotel_id"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setDocumentoIdentidad(rs.getString("documento_identidad"));
                u.setCargo(rs.getString("cargo"));
                u.setTelefono(rs.getString("telefono"));
                u.setCorreo(rs.getString("correo"));                
                u.setFechaRegistro(rs.getTimestamp("fecha_contratacion").toLocalDateTime());

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
    public List<Empleado> listar() {
        return (Consultar());
    }

    @POST
    @Path("/agregar")
    @Produces("application/json")
    @Consumes("application/json")
    public Response agregar(Empleado u) {
        String sql = "insert into empleado ( hotel_id, nombre_completo, documento_identidad, cargo, telefono,correo ) values (?,?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, u.getHotelId());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getDocumentoIdentidad());
            ps.setString(4, u.getCargo());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getCorreo());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Empleado agregado correctamente\"}")
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

    @PUT
    @Path("/modificar")

    public Response modificar(Empleado u) {
        String sql = "update empleado set hotel_id=?, nombre_completo=?, documento_identidad=?, cargo=?, telefono=?, correo=? where empleado_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(7, u.getEmpleadoId());
            ps.setInt(1, u.getHotelId());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getDocumentoIdentidad());
            ps.setString(4, u.getCargo());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getCorreo());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Empleado modificado correctamente\"}")
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
    public List<Empleado> consulta(@PathParam("id") int EmpleadoId) {
        List<Empleado> lista = new ArrayList<>();
        String sql = "select * from empleado where empleado_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, EmpleadoId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Empleado u = new Empleado();
                u.setEmpleadoId(rs.getInt("empleado_id"));
                u.setHotelId(rs.getInt("hotel_id"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setDocumentoIdentidad(rs.getString("documento_identidad"));
                u.setCargo(rs.getString("cargo"));
                u.setTelefono(rs.getString("telefono"));
                u.setCorreo(rs.getString("correo"));
                
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;

    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int EmpleadoId) {
        String sql = "delete from empleado where empleado_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, EmpleadoId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Empleado eliminado correctamente\"}")
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
    public OperacionesEmpleado() {
    }

}
