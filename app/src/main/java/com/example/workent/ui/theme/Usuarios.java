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

    private String clabe;

    private String terms;





    public Usuarios() {
        // Constructor vacío requerido para Firebase
    }

    public Usuarios(String correo, String contraseña, String celular, String nombre, String apellido, String tipoUsuario,String selfie,String id,String address,String clabe,String terms) {
        this.password = contraseña;
        this.email = correo;
        this.cellphone = celular;
        this.firstName = nombre;
        this.lastName = apellido;
        this.userType = tipoUsuario;
        this.selfie = selfie;
        this.id=id;
        this.address=address;
        this.clabe=clabe;
        this.terms=terms;
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

    public String getClabe(){return clabe;}

    public String getTerms() {
        return terms;
    }
}

