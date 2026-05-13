package com.sudoku.vista;

import com.sudoku.modelo.GestorPuntuaciones;
import com.sudoku.modelo.RegistroPuntuacion;

import javax.swing.*;
import java.awt.*;

public class PanelRegistroVictoria extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private int segundosTranscurridos;
    private String dificultad;

    private JLabel lblTiempo;
    private JTextField txtNombre;
    private JLabel lblError;

    public PanelRegistroVictoria(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        configurarPanel();
        inicializarComponentes();
    }

    private void configurarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(15, 23, 42)); // Mismo fondo oscuro
    }

    private void inicializarComponentes() {
        add(Box.createVerticalStrut(100));

        // Título de enhorabuena
        JLabel lblTitulo = new JLabel("¡Enhorabuena!");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTitulo.setForeground(new Color(250, 204, 21)); // Amarillo dorado
        add(lblTitulo);

        add(Box.createVerticalStrut(20));

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Has resuelto el Sudoku correctamente.");
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblSubtitulo.setForeground(new Color(226, 232, 240));
        add(lblSubtitulo);

        add(Box.createVerticalStrut(10));

        // Etiqueta de tiempo (se actualiza dinámicamente)
        lblTiempo = new JLabel("Tiempo: 00:00");
        lblTiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTiempo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTiempo.setForeground(new Color(56, 189, 248)); // Azul claro
        add(lblTiempo);

        add(Box.createVerticalStrut(60));

        // Formulario para nombre
        JLabel lblNombre = new JLabel("Introduce tu nombre para el ranking:");
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblNombre.setForeground(new Color(226, 232, 240));
        add(lblNombre);

        add(Box.createVerticalStrut(10));

        txtNombre = new JTextField();
        txtNombre.setMaximumSize(new Dimension(300, 40));
        txtNombre.setPreferredSize(new Dimension(300, 40));
        txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtNombre.setBackground(new Color(30, 41, 59));
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(txtNombre);

        add(Box.createVerticalStrut(5));

        // Label de error (inicialmente oculto o sin texto)
        lblError = new JLabel(" ");
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblError.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblError.setForeground(Color.WHITE); // El usuario pidió texto blanco
        add(lblError);

        add(Box.createVerticalStrut(40));

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setBackground(new Color(15, 23, 42));
        panelBotones.setMaximumSize(new Dimension(500, 60));

        JButton btnCancelar = createButton("Cancelar", new Color(220, 38, 38));
        btnCancelar.addActionListener(e -> ventanaPadre.mostrarMenu());

        JButton btnEnviar = createButton("Enviar", new Color(34, 197, 94));
        btnEnviar.addActionListener(e -> enviarPuntuacion());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnEnviar);

        add(panelBotones);
        add(Box.createVerticalGlue());
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.putClientProperty("FlatLaf.style", "arc: 15");
        return btn;
    }

    private void enviarPuntuacion() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            lblError.setText("¡Debes introducir un nombre válido!");
            txtNombre.requestFocus();
        } else {
            lblError.setText(" "); // Limpiar error
            RegistroPuntuacion registro = new RegistroPuntuacion(nombre, dificultad, segundosTranscurridos);
            GestorPuntuaciones.guardarPuntuacion(registro);
            ventanaPadre.mostrarRanking(dificultad);
        }
    }

    public void setDatosVictoria(int segundos, String dificultad) {
        this.segundosTranscurridos = segundos;
        this.dificultad = dificultad;
        
        int minutos = segundos / 60;
        int segs = segundos % 60;
        lblTiempo.setText(String.format("Tiempo: %02d:%02d", minutos, segs));
        
        // Limpiar el campo para una nueva partida
        txtNombre.setText("");
        lblError.setText(" ");
    }
}
