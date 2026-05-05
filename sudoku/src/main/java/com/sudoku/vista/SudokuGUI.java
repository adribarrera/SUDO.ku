package com.sudoku.vista;

import com.sudoku.modelo.Sudoku;
import com.sudoku.modelo.GestorPuntuaciones;
import com.sudoku.modelo.RegistroPuntuacion;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuGUI extends JPanel {

    private Sudoku sudoku;
    private JTextField[][] celdas;
    private JComboBox<String> comboDificultad;
    private JButton btnGenerar;
    private JButton btnVerificar;
    private Timer timer;
    private int segundosTranscurridos;
    private JLabel lblTimer;
    
    // Variables para el modo Hardcore
    private int vidas = 3;
    private JLabel[] iconosVidas;
    private JPanel panelVidas;
    private JPanel panelVacio; // Para compensar visualmente el reloj
    private ImageIcon iconoVidaLlena;
    private ImageIcon iconoVidaVacia;

    public SudokuGUI() {
        this.sudoku = new Sudoku();
        this.celdas = new JTextField[9][9];

        configurarPanel();
        inicializarComponentes();

        // Generar un tablero Fácil por defecto
        generarNuevoTablero("Fácil", false);
    }

    private ImageIcon escalarIcono(String ruta, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(ruta);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            } else {
                System.err.println("No se encontró el recurso: " + ruta);
                return new ImageIcon();
            }
        } catch (Exception e) {
            System.err.println("Error escalando icono: " + e.getMessage());
            return new ImageIcon();
        }
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42));
    }

    private void inicializarComponentes() {
        configurarPanelControles();
        configurarPanelTablero();
    }

    private void configurarPanelControles() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(15, 23, 42));

        // Configuración del Temporizador
        lblTimer = new JLabel("00:00", SwingConstants.CENTER);
        lblTimer.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTimer.setForeground(new Color(226, 232, 240));
        lblTimer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblTimer.setVisible(false); // Oculto al inicio según pidió el usuario
        
        timer = new Timer(1000, e -> {
            segundosTranscurridos++;
            int minutos = segundosTranscurridos / 60;
            int segundos = segundosTranscurridos % 60;
            lblTimer.setText(String.format("%02d:%02d", minutos, segundos));
        });

        panelSuperior.add(lblTimer, BorderLayout.CENTER);

        // Panel de Vidas (Hardcore)
        iconoVidaLlena = escalarIcono("/images/Vida.png", 30, 30);
        iconoVidaVacia = escalarIcono("/images/vidaMenos.png", 30, 30);

        panelVidas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVidas.setBackground(new Color(15, 23, 42));
        iconosVidas = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            iconosVidas[i] = new JLabel(iconoVidaLlena);
            panelVidas.add(iconosVidas[i]);
        }
        panelVidas.setVisible(false);
        panelSuperior.add(panelVidas, BorderLayout.EAST);

        // Compensación visual izquierda para mantener el centro
        panelVacio = new JPanel();
        panelVacio.setPreferredSize(new Dimension(100, 30));
        panelVacio.setBackground(new Color(15, 23, 42));
        panelVacio.setVisible(false);
        panelSuperior.add(panelVacio, BorderLayout.WEST);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new FlowLayout());
        panelControles.setBackground(new Color(30, 41, 59));

        panelControles.add(new JLabel("Dificultad:"));
        comboDificultad = new JComboBox<>(new String[] { "Fácil", "Medio", "Difícil", "Hardcore", "Prueba" });
        panelControles.add(comboDificultad);

        btnGenerar = new JButton("Nueva Partida");
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBackground(new Color(100, 150, 255));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.addActionListener(e -> generarNuevoTablero((String) comboDificultad.getSelectedItem(), true));
        panelControles.add(btnGenerar);

        btnVerificar = new JButton("Verificar Victoria");
        btnVerificar.setFocusPainted(false);
        btnVerificar.setBackground(new Color(100, 200, 100));
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.addActionListener(e -> verificarVictoria());
        panelControles.add(btnVerificar);

        panelSuperior.add(panelControles, BorderLayout.SOUTH);

        this.add(panelSuperior, BorderLayout.NORTH);
    }

    private void configurarPanelTablero() {
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(9, 9));
        panelTablero.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTablero.setBackground(new Color(15, 23, 42));

        Font fuenteCelda = new Font("SansSerif", Font.BOLD, 26);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField celda = new JTextField();
                celda.setHorizontalAlignment(JTextField.CENTER);
                celda.setFont(fuenteCelda);

                // Bordes para simular los bloques 3x3 del Sudoku
                int top = 1;
                if (i % 3 == 0) {
                    top = 3;
                }

                int left = 1;
                if (j % 3 == 0) {
                    left = 3;
                }

                int bottom = 0;
                if (i == 8) {
                    bottom = 3;
                }

                int right = 0;
                if (j == 8) {
                    right = 3;
                }

                celda.setBorder(BorderFactory.createCompoundBorder(
                        new MatteBorder(top, left, bottom, right, new Color(100, 116, 139)),
                        BorderFactory.createEmptyBorder()));

                final int finalI = i;
                final int finalJ = j;

                // Prevenir caracteres no deseados en la interfaz
                celda.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        // Ignorar cualquier ingreso si la celda es pista original
                        // o si el caracter no es un numero del 1 al 9.
                        if (c < '1' || c > '9' || sudoku.esCeldaFija(finalI, finalJ)) {
                            e.consume();
                        } else {
                            celda.setText(""); // Despejar para que solo atrape un dígito
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        // Si la celda es fija, su valor nunca se cambiará.
                        if (sudoku.esCeldaFija(finalI, finalJ)) {
                            return;
                        }

                        String texto = celda.getText().trim();
                        if (!texto.isEmpty()) {
                            try {
                                int valor = Integer.parseInt(texto);
                                // Intentar colocar el número en el modelo
                                boolean exito = sudoku.colocarNumero(finalI, finalJ, valor);

                                if (!exito) {
                                    celda.setForeground(Color.RED); // Error visual inmediato
                                } else {
                                    celda.setForeground(new Color(96, 165, 250)); // Color azul brillante
                                }
                            } catch (NumberFormatException ex) {
                                celda.setText("");
                            }
                        } else {
                            // Borrar el número en el modelo si lo vacíamos
                            sudoku.colocarNumero(finalI, finalJ, 0);
                        }
                    }
                });

                celdas[i][j] = celda;
                panelTablero.add(celda);
            }
        }

        this.add(panelTablero, BorderLayout.CENTER);
    }

    private void generarNuevoTablero(String dificultad, boolean arrancarTimer) {
        if (timer != null) timer.stop();
        segundosTranscurridos = 0;
        if (lblTimer != null) lblTimer.setText("00:00");
        
        if (arrancarTimer) {
            lblTimer.setVisible(true);
            if (timer != null) timer.start();
            comboDificultad.setEnabled(false); // Bloquear mientras se juega
        } else {
            lblTimer.setVisible(false);
            comboDificultad.setEnabled(true);
        }

        if (dificultad.equalsIgnoreCase("Hardcore")) {
            vidas = 3;
            for (int i = 0; i < 3; i++) {
                iconosVidas[i].setIcon(iconoVidaLlena);
            }
            panelVidas.setVisible(true);
            panelVacio.setVisible(true);
        } else {
            panelVidas.setVisible(false);
            panelVacio.setVisible(false);
        }

        // Enviar dificultad formateada (sin acentos) para emparejar con la lógica de
        // switch del modelo
        String difModelo = dificultad.toLowerCase().replace("á", "a").replace("í", "i");
        sudoku.generarTablero(difModelo);
        actualizarVistaTablero();
    }

    private void actualizarVistaTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField celda = celdas[i][j];
                int valor = sudoku.getValor(i, j);

                if (valor != 0) {
                    celda.setText(String.valueOf(valor));
                    if (sudoku.esCeldaFija(i, j)) {
                        celda.setEditable(false);
                        celda.setBackground(new Color(30, 41, 59)); // Fondo gris claro para pistas
                        celda.setForeground(new Color(226, 232, 240));
                    } else {
                        celda.setEditable(true);
                        celda.setBackground(new Color(15, 23, 42));
                        celda.setForeground(new Color(96, 165, 250));
                    }
                } else {
                    celda.setText("");
                    celda.setEditable(true);
                    celda.setBackground(new Color(15, 23, 42));
                    celda.setForeground(new Color(96, 165, 250));
                }
            }
        }
    }

    private void verificarVictoria() {
        if (sudoku.estaResuelto()) {
            if (timer != null) timer.stop();
            comboDificultad.setEnabled(true); // Desbloquear al ganar
            JOptionPane.showMessageDialog(this,
                    "¡Felicidades! Has resuelto el Sudoku correctamente en " + lblTimer.getText() + ".",
                    "¡Victoria!", JOptionPane.INFORMATION_MESSAGE);
            
            String nombre = JOptionPane.showInputDialog(this, "Introduce tu nombre para el ranking:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                String difSeleccionada = (String) comboDificultad.getSelectedItem();
                RegistroPuntuacion registro = new RegistroPuntuacion(nombre.trim(), difSeleccionada, segundosTranscurridos);
                GestorPuntuaciones.guardarPuntuacion(registro);
                
                PanelRanking panelRanking = new PanelRanking((JFrame) SwingUtilities.getWindowAncestor(this), difSeleccionada);
                panelRanking.setVisible(true);
            }
        } else {
            String difSeleccionada = (String) comboDificultad.getSelectedItem();
            if (difSeleccionada.equalsIgnoreCase("Hardcore")) {
                vidas--;
                if (vidas >= 0 && vidas < 3) {
                    iconosVidas[vidas].setIcon(iconoVidaVacia); // Al perder una, se apaga el corazón correspondiente a ese índice
                }
                
                if (vidas <= 0) {
                    if (timer != null) timer.stop();
                    comboDificultad.setEnabled(true); // Desbloquear al morir
                    JOptionPane.showMessageDialog(this,
                            "Has perdido tus 3 vidas. ¡GAME OVER!",
                            "Fin de la partida", JOptionPane.ERROR_MESSAGE);
                    // Bloquear tablero
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            celdas[i][j].setEditable(false);
                        }
                    }
                    return;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "¡Error en el tablero! Pierdes 1 vida. Te quedan: " + vidas,
                            "¡Cuidado!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Comprobar si al menos hay celdas vacías
            boolean hayVacias = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (sudoku.getValor(i, j) == 0) {
                        hayVacias = true;
                        break;
                    }
                }
            }
            if (hayVacias) {
                JOptionPane.showMessageDialog(this,
                        "El tablero aún tiene celdas vacías. ¡Sigue completándolo!",
                        "Juego Incompleto", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El tablero está lleno pero hay errores (en rojo). Revísalos.",
                        "Revisa tus movimientos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
