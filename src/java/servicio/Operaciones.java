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

@Path("generic")
public class Operaciones {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Context
    private UriInfo context;

    public List<Usuarios> Consultar() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setUsuarioId(rs.getInt("usuario_id")); // si tu clase tiene este campo
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setRolUsuario(rs.getString("rol_usuario"));
                u.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());

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
    public List<Usuarios> listar() {
        return (Consultar());
    }

    @POST
    @Path("/agregar")
    @Produces("application/json")
    @Consumes("application/json")
    public Response agregar(Usuarios u) {
        String sql = "insert into usuario(nombre_completo,nombre_usuario,contrasena_hash,rol_usuario) values (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getNombreUsuario());
            ps.setString(3, u.getContrasenaHash());
            ps.setString(4, u.getRolUsuario());
            ps.executeUpdate();
             int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Usuario eliminado correctamente\"}")
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
    
    public Response modificar(Usuarios u){
        String sql= "update usuario set nombre_completo=?, nombre_usuario=?, contrasena_hash=?, rol_usuario=? where usuario_id=?";
        try
        {
        con = cn.getConnection();
        ps=con.prepareStatement(sql);
        ps.setInt(5, u.getUsuarioId());
        ps.setString(1, u.getNombreCompleto());
        ps.setString(2, u.getNombreUsuario());
        ps.setString(3, u.getContrasenaHash());
        ps.setString(4, u.getRolUsuario());
        ps.executeUpdate();
         int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Usuario eliminado correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
                        .build();
            }
        }catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                    .build();
        }
    }
    @GET
    @Path("/consultar/{id}")
    public List<Usuarios> consultar (@PathParam("id") int usuario_id){
        List<Usuarios> lista = new ArrayList<>();
        String sql="select *  from usuario where usuario_id=?";
        try{
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuario_id);
            rs=ps.executeQuery();
            while(rs.next())
            {
                Usuarios u = new Usuarios();
                u.setUsuarioId(rs.getInt("usuario_id"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setContrasenaHash(rs.getString("contrasena_hash"));
                u.setRolUsuario(rs.getString("rol_usuario"));
                lista.add(u);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lista;
        
    }
    @DELETE
    @Path("/eliminar/{id}")
    public Response eliminar(@PathParam("id") int usuario_id){
        String sql="delete from usuario where usuario_id=?";
        try{
            con =cn.getConnection();
            ps=con.prepareStatement(sql);
            ps.setInt(1, usuario_id);
            ps.executeUpdate();
              int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok()
                        .entity("{\"message\": \"Usuario eliminado correctamente\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
                        .build();
            }
        }catch(Exception e)
        {
           return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
                    .build();
        }
    }

    public Operaciones() {

    }

}
