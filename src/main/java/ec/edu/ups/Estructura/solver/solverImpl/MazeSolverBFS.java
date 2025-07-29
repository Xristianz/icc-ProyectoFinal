package ec.edu.ups.Estructura.solver.solverImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.CellState;
import ec.edu.ups.Estructura.models.SolveResult;
import ec.edu.ups.Estructura.solver.MazeSolver;

/**
 * `MazeSolverBFS` implementa la interfaz `MazeSolver` utilizando el algoritmo de Búsqueda en Amplitud (BFS).
 * Esta clase encuentra el camino más corto desde una celda inicial hasta una celda final en un laberinto dado.
 */
public class MazeSolverBFS implements MazeSolver {

    /**
     * Encuentra un camino desde la celda de inicio hasta la celda final en el laberinto dado utilizando el algoritmo BFS.
     *
     * @param paramArrayOfCell Un arreglo 2D de objetos `Cell` que representa la cuadrícula del laberinto.
     * @param paramCell1 La `Cell` de inicio.
     * @param paramCell2 La `Cell` final.
     * @return Un objeto `SolveResult` que contiene dos listas de celdas:
     * 1. La lista `visitedCells`, que contiene todas las celdas exploradas durante el recorrido BFS.
     * 2. La lista `path`, que contiene las celdas que forman el camino más corto desde la celda de inicio hasta la celda final.
     * Si no se encuentra un camino, la lista `path` estará vacía.
     */
    @Override
    public SolveResult getPath(Cell[][] paramArrayOfCell, Cell paramCell1, Cell paramCell2) {
        // Dimensiones del laberinto
        int i = paramArrayOfCell.length;
        int j = (paramArrayOfCell[0]).length;

        // `arrayOfBoolean` (visitado) rastrea las celdas visitadas para evitar ciclos y procesamiento redundante.
        boolean[][] arrayOfBoolean = new boolean[i][j];

        // `hashMap` (parentMap) almacena el padre de cada celda, crucial para reconstruir el camino.
        // La clave es la celda actual y el valor es la celda desde la que fue descubierta.
        HashMap<Object, Object> hashMap = new HashMap<>();

        // `linkedList` (cola) es la cola utilizada para el recorrido BFS.
        LinkedList<Cell> linkedList = new LinkedList();

        // `arrayList1` (celdasVisitadas) almacena el orden en que las celdas son visitadas por el algoritmo BFS.
        ArrayList<Cell> arrayList1 = new ArrayList();

        // Obtener las celdas de inicio y fin reales de la cuadrícula del laberinto usando sus índices de fila y columna.
        Cell cell1 = paramArrayOfCell[paramCell1.row][paramCell1.col];
        Cell cell2 = paramArrayOfCell[paramCell2.row][paramCell2.col];

        // Iniciar BFS desde `cell1` (celda de inicio).
        linkedList.add(cell1);
        arrayOfBoolean[cell1.row][cell1.col] = true; // Marcar la celda de inicio como visitada.

        // Bucle de recorrido BFS: continúa mientras haya celdas por visitar en la cola.
        while (!linkedList.isEmpty()) {
            // Desencolar la celda actual.
            Cell cell = linkedList.poll();
            arrayList1.add(cell); // Añadir la celda desencolada a la lista de celdas visitadas.

            // Si la celda actual es la `cell2` (celda final), se ha encontrado el camino, así que se rompe el bucle.
            if (cell.equals(cell2))
                break;

            // Explorar vecinos: iterar a través de los posibles movimientos (arriba, abajo, izquierda, derecha).
            for (int[] arrayOfInt : new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }) {
                int k = cell.row + arrayOfInt[0]; // Calcular la fila del vecino.
                int m = cell.col + arrayOfInt[1]; // Calcular la columna del vecino.

                // Verificar si el vecino está dentro de los límites del laberinto.
                if (k >= 0 && k < i && m >= 0 && m < j) {
                    Cell cell4 = paramArrayOfCell[k][m]; // Obtener la celda vecina.

                    // Si el vecino no ha sido visitado y no es una pared:
                    if (!arrayOfBoolean[k][m] && cell4.state != CellState.WALL) {
                        arrayOfBoolean[k][m] = true; // Marcar el vecino como visitado.
                        hashMap.put(cell4, cell); // Almacenar la celda actual como padre del vecino.
                        linkedList.add(cell4); // Encolar al vecino para su posterior exploración.
                    }
                }
            }
        }

        // `arrayList2` (camino) almacenará el camino más corto reconstruido.
        ArrayList<Cell> arrayList2 = new ArrayList();
        Cell cell3 = cell2; // Iniciar la reconstrucción del camino desde la celda final.

        // Reconstruir el camino retrocediendo desde la celda final hasta la celda de inicio usando `parentMap`.
        while (hashMap.containsKey(cell3)) {
            arrayList2.add(cell3); // Añadir la celda actual al camino.
            cell3 = (Cell)hashMap.get(cell3); // Moverse a la celda padre.
        }

        // Si `cell3` (celda actual durante el retroceso) es `cell1` (celda de inicio),
        // significa que se encontró un camino con éxito.
        if (cell3.equals(cell1)) {
            arrayList2.add(cell1); // Añadir la celda de inicio al camino.
            Collections.reverse(arrayList2); // Invertir el camino para obtenerlo de inicio a fin.
        } else {
            // Si no se llegó a la celda de inicio durante el retroceso, no existe un camino.
            arrayList2.clear(); // Limpiar la lista del camino.
        }

        // Devolver el `SolveResult` que contiene las celdas visitadas y el camino encontrado.
        return new SolveResult(arrayList1, arrayList2);
    }
}