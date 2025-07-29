package ec.edu.ups.Estructura.solver.solverImpl;

import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.CellState;
import ec.edu.ups.Estructura.models.SolveResult;
import ec.edu.ups.Estructura.solver.MazeSolver;
import java.util.*;

/**
 * `MazeSolverRecursivoCompletoBT` implementa la interfaz `MazeSolver` utilizando un
 * algoritmo de Búsqueda en Profundidad (DFS) recursivo con **Backtracking**.
 * Este algoritmo busca un camino desde una celda de inicio hasta una celda final
 * en un laberinto, deshaciendo los pasos si un camino no conduce al objetivo.
 */
public class MazeSolverRecursivoCompletoBT implements MazeSolver {

    // `visited` es un conjunto que rastrea las celdas ya visitadas para evitar ciclos
    // y revisitas innecesarias. Se utiliza `LinkedHashSet` para mantener el orden de inserción.
    private Set<Cell> visited = new LinkedHashSet<>();

    // `camino` es una lista que almacena las celdas que forman el camino actual que se está explorando.
    // Con el backtracking, las celdas se añaden y se eliminan a medida que se avanza y se retrocede.
    private List<Cell> camino = new ArrayList<>();

    /**
     * Inicia la búsqueda de un camino desde la celda de inicio hasta la celda final
     * en el laberinto utilizando el algoritmo DFS recursivo con backtracking.
     *
     * @param paramArrayOfCell Un arreglo 2D de objetos `Cell` que representa la cuadrícula del laberinto.
     * @param paramCell1 La `Cell` de inicio.
     * @param paramCell2 La `Cell` final.
     * @return Un objeto `SolveResult` que contiene dos listas de celdas:
     * 1. La lista `visitedCells`, que contiene todas las celdas exploradas (visitadas) durante el proceso.
     * 2. La lista `path`, que contiene las celdas que forman el camino encontrado desde el inicio hasta el fin.
     * Si no se encuentra un camino, la lista `path` estará vacía.
     */
    @Override
    public SolveResult getPath(Cell[][] paramArrayOfCell, Cell paramCell1, Cell paramCell2) {
        // Limpiar las estructuras de datos al inicio de cada nueva búsqueda.
        this.visited.clear();
        this.camino.clear();

        // Iniciar la búsqueda recursiva desde la celda de inicio.
        findPath(paramArrayOfCell, paramCell1.row, paramCell1.col, paramCell2);

        // El camino se construye desde el destino hacia el origen durante la recursión,
        // por lo que necesitamos invertirlo para tenerlo en el orden correcto (inicio a fin).
        Collections.reverse(this.camino);

        // Devolver el resultado de la búsqueda, incluyendo las celdas visitadas y el camino final.
        return new SolveResult(new ArrayList<>(this.visited), new ArrayList<>(this.camino));
    }

    /**
     * Método auxiliar recursivo que implementa la lógica de Búsqueda en Profundidad (DFS) con backtracking.
     * Explora el laberinto, añadiendo celdas al camino y retrocediendo si un camino no es válido.
     *
     * @param paramArrayOfCell El arreglo 2D de celdas que representa el laberinto.
     * @param paramInt1 La coordenada de fila de la celda actual.
     * @param paramInt2 La coordenada de columna de la celda actual.
     * @param paramCell La `Cell` objetivo (celda final) que se busca.
     * @return `true` si se encontró un camino a la celda objetivo desde la celda actual, `false` en caso contrario.
     */
    private boolean findPath(Cell[][] paramArrayOfCell, int paramInt1, int paramInt2, Cell paramCell) {
        // 1. **Condición de límite / Invalidación**: Verifica si la celda está fuera de los límites,
        // es una pared o ya ha sido visitada en la ruta actual.
        if (!isValid(paramArrayOfCell, paramInt1, paramInt2)) {
            return false;
        }
        Cell cell = paramArrayOfCell[paramInt1][paramInt2];
        if (this.visited.contains(cell)) { // Importante: `visited` aquí es para la rama actual de exploración
            return false;
        }

        // 2. **Marcar y añadir al camino**: Si la celda es válida y no visitada en esta ruta,
        // la marcamos como visitada y la añadimos al camino temporal.
        this.visited.add(cell);
        this.camino.add(cell);

        // 3. **Condición de éxito**: Si la celda actual es la celda objetivo, hemos encontrado el camino.
        if (cell.equals(paramCell)) {
            return true; // Un camino válido ha sido encontrado.
        }

        // 4. **Explorar vecinos**: Intenta moverse en todas las direcciones posibles (abajo, derecha, arriba, izquierda).
        // Si cualquiera de estas llamadas recursivas encuentra el camino, retornamos `true` inmediatamente.
        if (findPath(paramArrayOfCell, paramInt1 + 1, paramInt2, paramCell) || // Moverse hacia abajo
                findPath(paramArrayOfCell, paramInt1, paramInt2 + 1, paramCell) || // Moverse hacia la derecha
                findPath(paramArrayOfCell, paramInt1 - 1, paramInt2, paramCell) || // Moverse hacia arriba
                findPath(paramArrayOfCell, paramInt1, paramInt2 - 1, paramCell)) { // Moverse hacia la izquierda
            return true; // Se encontró un camino a través de una de las direcciones.
        }

        // 5. **Backtracking**: Si ninguno de los movimientos desde la celda actual lleva a la celda objetivo,
        // significa que esta celda no forma parte del camino. Por lo tanto, se "retrocede" (backtrack)
        // eliminando la celda del camino actual. También se podría remover de `visited` si solo se quisiera
        // registrar celdas que son parte del camino final, pero en esta implementación `visited` registra
        // todas las celdas exploradas.
        this.camino.remove(this.camino.size() - 1);
        return false; // Este camino no lleva al objetivo.
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
        // Comprueba que la fila esté dentro de los límites del laberinto.
        return (paramInt1 >= 0 && paramInt1 < paramArrayOfCell.length &&
                // Comprueba que la columna esté dentro de los límites del laberinto.
                paramInt2 >= 0 && paramInt2 < (paramArrayOfCell[0]).length &&
                // Comprueba que la celda no sea una pared.
                (paramArrayOfCell[paramInt1][paramInt2]).state != CellState.WALL);
    }
}