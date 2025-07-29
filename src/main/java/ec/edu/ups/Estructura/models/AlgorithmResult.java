package ec.edu.ups.Estructura.models;

/**
 * Modelo de datos para almacenar las métricas de rendimiento de una ejecución de algoritmo.
 * Guarda el nombre, el tamaño del camino y el tiempo de ejecución.
 *
 * @author Cristian Moscoso
 */
public class AlgorithmResult {
    /**
     * El nombre del algoritmo ejecutado (ej. "BFS", "DFS").
     */
    private String algorithmName;
    /**
     * El número de celdas que componen el camino encontrado.
     */
    private int pathSize;
    /**
     * El tiempo total de ejecución del algoritmo, medido en nanosegundos.
     */
    private long timeNs;

    /**
     * Construye una nueva instancia de resultado de algoritmo.
     *
     * @param algorithmName El nombre del algoritmo.
     * @param pathSize      El tamaño del camino.
     * @param timeNs        El tiempo de ejecución en nanosegundos.
     */
    public AlgorithmResult(String algorithmName, int pathSize, long timeNs) {
        this.algorithmName = algorithmName;
        this.pathSize = pathSize;
        this.timeNs = timeNs;
    }

    /**
     * Obtiene el nombre del algoritmo.
     * @return El nombre del algoritmo.
     */
    public String getAlgorithmName() {
        return algorithmName;
    }

    /**
     * Obtiene el tamaño del camino.
     * @return El número de celdas en el camino.
     */
    public int getPathSize() {
        return pathSize;
    }

    /**
     * Obtiene el tiempo de ejecución en nanosegundos.
     * @return El tiempo de ejecución.
     */
    public long getTimeNs() {
        return timeNs;
    }

    /**
     * Establece el nombre del algoritmo.
     * @param algorithmName El nuevo nombre.
     */
    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    /**
     * Establece el tamaño del camino.
     * @param pathSize El nuevo tamaño del camino.
     */
    public void setPathSize(int pathSize) {
        this.pathSize = pathSize;
    }

    /**
     * Establece el tiempo de ejecución en nanosegundos.
     * @param timeNs El nuevo tiempo de ejecución.
     */
    public void setTimeNs(long timeNs) {
        this.timeNs = timeNs;
    }

    /**
     * Devuelve una representación en formato CSV del objeto.
     *
     * @return Una cadena con los atributos separados por comas.
     */
    @Override
    public String toString() {
        return algorithmName + "," + pathSize + "," + timeNs;
    }
}
