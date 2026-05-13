package com.sudoku.modelo;
import java.util.Random;

public class GeneradorSudoku {
   private int [][] tablero;
   private Random random;
   
   public GeneradorSudoku() {
    this.tablero = new int [9][9];
    this.random = new Random();
   }

   private int[][] tableroResuelto;

   public void generar(int [][] tableroObjetivo, int celdasVacias) {
    // Lleno primero los tres bloques diagonales
    llenarDiagonal();

    //Lleno el resto de celdas usando backtracking
    resolverSudoku(this.tablero);

    // Guardo una copia del tablero resuelto antes de quitar celdas
    this.tableroResuelto = new int[9][9];
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            this.tableroResuelto[i][j] = this.tablero[i][j];
        }
    }

    //Elimino aleatoriamente la cantidad de celdas necesarias para crear los agujeros
    quitarCeldas(celdasVacias);

    //Copio el array destino
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            tableroObjetivo[i][j] = this.tablero[i][j];
        }
    }
   }
   
   private void llenarDiagonal() {
        for(int i = 0;i < 9; i = i + 3) {
        llenarBloque(i,i);
        }
   }


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

    public boolean seguroEnBloque(int filaInicio, int colInicio, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[filaInicio + i] [colInicio + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean resolverSudoku(int [][] grid) {
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(grid[i][j] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (esMovimientoValido(grid, i, j, num)) {
                            grid[i][j] = num;
                            if (resolverSudoku(grid)) {
                                return true;
                            } else {
                                grid[i][j] = 0; // Deshago (backtracking)
                            }
                        }
                    }
                    return false; //Ningún número del 1 al 9 es válido aquí.
                }
            }
        }
        return true; //Si no hay celdas con 0, el sudoku está resuelto
    }

    public boolean esMovimientoValido(int[][] grid, int fila, int col, int num) {
        // Verifico fila y columna
        for (int x = 0; x < 9; x++) {
            if (grid[x][col] == num || grid [fila][x] == num) {
                return false;
            }
        }

        // Verifico bloque de 3x3
        int filaInicio = fila - fila % 3;
        int colInicio = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i + filaInicio][j + colInicio] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private void quitarCeldas(int cantidadVacias) {
        int contador = cantidadVacias;
        while (contador > 0) {
            int casilla = random.nextInt(81);
            int i = casilla/9;
            int j = casilla%9;
            if (tablero[i][j] != 0) {
                tablero[i][j] = 0;
                contador = contador - 1;
            }
        }
    }

    public int[][] getTableroResuelto() {
        return this.tableroResuelto;
    }
}
