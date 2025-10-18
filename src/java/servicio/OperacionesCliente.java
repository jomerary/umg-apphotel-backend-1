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
@Path("cliente")
public class OperacionesCliente {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Context
    private UriInfo context;

    public List<Cliente> Consultar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente u = new Cliente();
                u.setClienteId(rs.getInt("cliente_id")); // si tu clase tiene este campo
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setDocumentoIdentidad(rs.getString("documento_identidad"));
                u.setTelefono(rs.getString("telefono"));
                u.setCorreo(rs.getString("correo"));
                u.setDireccion(rs.getString("direccion"));
                u.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());

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
    public List<Cliente> listar() {
        return (Consultar());
    }

    @POST
    @Path("/agregar")
    @Produces("application/json")
    @Consumes("application/json")
    public Response agregar(Cliente u) {
        String sql = "insert into cliente(nombre_completo, documento_identidad, telefono, correo, direccion ) values (?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getDocumentoIdentidad());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getDireccion());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Cliente agregado correctamente\"}")
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

    public Response modificar(Cliente u) {
        String sql = "update cliente set nombre_completo=?, documento_identidad=?, telefono=?, correo=?, direccion=? where cliente_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(6, u.getClienteId());
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getDocumentoIdentidad());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getDireccion());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Cliente modificado correctamente\"}")
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
    public List<Cliente> consulta(@PathParam("id") int ClienteId) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "select * from cliente where cliente_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, ClienteId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente u = new Cliente();
                u.setClienteId(rs.getInt("cliente_id"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setDocumentoIdentidad(rs.getString("documento_identidad"));
                u.setTelefono(rs.getString("telefono"));
                u.setCorreo(rs.getString("correo"));
                u.setDireccion(rs.getString("direccion"));
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;

    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int ClienteId) {
        String sql = "delete from cliente where cliente_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, ClienteId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Cliente eliminado correctamente\"}")
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
    public OperacionesCliente() {
    }

}
