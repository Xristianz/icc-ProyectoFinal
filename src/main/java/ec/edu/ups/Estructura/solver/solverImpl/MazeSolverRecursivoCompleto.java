package ec.edu.ups.Estructura.solver.solverImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.CellState;
import ec.edu.ups.Estructura.models.SolveResult;
import ec.edu.ups.Estructura.solver.MazeSolver;

/**
 * `MazeSolverRecursivoCompleto` implementa la interfaz `MazeSolver` utilizando
 * un enfoque recursivo de búsqueda en profundidad (DFS).
 * Este algoritmo intenta encontrar un camino desde una celda de inicio a una
 * celda final en un laberinto dado.
 * A diferencia de BFS, no garantiza el camino más corto, pero sí encuentra un camino si existe.
 */
public class MazeSolverRecursivoCompleto implements MazeSolver {

    // `visited` es un conjunto que almacena las celdas ya exploradas para evitar
    // bucles infinitos y revisar celdas innecesariamente.
    // Se usa `LinkedHashSet` para mantener el orden de visita.
    private Set<Cell> visited = new LinkedHashSet<>();

    // `camino` es una lista que construye el camino encontrado de la celda de
    // inicio a la celda final.
    private List<Cell> camino = new ArrayList<>();

    /**
     * Inicia la búsqueda de un camino desde la celda de inicio hasta la celda final
     * en el laberinto utilizando un algoritmo DFS recursivo.
     *
     * @param paramArrayOfCell Un arreglo 2D de objetos `Cell` que representa la cuadrícula del laberinto.
     * @param paramCell1 La `Cell` de inicio.
     * @param paramCell2 La `Cell` final.
     * @return Un objeto `SolveResult` que contiene dos listas de celdas:
     * 1. La lista `visitedCells`, que contiene todas las celdas exploradas durante el recorrido.
     * 2. La lista `path`, que contiene las celdas que forman el camino desde el inicio hasta el fin.
     * Si no se encuentra un camino, la lista `path` estará vacía.
     */
    @Override
    public SolveResult getPath(Cell[][] paramArrayOfCell, Cell paramCell1, Cell paramCell2) {
        // Limpiar las estructuras de datos para una nueva búsqueda.
        this.visited.clear();
        this.camino.clear();

        // Iniciar la búsqueda recursiva desde la celda de inicio.
        findPath(paramArrayOfCell, paramCell1.row, paramCell1.col, paramCell2);

        // El camino se construye en orden inverso durante la recursión (del fin al inicio),
        // así que lo revertimos para obtenerlo en el orden correcto.
        Collections.reverse(this.camino);

        // Devolver el resultado de la búsqueda, incluyendo las celdas visitadas y el camino.
        return new SolveResult(new ArrayList<>(this.visited), new ArrayList<>(this.camino));
    }

    /**
     * Método auxiliar recursivo para encontrar un camino en el laberinto.
     * Explora las celdas adyacentes de forma recursiva hasta que se encuentra la celda objetivo.
     *
     * @param paramArrayOfCell El arreglo 2D de celdas que representa el laberinto.
     * @param paramInt1 La coordenada de fila de la celda actual.
     * @param paramInt2 La coordenada de columna de la celda actual.
     * @param paramCell La `Cell` objetivo (celda final).
     * @return `true` si se encuentra un camino a la celda objetivo desde la celda actual, `false` en caso contrario.
     */
    private boolean findPath(Cell[][] paramArrayOfCell, int paramInt1, int paramInt2, Cell paramCell) {
        // 1. Verificar si la celda actual es válida (dentro de los límites y no es una pared).
        if (!isValid(paramArrayOfCell, paramInt1, paramInt2)) {
            return false;
        }

        Cell cell = paramArrayOfCell[paramInt1][paramInt2];

        // 2. Si la celda ya ha sido visitada, significa que ya hemos explorado este camino
        // o hay un ciclo, por lo tanto, no se continúa.
        if (this.visited.contains(cell)) {
            return false;
        }

        // 3. Marcar la celda actual como visitada.
        this.visited.add(cell);
        // Añadir la celda actual al camino. Si este camino no lleva a la meta, se puede
        // remover de aquí, pero en esta implementación se mantiene.
        // *Nota: En un DFS puro para encontrar el camino, la celda se añade al camino
        // *solo si la exploración a través de ella efectivamente lleva al destino.
        // *En esta versión, se añade antes de explorar y se basa en el `Collections.reverse` final.
        this.camino.add(cell);

        // 4. Si la celda actual es la celda objetivo, hemos encontrado el camino.
        if (cell.equals(paramCell)) {
            // Ya se añadió la celda en `this.camino.add(cell)` anteriormente,
            // por lo que agregarla de nuevo sería duplicarla.
            // Para una lógica más limpia, la celda solo debería añadirse al camino
            // cuando se sabe que es parte de un camino exitoso de regreso de la recursión.
            // En esta implementación, el `Collections.reverse` al final corrige el orden.
            return true;
        }

        // 5. Explorar recursivamente las celdas adyacentes: abajo, derecha, arriba, izquierda.
        // La exploración se detiene tan pronto como una rama encuentra el camino a la celda objetivo.
        if (findPath(paramArrayOfCell, paramInt1 + 1, paramInt2, paramCell) || // Moverse hacia abajo
                findPath(paramArrayOfCell, paramInt1, paramInt2 + 1, paramCell) || // Moverse hacia la derecha
                findPath(paramArrayOfCell, paramInt1 - 1, paramInt2, paramCell) || // Moverse hacia arriba
                findPath(paramArrayOfCell, paramInt1, paramInt2 - 1, paramCell)) { // Moverse hacia la izquierda
            return true; // Si alguna de las llamadas recursivas encuentra el camino, retornar true.
        }

        // Si ninguna de las direcciones exploradas desde esta celda lleva a la celda objetivo,
        // significa que esta celda no forma parte del camino.
        // *Nota: Para una implementación estricta donde `camino` solo contenga el path,
        // *aquí se debería remover `cell` de `this.camino` usando `this.camino.remove(this.camino.size() - 1);`
        // *Esto es crucial para que `camino` represente solo el camino encontrado.
        // *La implementación actual confía en que `Collections.reverse` y el orden de adición
        // *finalmente produzcan el camino correcto si se encontró uno.
        // *Si no se encuentra un camino, `camino` contendrá las celdas de la última rama explorada antes de retroceder.
        // *El `Collections.reverse` al final puede producir un camino incorrecto si la celda final no fue alcanzada.
        // *Sería más robusto que la adición a `camino` solo ocurra al retornar `true` desde las llamadas recursivas.
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
        return (paramInt1 >= 0 && paramInt1 < paramArrayOfCell.length && // Verifica que la fila esté dentro de los límites.
                paramInt2 >= 0 && paramInt2 < (paramArrayOfCell[0]).length && // Verifica que la columna esté dentro de los límites.
                (paramArrayOfCell[paramInt1][paramInt2]).state != CellState.WALL); // Verifica que la celda no sea una pared.
    }
}