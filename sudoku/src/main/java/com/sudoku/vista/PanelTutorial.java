package com.sudoku.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que contiene la guía didáctica y tutorial del juego SUDO.ku.
 * Muestra el objetivo principal, las reglas básicas del Sudoku y las
 * explicaciones
 * detalladas de cada modo de juego, incluyendo el funcionamiento de las vidas
 * en modo Hardcore.
 */
public class PanelTutorial extends JPanel {
    private VentanaPrincipal ventanaPadre;

    /**
     * Constructor del panel de tutorial.
     * Configura el contenedor principal y carga el texto explicativo estructurado
     * en secciones.
     *
     * @param ventanaPadre Referencia a la ventana principal para permitir volver al
     *                     menú.
     */
    public PanelTutorial(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42)); // Fondo oscuro del diseño general

        inicializarComponentes();
    }

    /**
     * Inicializa y organiza los componentes internos: título principal, contenedor
     * de texto
     * con scroll automático y el botón de regreso al menú.
     */
    private void inicializarComponentes() {
        // Título principal de la sección
        JLabel lblTitulo = new JLabel("Tutorial SUDO.ku", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(250, 204, 21)); // Dorado
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Contenido estructurado en un panel con BoxLayout vertical
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(15, 23, 42));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));

        // Sección: Objetivo del Juego
        contentPanel.add(createSectionTitle("Objetivo del Juego", 5));
        contentPanel.add(createTextArea(
                "Rellenar la cuadrícula de 9x9 con números del 1 al 9. Cada número debe aparecer exactamente una vez en cada fila, columna y subcuadrícula de 3x3."));

        // Sección: Reglas Básicas
        contentPanel.add(createSectionTitle("Reglas Básicas", 15));
        contentPanel.add(createTextArea("1. No repetir números en la misma fila.\n" +
                "2. No repetir números en la misma columna.\n" +
                "3. No repetir números en el mismo bloque 3x3."));

        // Sección: Modos de Juego disponibles
        contentPanel.add(createSectionTitle("Modos de Juego", 15));
        contentPanel.add(createTextArea("• Fácil: Muchas pistas iniciales para aprender.\n" +
                "• Medio: Un desafío equilibrado.\n" +
                "• Difícil: Solo para expertos.\n" +
                "• Hardcore: ¡Solo tienes 3 vidas!"));

        // Ejemplo visual del modo Hardcore mostrando los iconos de vidas
        JPanel hardcorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        hardcorePanel.setBackground(new Color(15, 23, 42));
        hardcorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblHardcore = new JLabel("Vidas en Hardcore: ");
        lblHardcore.setForeground(new Color(226, 232, 240));
        lblHardcore.setFont(new Font("SansSerif", Font.PLAIN, 16));
        hardcorePanel.add(lblHardcore);

        ImageIcon vidaIcon = escalarIcono("/images/Vida.png", 25, 25);
        if (vidaIcon != null) {
            hardcorePanel.add(new JLabel(vidaIcon));
            hardcorePanel.add(new JLabel(vidaIcon));
            hardcorePanel.add(new JLabel(vidaIcon));
        }
        contentPanel.add(hardcorePanel);
        contentPanel.add(createTextArea(
                "Cada error que cometas restará una vida. Si llegas a 0 vidas, la partida terminará automáticamente."));

        // Envolver el contenido en un JScrollPane transparente para evitar cortes en
        // pantallas pequeñas
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(15, 23, 42));
        scrollPane.getViewport().setBackground(new Color(15, 23, 42));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con el botón para regresar al menú principal
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(15, 23, 42));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setPreferredSize(new Dimension(200, 45));
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnVolver.setBackground(new Color(100, 150, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.putClientProperty("FlatLaf.style", "arc: 15");
        btnVolver.addActionListener(e -> ventanaPadre.mostrarMenu());

        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea un subtítulo de sección estandarizado y alineado a la izquierda.
     *
     * @param text      Texto del subtítulo.
     * @param topMargin Margen superior en píxeles para separar de la sección
     *                  anterior.
     * @return JLabel configurado con el estilo de subtítulo.
     */
    private JLabel createSectionTitle(String text, int topMargin) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 22));
        label.setForeground(new Color(56, 189, 248)); // Azul claro
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(topMargin, 0, 5, 0));
        return label;
    }

    /**
     * Crea un área de texto de múltiples líneas, no editable y alineada a la
     * izquierda.
     * Implementa un cálculo de altura dinámico sobrescribiendo getPreferredSize
     * para asegurar
     * el correcto ajuste de línea dentro del BoxLayout.
     *
     * @param text Párrafo explicativo a mostrar.
     * @return JTextArea configurado con ajuste automático de líneas.
     */
    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text) {
            @Override
            public Dimension getPreferredSize() {
                // Forzar el cálculo de altura basándose en el ancho disponible aproximado
                // La ventana mide 600px, menos los márgenes del panel (40px a cada lado)
                setSize(500, Integer.MAX_VALUE);
                return super.getPreferredSize();
            }
        };
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textArea.setForeground(new Color(226, 232, 240));
        textArea.setBackground(new Color(15, 23, 42));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        textArea.setFocusable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return textArea;
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
                Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
