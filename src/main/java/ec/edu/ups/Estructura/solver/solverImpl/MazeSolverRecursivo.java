package ec.edu.ups.Estructura.solver.solverImpl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ec.edu.ups.Estructura.models.CellState;
import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.SolveResult;
import ec.edu.ups.Estructura.solver.MazeSolver;

/**
 * Implementación de {@link MazeSolver} que utiliza un algoritmo recursivo simple
 * para encontrar un camino. Este algoritmo solo intenta moverse hacia abajo y
 * hacia la derecha.
 *
 * @author Cristian Moscoso
 */
public class MazeSolverRecursivo implements MazeSolver {
    /**
     * Almacena el conjunto de celdas que han sido visitadas para no procesarlas de nuevo.
     * Se usa LinkedHashSet para mantener el orden de inserción.
     */
    private Set<Cell> visitadas = new LinkedHashSet<>();

    /**
     * Almacena la lista de celdas que forman el camino de la solución.
     */
    private List<Cell> camino = new ArrayList<>();

    /**
     * {@inheritDoc}
     * Inicia el proceso de búsqueda recursiva. Limpia los estados anteriores
     * y llama al método recursivo privado.
     */
    public SolveResult getPath(Cell[][] paramArrayOfCell, Cell paramCell1, Cell paramCell2) {
        this.visitadas.clear();
        this.camino.clear();
        findPath(paramArrayOfCell, paramCell1.row, paramCell1.col, paramCell2);
        // El camino se construye en orden inverso, así que no es necesario revertirlo
        // si se quisiera el orden de construcción. Para el camino real, se necesitaría revertir.
        return new SolveResult(new ArrayList<>(this.visitadas), new ArrayList<>(this.camino));
    }

    /**
     * Método recursivo principal que busca el camino.
     *
     * @param paramArrayOfCell El laberinto.
     * @param paramInt1        La fila actual.
     * @param paramInt2        La columna actual.
     * @param paramCell        La celda de destino.
     * @return {@code true} si se encontró un camino desde la celda actual, {@code false} de lo contrario.
     */
    private boolean findPath(Cell[][] paramArrayOfCell, int paramInt1, int paramInt2, Cell paramCell) {
        if (!isValid(paramArrayOfCell, paramInt1, paramInt2))
            return false;
        Cell cell = paramArrayOfCell[paramInt1][paramInt2];
        if (this.visitadas.contains(cell))
            return false;

        this.visitadas.add(cell);

        if (cell.equals(paramCell)) {
            this.camino.add(cell);
            return true;
        }

        // Intenta moverse hacia abajo o hacia la derecha.
        if (findPath(paramArrayOfCell, paramInt1 + 1, paramInt2, paramCell) || findPath(paramArrayOfCell, paramInt1, paramInt2 + 1, paramCell)) {
            // Si alguna de las llamadas recursivas tuvo éxito, se añade la celda actual al camino.
            this.camino.add(cell);
            return true;
        }
        return false;
    }

    /**
     * Verifica si una celda en las coordenadas dadas es válida para ser visitada.
     * Una celda es válida si está dentro de los límites del laberinto y no es un muro.
     *
     * @param paramArrayOfCell El laberinto.
     * @param paramInt1        La fila a verificar.
     * @param paramInt2        La columna a verificar.
     * @return {@code true} si la celda es válida, {@code false} en caso contrario.
     */
    private boolean isValid(Cell[][] paramArrayOfCell, int paramInt1, int paramInt2) {
        return (paramInt1 >= 0 && paramInt1 < paramArrayOfCell.length && paramInt2 >= 0 && paramInt2 < (paramArrayOfCell[0]).length && (paramArrayOfCell[paramInt1][paramInt2]).state != CellState.WALL);
    }
}