package com.sudoku.modelo;

import java.sql.Timestamp;

/**
 * Entidad (DTO) que representa una puntuación o victoria en el juego de Sudoku.
 * Encapsula la información del jugador, la dificultad superada, el tiempo
 * invertido
 * y la marca de tiempo de la partida. Proporciona métodos utilitarios para
 * formatear
 * la presentación visual del tiempo y la fecha en la tabla de ranking.
 */
public class RegistroPuntuacion {
    private int id;
    private String nombre;
    private String dificultad;
    private int tiempoSegundos;
    private Timestamp fecha;

    /**
     * Constructor completo utilizado al recuperar registros existentes desde la
     * base de datos.
     *
     * @param id             Identificador único de la puntuación en la base de
     *                       datos.
     * @param nombre         Nombre del jugador.
     * @param dificultad     Dificultad superada en la partida.
     * @param tiempoSegundos Tiempo total de resolución en segundos.
     * @param fecha          Marca de tiempo exacta en la que se registró la
     *                       victoria.
     */
    public RegistroPuntuacion(int id, String nombre, String dificultad, int tiempoSegundos, Timestamp fecha) {
        this.id = id;
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.tiempoSegundos = tiempoSegundos;
        this.fecha = fecha;
    }

    /**
     * Constructor utilizado al crear un nuevo registro de victoria en la
     * aplicación.
     * Asigna automáticamente la fecha y hora actuales.
     *
     * @param nombre         Nombre del jugador.
     * @param dificultad     Dificultad superada en la partida.
     * @param tiempoSegundos Tiempo total de resolución en segundos.
     */
    public RegistroPuntuacion(String nombre, String dificultad, int tiempoSegundos) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.tiempoSegundos = tiempoSegundos;
        this.fecha = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Obtiene el identificador único de la puntuación.
     *
     * @return ID de la puntuación.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return Nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la dificultad de la partida.
     *
     * @return Dificultad superada.
     */
    public String getDificultad() {
        return dificultad;
    }

    /**
     * Obtiene el tiempo total de resolución en segundos.
     *
     * @return Segundos transcurridos.
     */
    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    /**
     * Obtiene la marca de tiempo (Timestamp) de la victoria.
     *
     * @return Objeto Timestamp con la fecha y hora.
     */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
     * Convierte el tiempo total en segundos a una cadena formateada en minutos y
     * segundos (MM:SS).
     *
     * @return Cadena de texto con el tiempo formateado para la interfaz.
     */
    public String getTiempoFormateado() {
        int minutos = tiempoSegundos / 60;
        int segundos = tiempoSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    /**
     * Convierte el objeto Timestamp a una cadena de fecha legible en formato
     * estándar (DD/MM/YYYY).
     *
     * @return Cadena con la fecha formateada, o "-" si la fecha es nula.
     */
    public String getFechaFormateada() {
        if (fecha != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(fecha);
        }
        return "-";
    }
}
