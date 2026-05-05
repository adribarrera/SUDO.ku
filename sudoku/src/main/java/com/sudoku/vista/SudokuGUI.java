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
    public SudokuGUI() {
        this.sudoku = new Sudoku();
        this.celdas = new JTextField[9][9];

        configurarPanel();
        inicializarComponentes();

        // Generar un tablero Fácil por defecto
        generarNuevoTablero("Fácil", false);
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
        
        timer = new Timer(1000, e -> {
            segundosTranscurridos++;
            int minutos = segundosTranscurridos / 60;
            int segundos = segundosTranscurridos % 60;
            lblTimer.setText(String.format("%02d:%02d", minutos, segundos));
        });

        panelSuperior.add(lblTimer, BorderLayout.CENTER);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new FlowLayout());
        panelControles.setBackground(new Color(30, 41, 59));

        panelControles.add(new JLabel("Dificultad:"));
        comboDificultad = new JComboBox<>(new String[] { "Fácil", "Medio", "Difícil", "Prueba" });
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
        if (timer != null && arrancarTimer) timer.start();

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
