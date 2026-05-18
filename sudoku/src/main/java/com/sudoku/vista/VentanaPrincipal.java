package com.sudoku.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación SUDO.ku.
 * Actúa como contenedor principal utilizando un {@link CardLayout} para alternar
 * de forma fluida entre las diferentes pantallas del juego (menú, juego, ranking, tutorial y victoria).
 */
public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SudokuGUI sudokuGUI;
    private PanelMenuPrincipal menuPrincipal;
    private PanelRanking panelRanking;
    private PanelRegistroVictoria panelVictoria;
    private PanelTutorial panelTutorial;

    /**
     * Constructor de la ventana principal.
     * Configura las dimensiones, inicializa todas las vistas del juego y establece
     * el menú principal como la pantalla inicial.
     */
    public VentanaPrincipal() {
        super("SUDO.ku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(600, 650);
        setLocationRelativeTo(null);
        
        // Cargar el icono de la ventana principal
        try {
            java.net.URL imgURL = getClass().getResource("/images/iconosudo.png");
            if (imgURL != null) {
                setIconImage(new ImageIcon(imgURL).getImage());
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }

        // Configuramos el gestor de diseño CardLayout para intercambiar paneles
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Instancio las distintas pantallas del juego
        sudokuGUI = new SudokuGUI();
        menuPrincipal = new PanelMenuPrincipal(this);
        panelRanking = new PanelRanking(this);
        panelVictoria = new PanelRegistroVictoria(this);
        panelTutorial = new PanelTutorial(this);

        // Añado los paneles al CardLayout con un identificador único
        mainPanel.add(menuPrincipal, "MENU");
        mainPanel.add(sudokuGUI, "JUEGO");
        mainPanel.add(panelRanking, "RANKING");
        mainPanel.add(panelVictoria, "VICTORIA");
        mainPanel.add(panelTutorial, "TUTORIAL");

        add(mainPanel);
    }

    /**
     * Limpia el tablero actual y cambia la vista activa a la pantalla de juego.
     */
    public void mostrarJuego() {
        sudokuGUI.limpiarTablero();
        cardLayout.show(mainPanel, "JUEGO");
        sudokuGUI.requestFocusInWindow();
    }

    /**
     * Cambia la vista activa al menú principal de la aplicación.
     */
    public void mostrarMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    /**
     * Muestra la pantalla de registro de victoria tras completar con éxito un Sudoku.
     *
     * @param segundos Tiempo total transcurrido en segundos.
     * @param dificultad Dificultad en la que se ha superado la partida.
     */
    public void mostrarVictoria(int segundos, String dificultad) {
        panelVictoria.setDatosVictoria(segundos, dificultad);
        cardLayout.show(mainPanel, "VICTORIA");
    }

    /**
     * Carga las puntuaciones de la base de datos y muestra la pantalla de ranking.
     *
     * @param dificultad Dificultad de la cual se desean consultar los mejores tiempos.
     */
    public void mostrarRanking(String dificultad) {
        panelRanking.cargarRanking(dificultad);
        cardLayout.show(mainPanel, "RANKING");
    }

    /**
     * Cambia la vista activa a la pantalla explicativa del tutorial.
     */
    public void mostrarTutorial() {
        cardLayout.show(mainPanel, "TUTORIAL");
    }
}
