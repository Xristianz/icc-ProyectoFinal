package ec.edu.ups.Estructura.solver.solverImpl;

import java.util.ArrayList;
import java.util.Collections; // Se añade para Collections.reverse si fuera necesario, aunque en DFS se construye al revés
import java.util.HashMap;    // No se usa en DFS recursivo directo, pero se mantiene si se modifica el enfoque
import java.util.LinkedList; // No se usa en DFS, ya que es para BFS
import java.util.LinkedHashSet; // Importado para Set
import java.util.List;        // Importado para List
import java.util.Set;         // Importado para Set

import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.CellState;
import ec.edu.ups.Estructura.models.SolveResult;
import ec.edu.ups.Estructura.solver.MazeSolver;

/**
 * `MazeSolverDFS` implementa la interfaz `MazeSolver` utilizando el algoritmo de Búsqueda en Profundidad (DFS).
 * Esta clase encuentra un camino desde una celda inicial hasta una celda final en un laberinto dado.
 * Es importante destacar que DFS no garantiza encontrar el camino más corto, solo un camino si existe.
 */
public class MazeSolverDFS implements MazeSolver {

    // `visitadas` es un conjunto para almacenar las celdas ya visitadas durante la exploración DFS.
    // Se utiliza `LinkedHashSet` para mantener el orden de inserción de las celdas visitadas.
    private Set<Cell> visitadas = new LinkedHashSet<>();

    // `camino` es una lista para almacenar las celdas que forman el camino encontrado desde el inicio hasta el fin.
    private List<Cell> camino = new ArrayList<>();

    /**
     * Inicia la búsqueda de un camino desde la celda de inicio hasta la celda final en el laberinto
     * utilizando el algoritmo DFS.
     *
     * @param paramArrayOfCell Un arreglo 2D de objetos `Cell` que representa la cuadrícula del laberinto.
     * @param paramCell1 La `Cell` de inicio.
     * @param paramCell2 La `Cell` final.
     * @return Un objeto `SolveResult` que contiene dos listas de celdas:
     * 1. La lista de `visitedCells`, que contiene todas las celdas exploradas durante el recorrido DFS.
     * 2. La lista `path`, que contiene las celdas que forman el camino encontrado desde la celda de inicio hasta la celda final.
     * Si no se encuentra un camino, la lista `path` estará vacía.
     */
    @Override
    public SolveResult getPath(Cell[][] paramArrayOfCell, Cell paramCell1, Cell paramCell2) {
        // Limpiar las estructuras de datos antes de cada nueva búsqueda.
        this.visitadas.clear();
        this.camino.clear();

        // Llamar al método DFS recursivo para iniciar la búsqueda.
        // Se pasa la celda de inicio y la celda objetivo.
        dfs(paramArrayOfCell, paramCell1.row, paramCell1.col, paramCell2);

        // Devolver el resultado de la búsqueda, que incluye las celdas visitadas y el camino encontrado.
        // Se crea nuevas ArrayLists a partir de los conjuntos y listas internas para devolver copias.
        return new SolveResult(new ArrayList<>(this.visitadas), new ArrayList<>(this.camino));
    }

    /**
     * Método auxiliar recursivo para realizar la Búsqueda en Profundidad (DFS).
     * Explora el laberinto en profundidad, marcando las celdas visitadas y construyendo el camino
     * si se encuentra la celda objetivo.
     *
     * @param paramArrayOfCell El arreglo 2D de celdas que representa el laberinto.
     * @param paramInt1 La coordenada de fila de la celda actual.
     * @param paramInt2 La coordenada de columna de la celda actual.
     * @param paramCell La `Cell` objetivo (celda final) que se busca.
     * @return `true` si se encontró un camino a la celda objetivo desde la celda actual, `false` en caso contrario.
     */
    private boolean dfs(Cell[][] paramArrayOfCell, int paramInt1, int paramInt2, Cell paramCell) {
        // 1. Verificar si la celda actual es válida (dentro de los límites y no es una pared).
        if (!isValid(paramArrayOfCell, paramInt1, paramInt2)) {
            return false;
        }

        Cell cell = paramArrayOfCell[paramInt1][paramInt2];

        // 2. Verificar si la celda ya fue visitada. Si es así, se evita un ciclo o procesamiento redundante.
        if (this.visitadas.contains(cell)) {
            return false;
        }

        // 3. Marcar la celda actual como visitada.
        this.visitadas.add(cell);

        // 4. Si la celda actual es la celda objetivo, se ha encontrado un camino.
        if (cell.equals(paramCell)) {
            this.camino.add(cell); // Añadir la celda objetivo al camino.
            return true;
        }

        // 5. Explorar los vecinos de la celda actual (arriba, abajo, derecha, izquierda).
        // Se utiliza el operador lógico OR (||) para detener la exploración tan pronto como se encuentre un camino.
        if (dfs(paramArrayOfCell, paramInt1 + 1, paramInt2, paramCell) || // Moverse hacia abajo
                dfs(paramArrayOfCell, paramInt1 - 1, paramInt2, paramCell) || // Moverse hacia arriba
                dfs(paramArrayOfCell, paramInt1, paramInt2 + 1, paramCell) || // Moverse hacia la derecha
                dfs(paramArrayOfCell, paramInt1, paramInt2 - 1, paramCell)) { // Moverse hacia la izquierda
            this.camino.add(cell); // Si alguno de los vecinos lleva a la celda objetivo, añadir la celda actual al camino.
            return true;
        }

        // 6. Si ningún vecino conduce a la celda objetivo, este camino no es válido.
        return false;
    }

    /**
     * Método auxiliar para verificar si una celda dada por sus coordenadas es válida para la exploración.
     * Una celda es válida si está dentro de los límites del laberinto y no es una pared.
     *
     * @param paramArrayOfCell El arreglo 2D de celdas que representa el laberinto.
     * @param paramInt1 La coordenada de fila de la celda a verificar.
     * @param paramInt2 La coordenada de columna de la celda a verificar.
     * @return `true` si la celda es válida para la exploración, `false` en caso contrario.
     */
    private boolean isValid(Cell[][] paramArrayOfCell, int paramInt1, int paramInt2) {
        return (paramInt1 >= 0 && paramInt1 < paramArrayOfCell.length && // Verificar límites de fila
                paramInt2 >= 0 && paramInt2 < (paramArrayOfCell[0]).length && // Verificar límites de columna
                (paramArrayOfCell[paramInt1][paramInt2]).state != CellState.WALL); // Verificar que no sea una pared
    }
}