package ec.edu.ups.Estructura.models;

import java.util.List;

/**
 * Contiene el resultado de la ejecución de un algoritmo de resolución de laberintos.
 * Encapsula tanto la lista de celdas visitadas como el camino de la solución.
 *
 * @author Cristian Moscoso
 */
public class SolveResult {
    /**
     * Lista de todas las celdas que fueron visitadas por el algoritmo durante la búsqueda.
     */
    public final List<Cell> visitadas;
    /**
     * Lista de celdas que forman el camino desde el inicio hasta el fin.
     * Si no se encontró solución, esta lista estará vacía.
     */
    public final List<Cell> camino;

    /**
     * Construye un nuevo objeto de resultado.
     *
     * @param paramList1 La lista de celdas visitadas.
     * @param paramList2 La lista de celdas que componen el camino final.
     */
    public SolveResult(List<Cell> paramList1, List<Cell> paramList2) {
        this.visitadas = paramList1;
        this.camino = paramList2;
    }
}
