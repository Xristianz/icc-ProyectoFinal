package ec.edu.ups.Estructura.solver;

import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.SolveResult;

/**
 * Define el contrato que debe seguir cualquier clase que resuelva laberintos.
 * Esto permite tratar a todos los algoritmos de resolución de la misma manera.
 *
 * @author Cristian Moscoso
 */
public interface MazeSolver {
    /**
     * Busca un camino en el laberinto desde una celda de inicio hasta una de fin.
     *
     * @param maze      La matriz 2D que representa el laberinto.
     * @param startCell La celda de inicio.
     * @param endCell   La celda de destino.
     * @return Un objeto {@link SolveResult} que contiene el camino encontrado y las celdas visitadas.
     */
    public SolveResult getPath(Cell[][] maze, Cell startCell, Cell endCell);
}