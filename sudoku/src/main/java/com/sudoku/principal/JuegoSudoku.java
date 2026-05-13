package com.sudoku.principal;

import com.sudoku.vista.VentanaPrincipal;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Punto de entrada principal de la aplicación.
 * Inicializa el marco de la ventana y lo hace visible para comenzar el juego
 * gráficamente.
 */
public class JuegoSudoku {

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