package com.sudoku.modelo;

import java.util.Random;

/**
 * Clase encargada de generar tableros de Sudoku válidos y resolubles.
 * Utiliza un enfoque híbrido: primero llena los tres bloques diagonales
 * independientes
 * de 3x3 de forma aleatoria y luego completa el resto del tablero mediante
 * backtracking.
 * Finalmente, elimina casillas al azar para ajustarse a la dificultad
 * solicitada.
 */
public class GeneradorSudoku {
    private int[][] tablero;
    private Random random;
    private int[][] tableroResuelto;

    /**
     * Constructor del generador de Sudoku.
     * Inicializa la matriz de trabajo interna y el generador de números
     * pseudoaleatorios.
     */
    public GeneradorSudoku() {
        this.tablero = new int[9][9];
        this.random = new Random();
    }

    /**
     * Genera un tablero de Sudoku completo, guarda su solución y luego elimina
     * la cantidad de celdas especificada para crear el desafío.
     *
     * @param tableroObjetivo Matriz de destino donde se volcará el tablero con
     *                        huecos.
     * @param celdasVacias    Cantidad de casillas que deben quedar en blanco (0).
     */
    public void generar(int[][] tableroObjetivo, int celdasVacias) {
        // Lleno primero los tres bloques diagonales principales (que son independientes
        // entre sí)
        llenarDiagonal();

        // Lleno el resto de celdas del tablero usando el algoritmo de backtracking
        resolverSudoku(this.tablero);

        // Guardo una copia exacta del tablero completamente resuelto antes de empezar a
        // quitar celdas
        this.tableroResuelto = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.tableroResuelto[i][j] = this.tablero[i][j];
            }
        }

        // Elimino aleatoriamente la cantidad de celdas solicitada para crear los
        // agujeros del puzzle
        quitarCeldas(celdasVacias);

        // Copio el resultado final al array destino proporcionado por el modelo
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tableroObjetivo[i][j] = this.tablero[i][j];
            }
        }
    }

    /**
     * Llena los tres bloques de 3x3 ubicados en la diagonal principal del tablero
     * (arriba-izquierda, centro, abajo-derecha).
     */
    private void llenarDiagonal() {
        for (int i = 0; i < 9; i = i + 3) {
            llenarBloque(i, i);
        }
    }

    /**
     * Llena de forma aleatoria un bloque específico de 3x3 asegurando que no se
     * repitan números.
     *
     * @param filaInicio Fila donde comienza el bloque (0, 3 o 6).
     * @param colInicio  Columna donde comienza el bloque (0, 3 o 6).
     */
    private void llenarBloque(int filaInicio, int colInicio) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = random.nextInt(9) + 1;
                } while (!seguroEnBloque(filaInicio, colInicio, num));
                tablero[filaInicio + i][colInicio + j] = num;
            }
        }
    }

    /**
     * Verifica si un número puede colocarse en un bloque de 3x3 sin duplicarse.
     *
     * @param filaInicio Fila inicial del bloque.
     * @param colInicio  Columna inicial del bloque.
     * @param num        Número a verificar (1-9).
     * @return true si el número no existe en el bloque, false si ya está presente.
     */
    public boolean seguroEnBloque(int filaInicio, int colInicio, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[filaInicio + i][colInicio + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Algoritmo recursivo de backtracking para resolver o completar el tablero de
     * Sudoku.
     * Prueba secuencialmente números del 1 al 9 en cada celda vacía y retrocede si
     * no encuentra solución.
     *
     * @param grid Matriz de 9x9 a resolver.
     * @return true si el tablero se resuelve por completo, false si no tiene
     *         solución.
     */
    public boolean resolverSudoku(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (esMovimientoValido(grid, i, j, num)) {
                            grid[i][j] = num;
                            if (resolverSudoku(grid)) {
                                return true;
                            } else {
                                grid[i][j] = 0; // Deshago el cambio (backtracking) al no encontrar solución
                            }
                        }
                    }
                    return false; // Ningún número del 1 al 9 es válido en esta celda
                }
            }
        }
        return true; // Si no quedan celdas con valor 0, el Sudoku está completamente resuelto
    }

    /**
     * Comprueba si un número cumple las reglas de fila, columna y cuadrante en una
     * matriz de trabajo.
     *
     * @param grid Matriz de 9x9 actual.
     * @param fila Fila objetivo (0-8).
     * @param col  Columna objetivo (0-8).
     * @param num  Número a evaluar (1-9).
     * @return true si el número es válido en esa posición, false en caso contrario.
     */
    public boolean esMovimientoValido(int[][] grid, int fila, int col, int num) {
        // Verifico que el número no esté en la misma fila ni en la misma columna
        for (int x = 0; x < 9; x++) {
            if (grid[x][col] == num || grid[fila][x] == num) {
                return false;
            }
        }

        // Calculo las coordenadas de inicio del bloque de 3x3 correspondiente
        int filaInicio = fila - fila % 3;
        int colInicio = col - col % 3;

        // Verifico que el número no esté en el bloque de 3x3
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i + filaInicio][j + colInicio] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Vacía un número específico de casillas al azar en el tablero generado.
     *
     * @param cantidadVacias Número total de casillas que deben quedar en blanco.
     */
    private void quitarCeldas(int cantidadVacias) {
        int contador = cantidadVacias;
        while (contador > 0) {
            int casilla = random.nextInt(81);
            int i = casilla / 9;
            int j = casilla % 9;
            if (tablero[i][j] != 0) {
                tablero[i][j] = 0;
                contador = contador - 1;
            }
        }
    }

    /**
     * Devuelve la copia del tablero resuelto (solución de referencia).
     *
     * @return Matriz de 9x9 con la solución correcta.
     */
    public int[][] getTableroResuelto() {
        return this.tableroResuelto;
    }
}
