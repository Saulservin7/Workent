package com.example.workent.ui.theme;

public class Solicitudes {

    private String id;
    private String cliente;
    private String trabajador;
    private String idTrabajo;
    private String estatus;
    private String date;  // Nueva cadena date
    private String time;  // Nueva cadena time

    public Solicitudes() {

    }

    public Solicitudes(String id, String cliente, String trabajador, String idTrabajo, String estatus, String date, String time) {
        this.cliente = cliente;
        this.trabajador = trabajador;
        this.id = id;
        this.idTrabajo = idTrabajo;
        this.estatus = estatus;
        this.date = date;
        this.time = time;
    }

    public String getCliente() {
        return cliente;
    }

    public String getId() {
        return id;
    }

    public String getIdTrabajo() {
        return idTrabajo;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
