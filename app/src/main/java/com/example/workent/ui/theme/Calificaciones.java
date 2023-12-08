package com.example.workent.ui.theme;

public class Calificaciones {
    private int calificacion;
    private String cliente;
    private String trabajador;
    private String idTrabajo;
    private String comentario;

    public Calificaciones() {
        // Constructor vacío requerido para ciertos casos (por ejemplo, Firebase)
    }

    public Calificaciones(int calificacion, String cliente, String trabajador, String idTrabajo, String comentario) {
        this.calificacion = calificacion;
        this.cliente = cliente;
        this.trabajador = trabajador;
        this.idTrabajo = idTrabajo;
        this.comentario = comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public String getCliente() {
        return cliente;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public String getIdTrabajo() {
        return idTrabajo;
    }

    public String getComentario() {
        return comentario;
    }
}
