package com.sudoku.vista;

import com.sudoku.modelo.Sudoku;
import com.sudoku.principal.JuegoSudoku;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Panel principal de la interfaz gráfica del juego de Sudoku (SUDO.ku).
 * Gestiona la representación visual del tablero de 9x9, el temporizador de la
 * partida,
 * la selección de dificultad, el modo Hardcore (con sistema de vidas) y la
 * interacción
 * directa del usuario mediante teclado y ratón.
 */
public class SudokuGUI extends JPanel {

    private Sudoku sudoku;
    private JTextField[][] celdas;
    private JComboBox<String> comboDificultad;
    private JButton btnGenerar;
    private JButton btnVerificar;
    private Timer timer;
    private int segundosTranscurridos;
    private JLabel lblTimer;
    private JButton btnVolverMenu;
    private boolean partidaIniciada = false;
    private CardLayout cardLayoutSuperior;
    private JPanel panelCentroSuperior;

    // Variables para el modo Hardcore
    private int vidas = 3;
    private JLabel[] iconosVidas;
    private JPanel panelVidas;
    private JPanel panelVacio; // Para compensar visualmente el reloj y mantener el centrado
    private ImageIcon iconoVidaLlena;
    private ImageIcon iconoVidaVacia;

    /**
     * Constructor de la interfaz gráfica de Sudoku.
     * Inicializa el modelo interno del juego, la matriz de campos de texto y
     * configura
     * la disposición visual de los paneles de control y el tablero principal.
     */
    public SudokuGUI() {
        this.sudoku = new Sudoku();
        this.celdas = new JTextField[9][9];

        configurarPanel();
        inicializarComponentes();

        limpiarTablero();
    }

    /**
     * Carga y escala una imagen desde los recursos del proyecto para adaptarla a la
     * interfaz.
     *
     * @param ruta   Ruta relativa del recurso de imagen.
     * @param width  Ancho deseado para el icono escalado.
     * @param height Alto deseado para el icono escalado.
     * @return ImageIcon escalado, o un ImageIcon vacío si ocurre un error al
     *         cargar.
     */
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

    /**
     * Configura el diseño principal del contenedor (BorderLayout) y su color de
     * fondo.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42)); // Fondo oscuro de la aplicación
    }

    /**
     * Inicializa y organiza los subpaneles principales: la barra superior de
     * controles
     * y la cuadrícula central del tablero.
     */
    private void inicializarComponentes() {
        configurarPanelControles();
        configurarPanelTablero();
    }

    /**
     * Configura el panel superior que alberga el logotipo del juego, el
     * temporizador,
     * los iconos de vidas (para el modo Hardcore) y la barra inferior de botones de
     * acción
     * (selección de dificultad, nueva partida, verificar victoria y volver al
     * menú).
     */
    private void configurarPanelControles() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(15, 23, 42));

        cardLayoutSuperior = new CardLayout();
        panelCentroSuperior = new JPanel(cardLayoutSuperior);
        panelCentroSuperior.setBackground(new Color(15, 23, 42));

        // Configuración del Logo principal
        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        ImageIcon logoIcon = escalarIcono("/images/ku.png", 180, 45); // Escalar al tamaño de la barra superior
        if (logoIcon != null) {
            lblLogo.setIcon(logoIcon);
        } else {
            lblLogo.setText("SUDO.ku");
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 36));
            lblLogo.setForeground(new Color(226, 232, 240));
        }

        // Configuración del Temporizador de la partida
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

        panelCentroSuperior.add(lblLogo, "LOGO");
        panelCentroSuperior.add(lblTimer, "TIMER");

        panelSuperior.add(panelCentroSuperior, BorderLayout.CENTER);

        // Panel de Vidas exclusivo para el modo Hardcore
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

        // Compensación visual izquierda para mantener el temporizador y el logo
        // perfectamente centrados
        panelVacio = new JPanel();
        panelVacio.setPreferredSize(new Dimension(100, 30));
        panelVacio.setBackground(new Color(15, 23, 42));
        panelVacio.setVisible(false);
        panelSuperior.add(panelVacio, BorderLayout.WEST);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new FlowLayout());
        panelControles.setBackground(new Color(30, 41, 59));

        panelControles.add(new JLabel("Dificultad:"));
        if (JuegoSudoku.MODO_DEBUG) {
            comboDificultad = new JComboBox<>(new String[] { "Fácil", "Medio", "Difícil", "Hardcore", "Prueba" });
        } else {
            comboDificultad = new JComboBox<>(new String[] { "Fácil", "Medio", "Difícil", "Hardcore" });
        }
        panelControles.add(comboDificultad);

        btnGenerar = new JButton("Nueva Partida");
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBackground(new Color(100, 150, 255));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.addActionListener(e -> {
            if (partidaIniciada) {
                rendirse();
            } else {
                generarNuevoTablero((String) comboDificultad.getSelectedItem(), true);
            }
        });
        panelControles.add(btnGenerar);

        btnVerificar = new JButton("Verificar Victoria");
        btnVerificar.setFocusPainted(false);
        btnVerificar.setBackground(new Color(100, 200, 100));
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setEnabled(false); // Deshabilitado hasta que comience una partida
        btnVerificar.addActionListener(e -> verificarVictoria());
        panelControles.add(btnVerificar);

        btnVolverMenu = new JButton("Volver al Menú");
        btnVolverMenu.setFocusPainted(false);
        btnVolverMenu.setBackground(new Color(220, 38, 38)); // Rojo
        btnVolverMenu.setForeground(Color.WHITE);
        btnVolverMenu.addActionListener(e -> volverAlMenu());
        panelControles.add(btnVolverMenu);

        panelSuperior.add(panelControles, BorderLayout.SOUTH);

        this.add(panelSuperior, BorderLayout.NORTH);
    }

    /**
     * Crea y configura la cuadrícula central de 9x9 campos de texto (JTextField).
     * Establece los bordes compuestos para diferenciar visualmente los bloques de
     * 3x3
     * e implementa los listeners de foco y teclado para la navegación y entrada de
     * datos.
     */
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

                // Configuramos bordes de grosor variable para simular las subcuadrículas 3x3
                // del Sudoku
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 0;
                int right = (j == 8) ? 3 : 0;

                celda.setBorder(BorderFactory.createCompoundBorder(
                        new MatteBorder(top, left, bottom, right, new Color(100, 116, 139)),
                        BorderFactory.createEmptyBorder()));

                final int finalI = i;
                final int finalJ = j;

                // Listener para resaltar dinámicamente filas, columnas y cuadrantes al enfocar
                // una celda
                celda.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        resaltarCeldas(finalI, finalJ);
                    }
                });

                // Listener de teclado para control de navegación y filtrado de caracteres
                celda.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int code = e.getKeyCode();
                        // Implementamos navegación cíclica por el tablero utilizando aritmética
                        // modular.
                        // Sumar 8 y hacer módulo 9 equivale a restar 1 de forma segura sin dar
                        // resultados negativos.
                        if (code == KeyEvent.VK_UP) {
                            celdas[(finalI + 8) % 9][finalJ].requestFocus();
                        } else if (code == KeyEvent.VK_DOWN) {
                            celdas[(finalI + 1) % 9][finalJ].requestFocus();
                        } else if (code == KeyEvent.VK_LEFT) {
                            celdas[finalI][(finalJ + 8) % 9].requestFocus();
                        } else if (code == KeyEvent.VK_RIGHT) {
                            celdas[finalI][(finalJ + 1) % 9].requestFocus();
                        }
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        // Ignoramos la entrada si la celda es una pista fija original
                        // o si el carácter introducido no es un número del 1 al 9.
                        if (c < '1' || c > '9' || sudoku.esCeldaFija(finalI, finalJ)) {
                            e.consume();
                        } else {
                            celda.setText(""); // Despejamos el contenido previo para atrapar únicamente el nuevo dígito
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        // Si la celda es fija, su valor en el modelo nunca debe alterarse.
                        if (sudoku.esCeldaFija(finalI, finalJ)) {
                            return;
                        }

                        String texto = celda.getText().trim();
                        if (!texto.isEmpty()) {
                            try {
                                int valor = Integer.parseInt(texto);
                                // Intentamos colocar el número introducido en el modelo del juego
                                sudoku.colocarNumero(finalI, finalJ, valor);
                            } catch (NumberFormatException ex) {
                                celda.setText("");
                                sudoku.colocarNumero(finalI, finalJ, 0);
                            }
                        } else {
                            // Borramos el número en el modelo si el usuario ha vaciado la celda
                            sudoku.colocarNumero(finalI, finalJ, 0);
                        }

                        // Actualizamos la retroalimentación visual de errores y resaltados
                        actualizarColoresErrores();
                        resaltarCeldas(finalI, finalJ);
                    }
                });

                celdas[i][j] = celda;
                panelTablero.add(celda);
            }
        }

        this.add(panelTablero, BorderLayout.CENTER);
    }

    /**
     * Genera un nuevo tablero de Sudoku según la dificultad seleccionada.
     * Reinicia el temporizador, gestiona la visibilidad del sistema de vidas en
     * modo Hardcore
     * y actualiza el estado de los botones de control.
     *
     * @param dificultad    Cadena que representa la dificultad (Fácil, Medio,
     *                      Difícil, Hardcore, Prueba).
     * @param arrancarTimer Indica si debe iniciarse el temporizador al crear el
     *                      tablero.
     */
    private void generarNuevoTablero(String dificultad, boolean arrancarTimer) {
        if (timer != null)
            timer.stop();
        segundosTranscurridos = 0;
        if (lblTimer != null)
            lblTimer.setText("00:00");

        if (arrancarTimer) {
            if (cardLayoutSuperior != null && panelCentroSuperior != null) {
                cardLayoutSuperior.show(panelCentroSuperior, "TIMER");
            }
            if (timer != null)
                timer.start();
            comboDificultad.setEnabled(false); // Bloqueamos el selector de dificultad mientras se juega
        } else {
            if (cardLayoutSuperior != null && panelCentroSuperior != null) {
                cardLayoutSuperior.show(panelCentroSuperior, "LOGO");
            }
            comboDificultad.setEnabled(true);
        }

        // Configuración específica para el modo Hardcore
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

        // Adaptamos la cadena de dificultad eliminando tildes para la correcta lectura
        // en el modelo
        String difModelo = dificultad.toLowerCase().replace("á", "a").replace("í", "i");
        sudoku.generarTablero(difModelo);
        actualizarVistaTablero();

        if (arrancarTimer) {
            partidaIniciada = true;
            btnGenerar.setText("Rendirse");
            btnGenerar.setBackground(new Color(249, 115, 22)); // Naranja
            if (btnVolverMenu != null)
                btnVolverMenu.setVisible(true);
            if (btnVerificar != null)
                btnVerificar.setEnabled(true);
        }
    }

    /**
     * Navega de vuelta al menú principal de la aplicación obteniendo la ventana
     * contenedora.
     */
    private void volverAlMenu() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof VentanaPrincipal) {
            ((VentanaPrincipal) window).mostrarMenu();
        }
    }

    /**
     * Finaliza la partida actual por rendición del usuario.
     * Detiene el temporizador, bloquea las celdas y revela la solución completa
     * del tablero resaltando los números resueltos en color amarillo dorado.
     */
    private void rendirse() {
        if (timer != null)
            timer.stop();
        partidaIniciada = false;

        btnGenerar.setText("Nueva Partida");
        btnGenerar.setBackground(new Color(100, 150, 255)); // Azul original de botón
        if (btnVolverMenu != null)
            btnVolverMenu.setVisible(true);
        if (btnVerificar != null)
            btnVerificar.setEnabled(false);
        comboDificultad.setEnabled(true);

        // Mostramos la solución en el tablero marcando las celdas en amarillo dorado
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField celda = celdas[i][j];
                celda.setEditable(false);
                if (!sudoku.esCeldaFija(i, j)) {
                    int valorResuelto = sudoku.getValorResuelto(i, j);
                    if (valorResuelto != 0) {
                        celda.setText(String.valueOf(valorResuelto));
                        celda.setForeground(new Color(250, 204, 21)); // Amarillo dorado
                    }
                }
            }
        }
    }

    /**
     * Restablece por completo el estado del tablero y la interfaz gráfica.
     * Detiene temporizadores, oculta paneles de vidas, limpia las celdas de texto
     * y genera una instancia completamente nueva del modelo de Sudoku.
     */
    public void limpiarTablero() {
        if (timer != null)
            timer.stop();
        segundosTranscurridos = 0;
        if (lblTimer != null) {
            lblTimer.setText("00:00");
        }
        if (cardLayoutSuperior != null && panelCentroSuperior != null) {
            cardLayoutSuperior.show(panelCentroSuperior, "LOGO");
        }
        if (comboDificultad != null) {
            comboDificultad.setEnabled(true);
        }
        if (panelVidas != null)
            panelVidas.setVisible(false);
        if (panelVacio != null)
            panelVacio.setVisible(false);

        partidaIniciada = false;
        if (btnGenerar != null) {
            btnGenerar.setText("Nueva Partida");
            btnGenerar.setBackground(new Color(100, 150, 255)); // Azul original
        }
        if (btnVolverMenu != null)
            btnVolverMenu.setVisible(true);
        if (btnVerificar != null)
            btnVerificar.setEnabled(false);

        this.sudoku = new Sudoku();

        if (celdas != null) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (celdas[i][j] != null) {
                        celdas[i][j].setText("");
                        celdas[i][j].setEditable(false);
                        celdas[i][j].setBackground(new Color(15, 23, 42));
                        celdas[i][j].setForeground(new Color(96, 165, 250));
                    }
                }
            }
        }
    }

    /**
     * Resalta visualmente las celdas relacionadas con la celda actualmente
     * enfocada.
     * Aplica distintos tonos de azul y gris para destacar la propia celda, las
     * celdas
     * que contienen el mismo número y todas las celdas de su misma fila, columna y
     * cuadrante 3x3.
     *
     * @param filaFocus Fila de la celda con el foco actual.
     * @param colFocus  Columna de la celda con el foco actual.
     */
    private void resaltarCeldas(int filaFocus, int colFocus) {
        String valorFocusStr = celdas[filaFocus][colFocus].getText().trim();
        int valorFocus = valorFocusStr.isEmpty() ? 0 : Integer.parseInt(valorFocusStr);
        int bloqueFila = filaFocus / 3;
        int bloqueCol = colFocus / 3;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField celda = celdas[i][j];
                String valorStr = celda.getText().trim();
                int valorCelda = valorStr.isEmpty() ? 0 : Integer.parseInt(valorStr);

                // Colores base de fondo según si la celda es pista original o celda editable
                Color bgColor = sudoku.esCeldaFija(i, j) ? new Color(30, 41, 59) : new Color(15, 23, 42);

                if (i == filaFocus && j == colFocus) {
                    // Celda seleccionada directamente por el usuario
                    bgColor = new Color(37, 99, 235); // Blue 600
                } else if (valorFocus != 0 && valorCelda == valorFocus) {
                    // Celdas que contienen exactamente el mismo número en el tablero
                    bgColor = new Color(30, 58, 138); // Blue 900
                } else if (i == filaFocus || j == colFocus || (i / 3 == bloqueFila && j / 3 == bloqueCol)) {
                    // Celdas pertenecientes a la misma fila, columna o cuadrante 3x3
                    bgColor = new Color(51, 65, 85); // Slate 700
                }

                celda.setBackground(bgColor);
            }
        }
    }

    /**
     * Sincroniza la interfaz gráfica con el estado actual del modelo de Sudoku.
     * Configura los colores de fondo y texto apropiados para celdas fijas (pistas)
     * y editables.
     */
    private void actualizarVistaTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField celda = celdas[i][j];
                int valor = sudoku.getValor(i, j);

                if (valor != 0) {
                    celda.setText(String.valueOf(valor));
                    if (sudoku.esCeldaFija(i, j)) {
                        celda.setEditable(false);
                        celda.setBackground(new Color(30, 41, 59)); // Fondo gris oscuro para pistas iniciales
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
        actualizarColoresErrores();
    }

    /**
     * Recorre el tablero evaluando la validez de los números introducidos por el
     * usuario.
     * Si un número viola las reglas del Sudoku (repetición en fila, columna o
     * cuadrante),
     * su color de texto cambia a rojo para alertar visualmente al jugador.
     */
    private void actualizarColoresErrores() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!sudoku.esCeldaFija(i, j)) {
                    int valor = sudoku.getValor(i, j);
                    if (valor != 0) {
                        if (sudoku.esMovimientoValido(i, j, valor)) {
                            celdas[i][j].setForeground(new Color(96, 165, 250)); // Azul claro (correcto)
                        } else {
                            celdas[i][j].setForeground(Color.RED); // Rojo (movimiento inválido)
                        }
                    } else {
                        celdas[i][j].setForeground(new Color(96, 165, 250));
                    }
                }
            }
        }
    }

    /**
     * Verifica si el tablero actual representa una victoria válida.
     * Realiza una sincronización explícita previa de todos los campos de texto con
     * el modelo
     * para asegurar que se capturen los últimos cambios. Gestiona la reproducción
     * del audio
     * de victoria, el cambio a la pantalla de felicitación y el sistema de
     * penalización de vidas
     * en el modo Hardcore.
     */
    private void verificarVictoria() {
        // Sincronizo explícitamente el modelo interno con el contenido actual de los
        // JTextField
        // antes de validar. Esto previene fallos de desincronización causados por
        // eventos
        // de teclado pendientes (keyReleased) o acciones rápidas como copiar/pegar.
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!sudoku.esCeldaFija(i, j)) {
                    String texto = celdas[i][j].getText().trim();
                    if (!texto.isEmpty()) {
                        try {
                            int valor = Integer.parseInt(texto);
                            sudoku.colocarNumero(i, j, valor);
                        } catch (NumberFormatException ex) {
                            sudoku.colocarNumero(i, j, 0);
                        }
                    } else {
                        sudoku.colocarNumero(i, j, 0);
                    }
                }
            }
        }
        actualizarColoresErrores();

        if (sudoku.estaResuelto()) {
            if (timer != null)
                timer.stop();
            comboDificultad.setEnabled(true); // Desbloqueamos el selector al ganar

            // Reproduzco el efecto de sonido de victoria en un hilo independiente (Thread)
            // para garantizar que la interfaz gráfica (hilo de Swing) no sufra bloqueos o
            // tirones.
            new Thread(() -> {
                try {
                    java.net.URL url = getClass().getResource("/audio/victoria.wav");
                    if (url != null) {
                        javax.sound.sampled.AudioInputStream audioIn = javax.sound.sampled.AudioSystem
                                .getAudioInputStream(url);
                        javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
                        clip.open(audioIn);
                        clip.start();
                    } else {
                        System.err.println("No se encontró el archivo de audio: /audio/victoria.wav");
                    }
                } catch (Exception ex) {
                    System.err.println("Error al reproducir el sonido: " + ex.getMessage());
                }
            }).start();

            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof VentanaPrincipal) {
                String difSeleccionada = (String) comboDificultad.getSelectedItem();
                ((VentanaPrincipal) window).mostrarVictoria(segundosTranscurridos, difSeleccionada);
            }
        } else {
            String difSeleccionada = (String) comboDificultad.getSelectedItem();
            if (difSeleccionada.equalsIgnoreCase("Hardcore")) {
                vidas--;
                if (vidas >= 0 && vidas < 3) {
                    // Al perder una vida, apago visualmente el corazón correspondiente a ese índice
                    iconosVidas[vidas].setIcon(iconoVidaVacia);
                }

                if (vidas <= 0) {
                    if (timer != null)
                        timer.stop();
                    comboDificultad.setEnabled(true); // Desbloqueamos el selector al finalizar la partida
                    JOptionPane.showMessageDialog(this,
                            "Has perdido tus 3 vidas. ¡GAME OVER!",
                            "Fin de la partida", JOptionPane.ERROR_MESSAGE);

                    // Bloqueo total del tablero tras perder
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            celdas[i][j].setEditable(false);
                        }
                    }

                    partidaIniciada = false;
                    btnGenerar.setText("Nueva Partida");
                    btnGenerar.setBackground(new Color(100, 150, 255)); // Azul original de botón
                    if (btnVolverMenu != null)
                        btnVolverMenu.setVisible(true);
                    if (btnVerificar != null)
                        btnVerificar.setEnabled(false);
                    return;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "¡Error en el tablero! Pierdes 1 vida. Te quedan: " + vidas,
                            "¡Cuidado!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Compruebo si el fallo se debe a que el tablero aún tiene celdas vacías por
            // rellenar
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
