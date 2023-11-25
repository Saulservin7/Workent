package com.example.workent;

public class ListRequest {
    private String id;
    private String cliente;
    private String trabajador;
    private String idTrabajo;

    public ListRequest() {
        // Constructor vacío requerido para Firebase
    }

    public ListRequest(String id, String cliente, String trabajador, String idTrabajo) {
        this.id = id;
        this.cliente = cliente;
        this.trabajador = trabajador;
        this.idTrabajo = idTrabajo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(String trabajador) {
        this.trabajador = trabajador;
    }

    public String getIdTrabajo() {
        return idTrabajo;
    }

    public void setIdTrabajo(String idTrabajo) {
        this.idTrabajo = idTrabajo;
    }
}

