package com.sudoku.principal;

import com.sudoku.vista.SudokuGUI;
import javax.swing.JFrame;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Punto de entrada principal de la aplicación.
 * Inicializa el marco de la ventana y lo hace visible para comenzar el juego
 * gráficamente.
 */
public class JuegoSudoku {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame ventana = new JFrame("Juego de Sudoku");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);

        // Nuestro panel principal que creamos antes, el cual contiene toda la interfaz
        SudokuGUI panelPrincipal = new SudokuGUI();
        ventana.add(panelPrincipal);

        // Configuramos tamaño y lo centramos en la pantalla
        ventana.setSize(600, 650);
        ventana.setLocationRelativeTo(null); // Centra la ventana
        ventana.setVisible(true);
    }
}
