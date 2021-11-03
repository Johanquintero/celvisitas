package com.celar.celvisitas.ui.home;


import android.widget.ImageView;

import java.io.Serializable;

public class VisitElement implements Serializable {
    public String id;
    public String nombre;
    public String status;
    public String img;

    public VisitElement(String id,String nombre,String status, String img) {
        this.id = id;
        this.nombre = nombre;
        this.status = status;
        this.img = img;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
