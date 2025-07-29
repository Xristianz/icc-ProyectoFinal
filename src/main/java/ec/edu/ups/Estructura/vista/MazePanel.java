package ec.edu.ups.Estructura.vista;

import ec.edu.ups.Estructura.controlador.MazeController;
import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.CellState;

import javax.swing.*;
import java.awt.*;

/**
 * Clase MazePanel
 *
 * Esta clase representa el panel visual del laberinto en la interfaz gráfica.
 * Se encarga de dibujar las celdas del laberinto como botones y gestionar
 * las interacciones del usuario con estas celdas.
 *
 * @author Moises Piguave
 * @since 7/28/2025
 */
public class MazePanel extends JPanel {
    /**
     * Número de filas del laberinto.
     */
    private final int rows;

    /**
     * Número de columnas del laberinto.
     */
    private final int cols;

    /**
     * Matriz de objetos Cell que representa el estado lógico de cada celda del laberinto.
     */
    private final Cell[][] cells;

    /**
     * Matriz de objetos JButton que representa los botones visuales de cada celda del laberinto.
     */
    private final JButton[][] buttons;

    /**
     * Controlador del laberinto que maneja la lógica de negocio y las interacciones.
     */
    private MazeController controller;

    /**
     * Constructor de la clase MazePanel.
     *
     * @param paramInt1 El número de filas para el laberinto.
     * @param paramInt2 El número de columnas para el laberinto.
     */
    public MazePanel(int paramInt1, int paramInt2) {
        this.rows = paramInt1; // parametro 1
        this.cols = paramInt2; // parametro 2
        this.cells = new Cell[paramInt1][paramInt2];
        this.buttons = new JButton[paramInt1][paramInt2];
        setLayout(new GridLayout(paramInt1, paramInt2)); // Establece un GridLayout para organizar los botones.
        initGrid(); // Inicializa la cuadrícula de botones y celdas.
    }

    /**
     * Establece el controlador para el panel del laberinto.
     *
     * @param paramMazeController El controlador de laberinto a asignar.
     */
    public void setController(MazeController paramMazeController) {
        this.controller = paramMazeController;
    }

    /**
     * Inicializa la cuadrícula de celdas y botones.
     * Crea un objeto Cell y un JButton para cada posición en la cuadrícula,
     * configura su apariencia y añade un ActionListener a cada botón.
     */
    private void initGrid() {
        for (byte b = 0; b < this.rows; b++) {
            for (byte b1 = 0; b1 < this.cols; b1++) {
                Cell cell = new Cell(b, b1); // Crea una nueva celda lógica.
                JButton jButton = new JButton(); // Crea un nuevo botón visual.
                jButton.setBackground(Color.WHITE); // Establece el color de fondo predeterminado a blanco.
                jButton.setOpaque(true); // Asegura que el color de fondo sea visible.
                jButton.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Añade un borde gris.

                final byte rowCoord = b; // Coordenada de fila para el ActionListener.
                final byte colCoord = b1; // Coordenada de columna para el ActionListener.

                // Agrega un ActionListener a cada botón para manejar clics.
                jButton.addActionListener(paramActionEvent -> {
                    if (this.controller != null)
                        // Llama al método onCellClicked del controlador cuando se hace clic en una celda.
                        this.controller.onCellClicked(rowCoord, colCoord);
                });
                add(jButton); // Añade el botón al panel.
                this.cells[b][b1] = cell; // Almacena la celda lógica en la matriz.
                this.buttons[b][b1] = jButton; // Almacena el botón visual en la matriz.
            }
        }
    }

    /**
     * Limpia el estado visual de las celdas visitadas en el laberinto.
     * Restablece el estado de las celdas (excepto muros, inicio y fin) a EMPTY y su color a blanco.
     */
    public void limpiarCeldasVisitadas() {
        for (byte b = 0; b < this.rows; b++) {
            for (byte b1 = 0; b1 < this.cols; b1++) {
                Cell cell = this.cells[b][b1];
                // Verifica que la celda no sea un muro, inicio o fin antes de limpiarla.
                if (cell.state != CellState.WALL && cell.state != CellState.START && cell.state != CellState.END) {
                    cell.state = CellState.EMPTY; // Cambia el estado lógico a EMPTY.
                    this.buttons[b][b1].setBackground(Color.WHITE); // Cambia el color del botón a blanco.
                }
            }
        }
    }

    /**
     * Obtiene la matriz de celdas lógicas del laberinto.
     *
     * @return La matriz de objetos Cell.
     */
    public Cell[][] getCells() {
        return this.cells;
    }

    /**
     * Obtiene el botón (JButton) en las coordenadas especificadas.
     *
     * @param paramInt1 La coordenada de la fila.
     * @param paramInt2 La coordenada de la columna.
     * @return El JButton en la posición especificada.
     */
    public JButton getButton(int paramInt1, int paramInt2) {
        return this.buttons[paramInt1][paramInt2];
    }
}