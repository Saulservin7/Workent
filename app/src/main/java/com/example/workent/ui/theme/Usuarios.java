package com.example.workent.ui.theme;

public class Usuarios {
    private String email;
    private String password;

    private String cellphone;

    private String firstName;

    private String lastName;

    private String userType;

    private  String selfie;

    private String id;

    private String address;





    public Usuarios() {
        // Constructor vacío requerido para Firebase
    }

    public Usuarios(String correo, String contraseña, String celular, String nombre, String apellido, String tipoUsuario,String selfie,String id,String address) {
        this.password = contraseña;
        this.email = correo;
        this.cellphone = celular;
        this.firstName = nombre;
        this.lastName = apellido;
        this.userType = tipoUsuario;
        this.selfie = selfie;
        this.id=id;
        this.address=address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getCellphone() {
        return cellphone;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public String getUserType() {
        return userType;
    }

    public String getSelfie(){return selfie; }

    public String getId(){return id;}

    public String getAddress() {
        return address;
    }


}

