package com.sudoku.vista;

import com.sudoku.modelo.GestorPuntuaciones;
import com.sudoku.modelo.RegistroPuntuacion;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que se muestra al usuario tras completar con éxito un Sudoku.
 * Felicita al jugador, muestra el tiempo total invertido y proporciona un
 * formulario
 * para introducir su nombre y registrar la puntuación en la base de datos.
 */
public class PanelRegistroVictoria extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private int segundosTranscurridos;
    private String dificultad;

    private JLabel lblTiempo;
    private JTextField txtNombre;
    private JLabel lblError;

    /**
     * Constructor del panel de registro de victoria.
     * Configura el contenedor principal e inicializa los elementos visuales y de
     * formulario.
     *
     * @param ventanaPadre Referencia a la ventana principal para gestionar la
     *                     navegación.
     */
    public PanelRegistroVictoria(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        configurarPanel();
        inicializarComponentes();
    }

    /**
     * Configura el diseño vertical (BoxLayout) y el color de fondo del panel.
     */
    private void configurarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(15, 23, 42)); // Mismo fondo oscuro de la aplicación
    }

    /**
     * Inicializa y organiza los componentes internos: mensajes de felicitación,
     * etiqueta del tiempo de resolución, campo de texto para el nombre del jugador
     * y botones de acción (enviar o cancelar).
     */
    private void inicializarComponentes() {
        add(Box.createVerticalStrut(100));

        // Título principal de felicitación
        JLabel lblTitulo = new JLabel("¡Enhorabuena!");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTitulo.setForeground(new Color(250, 204, 21)); // Amarillo dorado
        add(lblTitulo);

        add(Box.createVerticalStrut(20));

        JLabel lblSubtitulo = new JLabel("Has resuelto el Sudoku correctamente.");
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblSubtitulo.setForeground(new Color(226, 232, 240));
        add(lblSubtitulo);

        add(Box.createVerticalStrut(10));

        // Etiqueta de tiempo (se actualiza dinámicamente al cargar la vista)
        lblTiempo = new JLabel("Tiempo: 00:00");
        lblTiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTiempo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTiempo.setForeground(new Color(56, 189, 248)); // Azul claro
        add(lblTiempo);

        add(Box.createVerticalStrut(60));

        // Etiqueta del formulario para el ingreso del nombre
        JLabel lblNombre = new JLabel("Introduce tu nombre para el ranking:");
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblNombre.setForeground(new Color(226, 232, 240));
        add(lblNombre);

        add(Box.createVerticalStrut(10));

        // Campo de texto personalizado para introducir el nombre
        txtNombre = new JTextField();
        txtNombre.setMaximumSize(new Dimension(300, 40));
        txtNombre.setPreferredSize(new Dimension(300, 40));
        txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtNombre.setBackground(new Color(30, 41, 59)); // Fondo de celda oscuro
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(txtNombre);

        add(Box.createVerticalStrut(5));

        // Etiqueta de error para validación del formulario (inicialmente vacía)
        lblError = new JLabel(" ");
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblError.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblError.setForeground(Color.WHITE); // Texto en blanco según petición previa
        add(lblError);

        add(Box.createVerticalStrut(40));

        // Panel contenedor para los botones de acción
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setBackground(new Color(15, 23, 42));
        panelBotones.setMaximumSize(new Dimension(500, 60));

        // Botón Cancelar: Descarta el registro y vuelve al menú principal
        JButton btnCancelar = createButton("Cancelar", new Color(220, 38, 38)); // Rojo
        btnCancelar.addActionListener(e -> ventanaPadre.mostrarMenu());

        // Botón Enviar: Valida y registra la puntuación en la base de datos
        JButton btnEnviar = createButton("Enviar", new Color(34, 197, 94)); // Verde
        btnEnviar.addActionListener(e -> enviarPuntuacion());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnEnviar);

        add(panelBotones);
        add(Box.createVerticalGlue());
    }

    /**
     * Crea y configura un botón estandarizado para el formulario con esquinas
     * redondeadas.
     *
     * @param text    Texto que mostrará el botón.
     * @param bgColor Color de fondo del botón.
     * @return JButton configurado con el estilo del proyecto.
     */
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

    /**
     * Procesa el envío del formulario. Valida que el nombre no esté vacío,
     * crea el registro de puntuación, lo guarda en la base de datos y redirige a la
     * pantalla de ranking.
     */
    private void enviarPuntuacion() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            lblError.setText("¡Debes introducir un nombre válido!");
            txtNombre.requestFocus();
        } else {
            lblError.setText(" "); // Limpiamos el mensaje de error
            RegistroPuntuacion registro = new RegistroPuntuacion(nombre, dificultad, segundosTranscurridos);
            GestorPuntuaciones.guardarPuntuacion(registro);
            ventanaPadre.mostrarRanking(dificultad);
        }
    }

    /**
     * Recibe y configura los datos de la partida recién ganada antes de mostrar el
     * panel.
     * Formatea el tiempo en minutos y segundos y resetea los campos del formulario.
     *
     * @param segundos   Tiempo total transcurrido en segundos.
     * @param dificultad Dificultad superada en la partida.
     */
    public void setDatosVictoria(int segundos, String dificultad) {
        this.segundosTranscurridos = segundos;
        this.dificultad = dificultad;

        int minutos = segundos / 60;
        int segs = segundos % 60;
        lblTiempo.setText(String.format("Tiempo: %02d:%02d", minutos, segs));

        // Limpiamos el campo y el mensaje de error para una nueva partida
        txtNombre.setText("");
        lblError.setText(" ");
    }
}
