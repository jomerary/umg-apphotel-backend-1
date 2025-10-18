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
@Path("hotel")
public class OperacionesHotel {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Context
    private UriInfo context;

    public List<Hotel> Consultar() {
        List<Hotel> lista = new ArrayList<>();
        String sql = "select * from hotel";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Hotel u = new Hotel();
                u.setHotelId(rs.getInt("hotel_id")); // si tu clase tiene este campo
                u.setNombre(rs.getString("nombre"));
                u.setDireccion(rs.getString("direccion"));
                u.setTelefono(rs.getString("telefono"));
                u.setCorreo(rs.getString("correo"));
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
    public List<Hotel> listar() {
        return (Consultar());
    }

    @POST
    @Path("/agregar")
    @Produces("application/json")
    @Consumes("application/json")
    public Response agregar(Hotel u) {
        String sql = "insert into hotel(hotel_id,nombre,direccion,telefono,correo) values (?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, u.getHotelId());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getDireccion());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getCorreo());
            ps.executeUpdate();
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Hotel creado correctamente\"}")
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

    public int modificar(Hotel u) {
        String sql = "update hotel set nombre=?, direccion=?, telefono=?, correo=? where hotel_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(5, u.getHotelId());
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getDireccion());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getCorreo());
            ps.executeUpdate();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @GET
    @Path("/consultar/{id}")
    public List<Hotel> consultar(@PathParam("id") int Hotel_id) {
        List<Hotel> lista = new ArrayList<>();
        String sql = "select * from hotel where hotel_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, Hotel_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Hotel u = new Hotel();
                u.setHotelId(rs.getInt("hotel_id"));
                u.setNombre(rs.getString("nombre"));
                u.setDireccion(rs.getString("direccion"));
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
    public int eliminar(@PathParam("id") int hotel_id) {
        String sql = "delete from hotel where hotel_id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, hotel_id);
            ps.executeUpdate();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Creates a new instance of OperacionesHotel
     */
    public OperacionesHotel() {
    }

}
