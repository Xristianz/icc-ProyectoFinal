package ec.edu.ups.Estructura.models;

/**
 * Representa una celda individual en la cuadrícula del laberinto.
 * Cada celda se define por sus coordenadas de fila y columna.
 *
 * @author Cristian Moscoso
 */
public class Cell {
    /**
     * La coordenada de la fila (posición vertical).
     */
    public int row;

    /**
     * La coordenada de la columna (posición horizontal).
     */
    public int col;

    /**
     * El estado actual de la celda (ej. vacía, muro, etc.).
     */
    public CellState state;

    /**
     * Construye una nueva celda con sus coordenadas.
     * Por defecto, se inicializa con el estado EMPTY.
     *
     * @param paramInt1 La fila de la celda.
     * @param paramInt2 La columna de la celda.
     */
    public Cell(int paramInt1, int paramInt2) {
        this.row = paramInt1;
        this.col = paramInt2;
        this.state = CellState.EMPTY;
    }

    /**
     * Compara si esta celda es igual a otro objeto.
     * Dos celdas se consideran iguales si tienen la misma fila y columna.
     *
     * @param paramObject El objeto con el que se va a comparar.
     * @return {@code true} si las celdas son iguales, {@code false} en caso contrario.
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if (!(paramObject instanceof Cell))
            return false;
        Cell cell = (Cell)paramObject;
        return (this.row == cell.row && this.col == cell.col);
    }

    /**
     * Genera un código hash único para la celda basado en sus coordenadas.
     * Esencial para un rendimiento óptimo en colecciones como HashSet.
     *
     * @return El código hash de la celda.
     */
    public int hashCode() {
        return 31 * this.row + this.col;
    }
}
