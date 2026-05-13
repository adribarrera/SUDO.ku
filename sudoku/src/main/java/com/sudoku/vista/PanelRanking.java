package com.sudoku.vista;

import com.sudoku.modelo.GestorPuntuaciones;
import com.sudoku.modelo.RegistroPuntuacion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelRanking extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private JLabel lblTitulo;
    private DefaultTableModel modeloTabla;
    private ImageIcon oroIcon;
    private ImageIcon plataIcon;
    private ImageIcon bronceIcon;

    public PanelRanking(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        cargarIconos();
        configurarPanel();
        inicializarComponentes();
    }

    private void cargarIconos() {
        oroIcon = escalarIcono("/images/gold-medal.png", 24, 24);
        plataIcon = escalarIcono("/images/silver-medal.png", 24, 24);
        bronceIcon = escalarIcono("/images/bronze-medal.png", 24, 24);
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
            return new ImageIcon();
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42));
    }

    private void inicializarComponentes() {
        // Título superior
        lblTitulo = new JLabel("Ranking", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(226, 232, 240));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla de puntuaciones
        String[] columnas = { "Pos", "Nombre", "Tiempo", "Fecha" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaRanking = new JTable(modeloTabla);
        tablaRanking.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tablaRanking.setRowHeight(40);
        tablaRanking.setBackground(new Color(30, 41, 59));
        tablaRanking.setForeground(new Color(226, 232, 240));
        tablaRanking.setSelectionBackground(new Color(71, 85, 105));
        tablaRanking.setSelectionForeground(Color.WHITE);

        // Desactivamos la cuadrícula por defecto para usar nuestros bordes azules
        tablaRanking.setShowGrid(false);
        tablaRanking.setIntercellSpacing(new Dimension(0, 0));

        tablaRanking.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tablaRanking.getTableHeader().setBackground(new Color(15, 23, 42));
        tablaRanking.getTableHeader().setForeground(new Color(96, 165, 250)); // Encabezados en azul
        tablaRanking.getTableHeader().setReorderingAllowed(false);

        Border bordeAzul = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(59, 130, 246)); // Azul

        // Renderizador para las columnas (centrado general con borde azul)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(bordeAzul);
                return label;
            }
        };

        // Renderizador especial para la columna Posición (Medallas)
        DefaultTableCellRenderer positionRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setIcon(null); // Resetear icono por defecto
                label.setBorder(bordeAzul);

                if (value != null) {
                    String texto = value.toString();
                    if (texto.equals("1º") && oroIcon != null && oroIcon.getImage() != null) {
                        label.setIcon(oroIcon);
                        label.setText(""); // Ocultar el texto, dejar solo el icono
                    } else if (texto.equals("2º") && plataIcon != null && plataIcon.getImage() != null) {
                        label.setIcon(plataIcon);
                        label.setText("");
                    } else if (texto.equals("3º") && bronceIcon != null && bronceIcon.getImage() != null) {
                        label.setIcon(bronceIcon);
                        label.setText("");
                    }
                }

                return label;
            }
        };

        tablaRanking.getColumnModel().getColumn(0).setCellRenderer(positionRenderer);
        tablaRanking.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaRanking.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaRanking.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Ajustar anchos
        tablaRanking.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaRanking.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaRanking.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaRanking.getColumnModel().getColumn(3).setPreferredWidth(140);

        JScrollPane scrollPane = new JScrollPane(tablaRanking);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        scrollPane.getViewport().setBackground(new Color(15, 23, 42));
        add(scrollPane, BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Volver al Menú");
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBackground(new Color(100, 150, 255));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setPreferredSize(new Dimension(200, 45));
        btnCerrar.putClientProperty("FlatLaf.style", "arc: 15");
        btnCerrar.addActionListener(e -> ventanaPadre.mostrarMenu());

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(15, 23, 42));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        panelInferior.add(btnCerrar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public void cargarRanking(String dificultad) {
        lblTitulo.setText("Ranking: " + dificultad);
        modeloTabla.setRowCount(0); // Limpiar tabla

        List<RegistroPuntuacion> topPuntuaciones = GestorPuntuaciones.obtenerTopPuntuaciones(dificultad);
        int posicion = 1;
        for (RegistroPuntuacion registro : topPuntuaciones) {
            modeloTabla.addRow(new Object[] {
                    posicion + "º",
                    registro.getNombre(),
                    registro.getTiempoFormateado(),
                    registro.getFechaFormateada()
            });
            posicion++;
        }

        if (topPuntuaciones.isEmpty()) {
            modeloTabla.addRow(new Object[] { "-", "Sin registros", "-", "-" });
        }
    }
}
