package com.sudoku.principal;

import com.sudoku.vista.VentanaPrincipal;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Punto de entrada principal de la aplicación SUDO.ku.
 * Inicializa el tema visual oscuro (FlatDarkLaf) e instancia el contenedor
 * principal de la ventana para iniciar la ejecución del juego.
 */
public class JuegoSudoku {

    /**
     * Recurso global para activar el modo de depuración (Debug).
     * Si está activa (true), habilita opciones de desarrollo en la interfaz,
     * como la dificultad "Prueba" para testear victorias rápidamente.
     * Si está inactiva (false), oculta estas opciones para el usuario final.
     */
    public static final boolean MODO_DEBUG = false;

    /**
     * Método principal (main) que arranca la aplicación.
     * Configura FlatLaf y muestra la ventana principal.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    }
}