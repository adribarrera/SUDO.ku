package com.sudoku.modelo;

import java.sql.Timestamp;

public class RegistroPuntuacion {
    private int id;
    private String nombre;
    private String dificultad;
    private int tiempoSegundos;
    private Timestamp fecha;

    public RegistroPuntuacion(int id, String nombre, String dificultad, int tiempoSegundos, Timestamp fecha) {
        this.id = id;
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.tiempoSegundos = tiempoSegundos;
        this.fecha = fecha;
    }

    public RegistroPuntuacion(String nombre, String dificultad, int tiempoSegundos) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.tiempoSegundos = tiempoSegundos;
        this.fecha = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDificultad() {
        return dificultad;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    // Método para formatear el tiempo
    public String getTiempoFormateado() {
        int minutos = tiempoSegundos / 60;
        int segundos = tiempoSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    // Método para formatear la fecha
    public String getFechaFormateada() {
        if (fecha != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(fecha);
        }
        return "-";
    }
}
