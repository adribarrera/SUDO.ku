package com.sudoku.modelo;

/**
 * Modelo principal que encapsula el estado y la lógica de negocio del juego de
 * Sudoku.
 * Gestiona la matriz activa del tablero, la solución de referencia y el mapa de
 * celdas
 * fijas (pistas iniciales). Proporciona métodos para verificar la validez de
 * movimientos
 * y comprobar si el tablero ha sido resuelto correctamente.
 */
public class Sudoku {

    private int[][] tablero;
    private int[][] tableroResuelto;
    private boolean[][] celdasFijas;

    /**
     * Constructor por defecto del modelo Sudoku.
     * Inicializa las estructuras internas de matrices de 9x9 para el tablero
     * activo,
     * el tablero resuelto y el mapa de pistas fijas.
     */
    public Sudoku() {
        this.tablero = new int[9][9];
        this.tableroResuelto = new int[9][9];
        this.celdasFijas = new boolean[9][9];
    }

    /**
     * Genera un nuevo tablero de Sudoku adaptando la cantidad de celdas vacías
     * según
     * la dificultad solicitada por el usuario.
     *
     * @param dificultad Nivel de dificultad (prueba, facil, medio, dificil,
     *                   hardcore).
     */
    public void generarTablero(String dificultad) {
        // Inicializo los tableros activos y de pistas
        this.tablero = new int[9][9];
        this.celdasFijas = new boolean[9][9];

        int vacias = 40; // Valor de celdas vacías por defecto
        switch (dificultad.toLowerCase()) {
            case "prueba":
                vacias = 1; // Un solo hueco para agilizar pruebas de victoria
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
        this.tableroResuelto = generador.getTableroResuelto();

        // Establezco las celdas fijas según el tablero generado
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.tablero[i][j] != 0) {
                    this.celdasFijas[i][j] = true;
                }
            }
        }
    }

    /**
     * Evalúa si un número puede colocarse en una posición específica sin violar las
     * reglas del Sudoku.
     * Comprueba que el número no esté repetido en la misma fila, columna ni
     * subcuadrícula de 3x3.
     *
     * @param fila    Fila objetivo (0-8).
     * @param columna Columna objetivo (0-8).
     * @param valor   Número a evaluar (1-9).
     * @return true si el movimiento cumple todas las reglas, false en caso
     *         contrario.
     */
    public boolean esMovimientoValido(int fila, int columna, int valor) {
        if (valor < 1 || valor > 9) {
            return false;
        }

        // Compruebo la fila buscando posibles duplicados
        for (int j = 0; j < 9; j++) {
            if (tablero[fila][j] == valor && j != columna) {
                return false;
            }
        }

        // Compruebo la columna buscando posibles duplicados
        for (int i = 0; i < 9; i++) {
            if (tablero[i][columna] == valor && i != fila) {
                return false;
            }
        }

        // Compruebo la cuadrícula de 3x3 correspondiente a la celda
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

    /**
     * Inserta un valor en el tablero activo si la celda no es una pista fija.
     *
     * @param fila    Fila objetivo (0-8).
     * @param columna Columna objetivo (0-8).
     * @param valor   Número a colocar (0 para vaciar, 1-9 para asignar).
     * @return true si la colocación es válida o vacía, false si la celda es fija o
     *         está fuera de límites.
     */
    public boolean colocarNumero(int fila, int columna, int valor) {
        if ((fila >= 0 && fila < 9) && (columna >= 0 && columna < 9)) {
            if (!celdasFijas[fila][columna]) {
                boolean valido = (valor == 0) || esMovimientoValido(fila, columna, valor);
                tablero[fila][columna] = valor;
                return valido;
            }
        }
        return false;
    }

    /**
     * Verifica de forma estricta si el tablero de Sudoku está completamente lleno y
     * resuelto
     * cumpliendo todas y cada una de las reglas matemáticas.
     *
     * @return true si el tablero está lleno y sin errores, false en caso contrario.
     */
    public boolean estaResuelto() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Si hay alguna celda vacía, el tablero no está completo
                if (tablero[i][j] == 0) {
                    return false;
                }

                // Compruebo de forma estricta si la solución actual en esta celda es válida
                int valorCelda = tablero[i][j];
                tablero[i][j] = 0; // Extraigo temporalmente la celda para verificar
                if (!esMovimientoValido(i, j, valorCelda)) {
                    tablero[i][j] = valorCelda; // Restauro el valor
                    return false;
                }
                tablero[i][j] = valorCelda; // Restauro el valor
            }
        }
        return true;
    }

    /**
     * Imprime por consola una representación en formato texto del tablero actual.
     * Utilizado principalmente para depuración
     */
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

    /**
     * Obtiene el valor actual contenido en una celda del tablero activo.
     *
     * @param fila    Fila objetivo (0-8).
     * @param columna Columna objetivo (0-8).
     * @return Valor entero contenido en la celda (0 si está vacía, 1-9 si tiene un
     *         número).
     */
    public int getValor(int fila, int columna) {
        return tablero[fila][columna];
    }

    /**
     * Obtiene el valor correcto de la solución de referencia para una celda
     * específica.
     *
     * @param fila    Fila objetivo (0-8).
     * @param columna Columna objetivo (0-8).
     * @return Valor resuelto de la celda (1-9).
     */
    public int getValorResuelto(int fila, int columna) {
        return tableroResuelto[fila][columna];
    }

    /**
     * Indica si una celda específica es una pista inicial fija.
     *
     * @param fila    Fila objetivo (0-8).
     * @param columna Columna objetivo (0-8).
     * @return true si la celda es fija (no modificable), false si es editable.
     */
    public boolean esCeldaFija(int fila, int columna) {
        return celdasFijas[fila][columna];
    }
}