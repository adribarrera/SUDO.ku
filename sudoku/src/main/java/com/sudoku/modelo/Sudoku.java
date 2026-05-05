package com.sudoku.modelo;

public class Sudoku {

    private int[][] tablero;
    private boolean[][] celdasFijas;

    public Sudoku() {
        this.tablero = new int[9][9];
        this.celdasFijas = new boolean[9][9];
    }

    public void generarTablero(String dificultad) {
        // Inicializo los tableros
        this.tablero = new int[9][9];
        this.celdasFijas = new boolean[9][9];

        int vacias = 40; // Por defecto
        switch (dificultad.toLowerCase()) {
            case "prueba":
                vacias = 1;
                break;
            case "facil":
                vacias = 30;
                break;
            case "medio":
                vacias = 45;
                break;
            case "dificil":
                vacias = 60;
                break;
            case "hardcore":
                vacias = 65;
                break;
            default:
                vacias = 45;
        }

        GeneradorSudoku generador = new GeneradorSudoku();
        generador.generar(this.tablero, vacias);

        // Establecer las celdas fijas según el tablero generado
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.tablero[i][j] != 0) {
                    this.celdasFijas[i][j] = true;
                }
            }
        }
    }

    public boolean esMovimientoValido(int fila, int columna, int valor) {
        if (valor < 1 || valor > 9) {
            return false;
        }

        for (int j = 0; j < 9; j++) {
            if(tablero[fila][j] == valor && j != columna) {
                return false;
            }
        }

        // Compruebo la columna
        for (int i = 0; i < 9; i++) {
            if (tablero[i][columna] == valor && i != fila) {
                return false;
            }
        }

        // Compruebo la cuadrícula
        int inicioFila = (fila / 3) * 3;
        int inicioCol = (columna / 3) * 3;

        for (int i = inicioFila; i < inicioFila + 3; i++) {
            for (int j = inicioCol; j < inicioCol + 3; j++) {
                if (tablero[i][j] == valor && (i != fila || j != columna)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public boolean colocarNumero(int fila, int columna, int valor) {
        if ((fila >= 0 && fila < 9) && (columna >= 0 && columna < 9)) {
            if (!celdasFijas[fila][columna]) {
                if (valor == 0 || esMovimientoValido(fila, columna, valor)) {
                    tablero[fila][columna] = valor;
                    return true;
                }
            }
        }
        return false; 
    }

    public boolean estaResuelto() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Si hay alguna celda vacía, el tablero no está completo
                if (tablero[i][j] == 0) {
                    return false;
                }

                // Comprobar de forma estricta si la solución actual es válida
                int valorCelda = tablero[i][j];
                tablero[i][j] = 0; // Sacamos temporalmente para verificar
                if (!esMovimientoValido(i, j, valorCelda)) {
                    tablero[i][j] = valorCelda; // Restaurar
                    return false;
                }
                tablero[i][j] = valorCelda; // Restaurar
            }
        }
        return true;
    }

    public void mostrarTablero() {
        System.out.println("-------------------------");
        for (int i = 0; i < 9; i++) {
            System.out.print("| ");
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print(tablero[i][j] + " ");
                }

                if ((j + 1) % 3 == 0) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((i + 1) % 3 == 0) {
                System.out.println("-------------------------");
            }
        }
    }


    public int getValor(int fila, int columna) {
        return tablero[fila][columna];
    }

    public boolean esCeldaFija(int fila, int columna) {
        return celdasFijas[fila][columna];
    }
}