package com.sudoku.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que representa el menú principal de la aplicación SUDO.ku.
 * Proporciona la interfaz inicial con el logotipo del juego y las opciones
 * para comenzar una nueva partida, consultar el tutorial o salir del juego.
 */
public class PanelMenuPrincipal extends JPanel {
    private VentanaPrincipal ventanaPadre;

    /**
     * Constructor del menú principal.
     * Configura el diseño vertical, carga el logotipo y añade los botones de
     * navegación.
     *
     * @param ventanaPadre Referencia a la ventana principal para gestionar el
     *                     cambio de pantallas.
     */
    public PanelMenuPrincipal(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(15, 23, 42)); // Mismo fondo oscuro del diseño general

        // Espacio para bajar el título a aproximadamente 1/3 de la ventana
        add(Box.createVerticalStrut(160));

        // Título principal con imagen del logotipo
        JLabel lblTitulo = new JLabel();
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon logoIcon = escalarIcono("/images/ku.png", 400, 100);
        if (logoIcon != null) {
            lblTitulo.setIcon(logoIcon);
        } else {
            // Fallback en texto por si la imagen no se encuentra o falla al cargar
            lblTitulo.setText("SUDO.ku");
            lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 64));
            lblTitulo.setForeground(new Color(226, 232, 240));
        }
        add(lblTitulo);

        // Espacio vertical entre el título y los botones de acción
        add(Box.createVerticalStrut(70));

        // Botón Comenzar inicia el flujo de juego
        JButton btnComenzar = createButton("Comenzar", new Color(34, 197, 94)); // Verde
        btnComenzar.addActionListener(e -> this.ventanaPadre.mostrarJuego());
        add(btnComenzar);

        add(Box.createVerticalStrut(20));

        // Botón Tutorial muestra las reglas y modos de juego
        JButton btnTutorial = createButton("Tutorial", new Color(100, 150, 255));
        btnTutorial.addActionListener(e -> this.ventanaPadre.mostrarTutorial());
        add(btnTutorial);

        add(Box.createVerticalStrut(20));

        // Botón Salir cierra la aplicación completamente
        JButton btnSalir = createButton("Salir", new Color(220, 38, 38)); // Rojo
        btnSalir.addActionListener(e -> System.exit(0));
        add(btnSalir);

        // Empuja los elementos hacia arriba para mantener el diseño centrado
        add(Box.createVerticalGlue());
    }

    /**
     * Crea y configura un botón estandarizado para el menú principal con esquinas
     * redondeadas.
     *
     * @param text    Texto que mostrará el botón.
     * @param bgColor Color de fondo del botón.
     * @return JButton configurado con las dimensiones y estilo del proyecto.
     */
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);

        // Propiedades de FlatLaf para redondear ligeramente las esquinas
        btn.putClientProperty("FlatLaf.style", "arc: 15");

        return btn;
    }

    /**
     * Carga y escala una imagen desde los recursos del proyecto para adaptarla a la
     * interfaz.
     *
     * @param ruta   Ruta relativa del recurso de imagen.
     * @param width  Ancho deseado para el icono escalado.
     * @param height Alto deseado para el icono escalado.
     * @return ImageIcon escalado, o null si ocurre un error al cargar la imagen.
     */
    private ImageIcon escalarIcono(String ruta, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(ruta);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                // Escalamos la imagen manteniendo suavidad en los bordes
                Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
