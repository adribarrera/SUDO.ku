package com.sudoku.vista;

import com.sudoku.modelo.GestorPuntuaciones;
import com.sudoku.modelo.RegistroPuntuacion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelRanking extends JDialog {

    private String dificultad;

    public PanelRanking(JFrame parent, String dificultad) {
        super(parent, "Mejores Tiempos - " + dificultad, true);
        this.dificultad = dificultad;

        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setResizable(false);
    }

    private void inicializarComponentes() {
        // Título superior
        JLabel lblTitulo = new JLabel("Ranking: " + dificultad, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla de puntuaciones
        String[] columnas = {"Posición", "Nombre", "Tiempo"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaRanking = new JTable(modeloTabla);
        tablaRanking.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaRanking.setRowHeight(25);
        tablaRanking.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaRanking.getTableHeader().setReorderingAllowed(false);
        
        // Centrar el contenido de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<3; i++){
            tablaRanking.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Cargar datos
        List<RegistroPuntuacion> topPuntuaciones = GestorPuntuaciones.obtenerTopPuntuaciones(dificultad);
        int posicion = 1;
        for (RegistroPuntuacion registro : topPuntuaciones) {
            modeloTabla.addRow(new Object[]{
                    posicion + "º",
                    registro.getNombre(),
                    registro.getTiempoFormateado()
            });
            posicion++;
        }

        if (topPuntuaciones.isEmpty()) {
            modeloTabla.addRow(new Object[]{"-", "Sin registros", "-"});
        }

        JScrollPane scrollPane = new JScrollPane(tablaRanking);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrar.addActionListener(e -> dispose());
        
        JPanel panelInferior = new JPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelInferior.add(btnCerrar);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
}
