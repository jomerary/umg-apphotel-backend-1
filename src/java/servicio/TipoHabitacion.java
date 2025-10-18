/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import java.time.LocalDateTime;
/**
 *
 * @author merar
 */
public class TipoHabitacion {
     
    private int tipoHabitacionId;
    private String descripcion;
    private double precioBase;
    private int capacidad;
    private LocalDateTime fechaCreacion;

    public int getTipoHabitacionId() {
        return tipoHabitacionId;
    }

    public void setTipoHabitacionId(int tipoHabitacionId) {
        this.tipoHabitacionId = tipoHabitacionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFecha_creacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
}
