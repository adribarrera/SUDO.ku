package com.sudoku.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SudokuGUI sudokuGUI;
    private PanelMenuPrincipal menuPrincipal;
    private PanelRanking panelRanking;
    private PanelRegistroVictoria panelVictoria;
    private PanelTutorial panelTutorial;

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
        panelRanking = new PanelRanking(this);
        panelVictoria = new PanelRegistroVictoria(this);
        panelTutorial = new PanelTutorial(this);

        mainPanel.add(menuPrincipal, "MENU");
        mainPanel.add(sudokuGUI, "JUEGO");
        mainPanel.add(panelRanking, "RANKING");
        mainPanel.add(panelVictoria, "VICTORIA");
        mainPanel.add(panelTutorial, "TUTORIAL");

        add(mainPanel);
    }

    public void mostrarJuego() {
        sudokuGUI.limpiarTablero();
        cardLayout.show(mainPanel, "JUEGO");
        sudokuGUI.requestFocusInWindow();
    }

    public void mostrarMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    public void mostrarVictoria(int segundos, String dificultad) {
        panelVictoria.setDatosVictoria(segundos, dificultad);
        cardLayout.show(mainPanel, "VICTORIA");
    }

    public void mostrarRanking(String dificultad) {
        panelRanking.cargarRanking(dificultad);
        cardLayout.show(mainPanel, "RANKING");
    }

    public void mostrarTutorial() {
        cardLayout.show(mainPanel, "TUTORIAL");
    }
}
