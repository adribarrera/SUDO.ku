package com.sudoku.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SudokuGUI sudokuGUI;
    private PanelMenuPrincipal menuPrincipal;

    public VentanaPrincipal() {
        super("SUDO.ku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(600, 650);
        setLocationRelativeTo(null);
        
        // Cargar el icono de la ventana
        try {
            java.net.URL imgURL = getClass().getResource("/images/iconosudo.png");
            if (imgURL != null) {
                setIconImage(new ImageIcon(imgURL).getImage());
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        sudokuGUI = new SudokuGUI();
        menuPrincipal = new PanelMenuPrincipal(this);

        mainPanel.add(menuPrincipal, "MENU");
        mainPanel.add(sudokuGUI, "JUEGO");

        add(mainPanel);
    }

    public void mostrarJuego() {
        cardLayout.show(mainPanel, "JUEGO");
        sudokuGUI.requestFocusInWindow();
    }

    public void mostrarMenu() {
        cardLayout.show(mainPanel, "MENU");
    }
}
