package com.sudoku.vista;

import javax.swing.*;
import java.awt.*;

public class PanelMenuPrincipal extends JPanel {
    private VentanaPrincipal ventanaPadre;

    public PanelMenuPrincipal(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(15, 23, 42)); // Mismo fondo oscuro

        // Espacio para bajar el título a 1/3 de la ventana
        add(Box.createVerticalStrut(160));

        // Título con imagen
        JLabel lblTitulo = new JLabel();
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon logoIcon = escalarIcono("/images/ku.png", 400, 100);
        if (logoIcon != null) {
            lblTitulo.setIcon(logoIcon);
        } else {
            // Fallback por si la imagen falla
            lblTitulo.setText("SUDO.ku");
            lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 64));
            lblTitulo.setForeground(new Color(226, 232, 240));
        }
        add(lblTitulo);

        // Espacio entre título y botones
        add(Box.createVerticalStrut(70));

        // Botones
        JButton btnComenzar = createButton("Comenzar", new Color(34, 197, 94)); // Verde
        btnComenzar.addActionListener(e -> ventanaPadre.mostrarJuego());
        add(btnComenzar);

        add(Box.createVerticalStrut(20));

        JButton btnTutorial = createButton("Tutorial", new Color(100, 150, 255));
        btnTutorial.addActionListener(e -> mostrarTutorial());
        add(btnTutorial);

        add(Box.createVerticalStrut(20));

        JButton btnSalir = createButton("Salir", new Color(220, 38, 38));
        btnSalir.addActionListener(e -> System.exit(0));
        add(btnSalir);

        add(Box.createVerticalGlue());
    }

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

    private void mostrarTutorial() {
        String mensaje = "¡Bienvenido a SUDO.ku!\n\n" +
                "Objetivo del juego:\n" +
                "Rellenar la cuadrícula de 9x9 con números del 1 al 9, de forma que\n" +
                "cada número aparezca exactamente una vez en cada fila, columna y\n" +
                "subcuadrícula de 3x3.\n\n" +
                "Modos de juego:\n" +
                "- Fácil: Ideal para principiantes, con muchas pistas iniciales.\n" +
                "- Medio: Un desafío moderado para jugadores con experiencia.\n" +
                "- Difícil: Para los más expertos, con muy pocas pistas.\n" +
                "- Hardcore: Tendrás 3 vidas. Cada error resta una vida.\n" +
                "  ¡Si te quedas a 0, Game Over!";

        JOptionPane.showMessageDialog(this, mensaje, "Tutorial", JOptionPane.INFORMATION_MESSAGE);
    }

    private ImageIcon escalarIcono(String ruta, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(ruta);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
