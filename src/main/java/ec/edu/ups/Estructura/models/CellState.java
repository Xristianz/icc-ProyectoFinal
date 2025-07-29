package ec.edu.ups.Estructura.models;

/**
 * Representa los posibles estados de una celda dentro del laberinto.
 *
 * @author Cristian Moscoso
 */
public enum CellState {
    /**
     * La celda está vacía y es transitable.
     */
    EMPTY,
    /**
     * La celda es un muro o un obstáculo, no es transitable.
     */
    WALL,
    /**
     * La celda es el punto de inicio del laberinto.
     */
    START,
    /**
     * La celda es el punto final o de destino del laberinto.
     */
    END,
    /**
     * La celda forma parte del camino de la solución encontrada.
     */
    PATH;
}
