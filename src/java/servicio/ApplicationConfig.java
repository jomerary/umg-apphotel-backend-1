/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.Set;

/**
 *
 * @author merar
 */
@jakarta.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends jakarta.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Servicio_Progra.GenericResource.class);
        resources.add(servicio.Operaciones.class);
        resources.add(servicio.OperacionesCliente.class);
        resources.add(servicio.OperacionesEmpleado.class);
        resources.add(servicio.OperacionesHabitacion.class);
        resources.add(servicio.OperacionesHabitaciones.class);
        resources.add(servicio.OperacionesHotel.class);
    }
    
}
