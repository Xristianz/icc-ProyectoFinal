package ec.edu.ups.Estructura.vista;
import ec.edu.ups.Estructura.controlador.MazeController;
import ec.edu.ups.Estructura.dao.AlgorithmResultDAO;
import ec.edu.ups.Estructura.dao.DaoImpl.AlgorithmResultDAOFile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import ec.edu.ups.Estructura.models.AlgorithmResult;
import ec.edu.ups.Estructura.models.Cell;
import ec.edu.ups.Estructura.models.CellState;
import ec.edu.ups.Estructura.models.SolveResult;
import ec.edu.ups.Estructura.solver.MazeSolver;
import ec.edu.ups.Estructura.solver.solverImpl.MazeSolverBFS;
import ec.edu.ups.Estructura.solver.solverImpl.MazeSolverDFS;
import ec.edu.ups.Estructura.solver.solverImpl.MazeSolverRecursivo;
import ec.edu.ups.Estructura.solver.solverImpl.MazeSolverRecursivoCompleto;
import ec.edu.ups.Estructura.solver.solverImpl.MazeSolverRecursivoCompletoBT;

/**
 * Clase MazeFrame
 *
 * Esta clase representa la ventana principal de la aplicación de creación y resolución de laberintos.
 * Contiene el panel del laberinto, controles para establecer puntos de inicio/fin y muros,
 * un selector de algoritmos de resolución, botones para resolver y limpiar, y opciones de menú.
 *
 * @author Moises Piguave
 * @since 7/28/2025
 */
public class MazeFrame extends JFrame {
    /**
     * El panel donde se dibuja el laberinto y sus celdas.
     */
    private final MazePanel mazePanel;

    /**
     * El controlador que gestiona la lógica de interacción con el laberinto.
     */
    private final MazeController controller;

    /**
     * ComboBox para seleccionar el algoritmo de resolución del laberinto.
     */
    private final JComboBox<String> algorithmSelector;

    /**
     * Botón para iniciar la resolución del laberinto.
     */
    private final JButton solveButton;

    /**
     * Botón para ejecutar la resolución paso a paso.
     */
    private final JButton pasoAPasoButton = new JButton("Paso a paso");

    /**
     * Lista de celdas visitadas durante la resolución en modo paso a paso.
     */
    private List<Cell> pasoCeldasVisitadas;

    /**
     * Lista de celdas que forman el camino encontrado durante la resolución en modo paso a paso.
     */
    private List<Cell> pasoCamino;

    /**
     * Índice actual para el recorrido paso a paso de las celdas visitadas y el camino.
     */
    private int pasoIndex = 0;

    /**
     * Bandera que indica si la resolución paso a paso ha comenzado.
     */
    private boolean resolvioPasoAPaso = false;

    /**
     * Objeto DAO para guardar y cargar los resultados de los algoritmos.
     */
    private final AlgorithmResultDAO resultDAO;

    /**
     * Mapa estático que asocia cada estado de celda con un color específico para la visualización.
     */
    private static final Map<CellState, Color> COLOR_MAP = new HashMap<>();

    // Bloque estático para inicializar el mapa de colores.
    static {
        COLOR_MAP.put(CellState.EMPTY, Color.LIGHT_GRAY);
        COLOR_MAP.put(CellState.WALL, Color.BLACK);
        COLOR_MAP.put(CellState.START, Color.GREEN);
        COLOR_MAP.put(CellState.END, Color.RED);
        COLOR_MAP.put(CellState.PATH, Color.BLUE);
    }

    /**
     * Constructor de la clase MazeFrame.
     *
     * @param paramInt1 El número de filas para el laberinto.
     * @param paramInt2 El número de columnas para el laberinto.
     */
    public MazeFrame(int paramInt1, int paramInt2) {
        // Inicializa el DAO para guardar los resultados en un archivo CSV.
        this.resultDAO = (AlgorithmResultDAO)new AlgorithmResultDAOFile("results.csv");
        setTitle("Maze Creator"); // Establece el título de la ventana.
        setDefaultCloseOperation(3); // Configura la operación de cierre predeterminada.
        setSize(800, 600); // Establece el tamaño de la ventana.
        setLayout(new BorderLayout()); // Usa un BorderLayout para organizar los componentes.

        this.mazePanel = new MazePanel(paramInt1, paramInt2); // Crea una nueva instancia de MazePanel.
        this.controller = new MazeController(this.mazePanel); // Crea un controlador de laberinto asociado al panel.
        this.mazePanel.setController(this.controller); // Asigna el controlador al MazePanel.
        add(this.mazePanel, "Center"); // Agrega el MazePanel al centro del frame.

        JPanel jPanel1 = new JPanel(); // Panel para los botones de modo.
        JButton jButton1 = new JButton("Set Start");
        JButton jButton2 = new JButton("Set End");
        JButton jButton3 = new JButton("Toggle Wall");

        // Asigna ActionListeners a los botones para cambiar el modo del controlador.
        jButton1.addActionListener(paramActionEvent -> this.controller.setMode(MazeController.Mode.START));
        jButton2.addActionListener(paramActionEvent -> this.controller.setMode(MazeController.Mode.END));
        jButton3.addActionListener(paramActionEvent -> this.controller.setMode(MazeController.Mode.WALL));

        jPanel1.add(jButton1);
        jPanel1.add(jButton2);
        jPanel1.add(jButton3);
        add(jPanel1, "North"); // Agrega el panel de botones de modo en la parte superior.

        // Opciones de algoritmos para el JComboBox.
        String[] arrayOfString = { "Recursivo", "Recursivo Completo", "Recursivo Completo BT", "BFS", "DFS", "Backtracking" };
        this.algorithmSelector = new JComboBox<>(arrayOfString); // Crea el JComboBox con los algoritmos.
        this.solveButton = new JButton("Resolver"); // Crea el botón de resolver.

        JPanel jPanel2 = new JPanel(); // Panel para el selector de algoritmos y botones de acción.
        jPanel2.add(new JLabel("Algoritmo:"));
        jPanel2.add(this.algorithmSelector);
        jPanel2.add(this.solveButton);
        jPanel2.add(this.pasoAPasoButton);
        add(jPanel2, "South"); // Agrega el panel de controles en la parte inferior.

        // ActionListener para el botón "Resolver".
        this.solveButton.addActionListener(paramActionEvent -> {
            SolveResult solveResults = resolverYObtenerResultados(); // Resuelve el laberinto y obtiene los resultados.
            if (solveResults != null)
                animarVisitadas(solveResults.visitadas, solveResults.camino); // Anima las celdas visitadas y el camino.
        });

        // ActionListener para el botón "Paso a paso".
        this.pasoAPasoButton.addActionListener(paramActionEvent -> {
            if (!this.resolvioPasoAPaso) { // Si no ha empezado el paso a paso, lo inicializa.
                SolveResult solveResults = resolverYObtenerResultados();
                if (solveResults != null) {
                    this.pasoCeldasVisitadas = solveResults.visitadas;
                    this.pasoCamino = solveResults.camino;
                    this.pasoIndex = 0;
                    this.resolvioPasoAPaso = true;
                }
            } else if (this.pasoIndex < this.pasoCeldasVisitadas.size()) { // Muestra las celdas visitadas paso a paso.
                Cell cell = this.pasoCeldasVisitadas.get(this.pasoIndex++);
                if (cell.state == CellState.EMPTY)
                    paintCell(cell, CellState.EMPTY); // Pinta la celda como visitada (o vacía si es su estado original).
            } else if (this.pasoIndex - this.pasoCeldasVisitadas.size() < this.pasoCamino.size()) { // Muestra el camino paso a paso.
                int i = this.pasoIndex - this.pasoCeldasVisitadas.size();
                Cell cell = this.pasoCamino.get(i);
                this.pasoIndex++;
                if (cell.state != CellState.START && cell.state != CellState.END)
                    paintCell(cell, CellState.PATH); // Pinta la celda como parte del camino.
            } else {
                JOptionPane.showMessageDialog(this, "Ya se ha mostrado todo el recorrido."); // Mensaje al finalizar.
                this.resolvioPasoAPaso = false; // Resetea el modo paso a paso.
            }
        });

        JButton jButton4 = new JButton("Limpiar"); // Botón para limpiar el laberinto.
        // ActionListener para el botón "Limpiar".
        jButton4.addActionListener(paramActionEvent -> {
            this.mazePanel.limpiarCeldasVisitadas(); // Limpia las celdas visitadas en el panel.
            limpiarPasoAPaso(); // Resetea el estado del modo paso a paso.
        });
        jPanel2.add(jButton4); // Agrega el botón de limpiar al panel inferior.

        // Configuración de la barra de menú.
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu1 = new JMenu("Archivo");
        JMenuItem jMenuItem1 = new JMenuItem("Nuevo laberinto");
        jMenuItem1.addActionListener(paramActionEvent -> reiniciarLaberinto()); // Acción para reiniciar el laberinto.
        jMenu1.add(jMenuItem1);
        jMenuBar.add(jMenu1);

        JMenu jMenu2 = new JMenu("Ayuda");
        JMenuItem jMenuItem2 = new JMenuItem("Acerca de");
        jMenuItem2.addActionListener(paramActionEvent -> mostrarAcercaDe()); // Acción para mostrar información "Acerca de".
        jMenu2.add(jMenuItem2);
        jMenuBar.add(jMenu2);

        JMenuItem jMenuItem3 = new JMenuItem("Ver resultados");
        jMenuItem3.addActionListener(paramActionEvent -> {
            ResultadosDialog resultadosDialog = new ResultadosDialog(this, this.resultDAO);
            resultadosDialog.setVisible(true); // Abre el diálogo de resultados.
        });
        jMenu1.add(jMenuItem3); // Agrega la opción "Ver resultados" al menú "Archivo".
        setJMenuBar(jMenuBar); // Establece la barra de menú en el frame.

        setVisible(true); // Hace visible la ventana principal.
    }

    /**
     * Resuelve el laberinto utilizando el algoritmo seleccionado y devuelve los resultados.
     *
     * @return Un objeto SolveResult con las celdas visitadas y el camino, o null si hay un error.
     */
    private SolveResult resolverYObtenerResultados() {
        MazeSolver selectedSolver = null;
        Cell cell1 = this.controller.getStartCell(); // Obtiene la celda de inicio.
        Cell cell2 = this.controller.getEndCell(); // Obtiene la celda de fin.

        if (cell1 == null || cell2 == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero el origen y destino.");
            return null;
        }

        this.mazePanel.limpiarCeldasVisitadas(); // Limpia las celdas visitadas antes de una nueva resolución.
        limpiarPasoAPaso(); // Resetea el estado del modo paso a paso.

        String str = (String)this.algorithmSelector.getSelectedItem(); // Obtiene el algoritmo seleccionado.
        // Selecciona la implementación del algoritmo de resolución basándose en la selección del usuario.
        switch (str) {
            case "Recursivo":
                selectedSolver = new MazeSolverRecursivo();
                break;
            case "Recursivo Completo":
                selectedSolver = new MazeSolverRecursivoCompleto();
                break;
            case "Recursivo Completo BT":
                selectedSolver = new MazeSolverRecursivoCompletoBT();
                break;
            case "DFS":
                selectedSolver = new MazeSolverDFS();
                break;
            case "BFS":
                selectedSolver = new MazeSolverBFS();
                break;
            default:
                selectedSolver = new MazeSolverRecursivo(); // Algoritmo predeterminado.
                break;
        }

        if (selectedSolver == null) {
            JOptionPane.showMessageDialog(this, "Error: No se pudo inicializar el algoritmo.");
            return null;
        }

        long l1 = System.nanoTime(); // Marca de tiempo de inicio.
        SolveResult solveResults = selectedSolver.getPath(this.mazePanel.getCells(), cell1, cell2); // Ejecuta el algoritmo.
        long l2 = System.nanoTime(); // Marca de tiempo de fin.

        // Si se encontró un camino, guarda los resultados.
        if (solveResults != null && !solveResults.camino.isEmpty()) {
            AlgorithmResult algorithmResult = new AlgorithmResult(str, solveResults.camino.size(), l2 - l1);
            this.resultDAO.save(algorithmResult); // Guarda el resultado del algoritmo.
        }
        return solveResults;
    }

    /**
     * Limpia el estado de las variables relacionadas con la visualización paso a paso.
     */
    private void limpiarPasoAPaso() {
        this.pasoCeldasVisitadas = null;
        this.pasoCamino = null;
        this.pasoIndex = 0;
        this.resolvioPasoAPaso = false;
    }

    /**
     * Pinta una celda en el MazePanel en el hilo de despacho de eventos de Swing.
     *
     * @param paramCell La celda a pintar.
     * @param paramCellState El nuevo estado visual de la celda.
     */
    private void paintCellInvokeLater(Cell paramCell, CellState paramCellState) {
        SwingUtilities.invokeLater(() -> paintCell(paramCell, paramCellState));
    }

    /**
     * Pinta una celda en el MazePanel actualizando el color de su botón asociado.
     *
     * @param paramCell La celda a pintar.
     * @param paramCellState El estado de la celda que determina el color.
     */
    private void paintCell(Cell paramCell, CellState paramCellState) {
        JButton jButton = this.mazePanel.getButton(paramCell.row, paramCell.col);
        jButton.setBackground(COLOR_MAP.getOrDefault(paramCellState, Color.WHITE)); // Establece el color de fondo.
    }

    /**
     * Anima la visualización de las celdas visitadas y el camino encontrado en el laberinto.
     *
     * @param paramList1 La lista de celdas visitadas por el algoritmo.
     * @param paramList2 La lista de celdas que forman el camino de la solución.
     */
    private void animarVisitadas(List<Cell> paramList1, List<Cell> paramList2) {
        (new Thread(() -> { // Ejecuta la animación en un nuevo hilo para no bloquear la interfaz.
            for (Cell cell : paramList1) { // Anima las celdas visitadas.
                if (cell.state == CellState.EMPTY)
                    paintCellInvokeLater(cell, CellState.EMPTY); // Pinta solo celdas vacías como visitadas.
                try {
                    Thread.sleep(30L); // Pausa para la animación.
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            if (paramList2.isEmpty()) { // Si no hay camino, no anima nada más.
                SwingUtilities.invokeLater(() -> {});
                return;
            }
            for (Cell cell : paramList2) { // Anima el camino encontrado.
                if (cell.state != CellState.START && cell.state != CellState.END)
                    paintCellInvokeLater(cell, CellState.PATH); // Pinta las celdas del camino (excepto inicio/fin).
                try {
                    Thread.sleep(80L); // Pausa para la animación del camino.
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        })).start(); // Inicia el hilo de animación.
    }

    /**
     * Solicita al usuario las dimensiones (filas y columnas) para un nuevo laberinto.
     *
     * @return Un arreglo de enteros con [filas, columnas] si son válidos, o null en caso contrario.
     */
    public int[] solicitarDimensiones() {
        try {
            String str1 = JOptionPane.showInputDialog("Ingrese numero de filas 🙂:");
            if (str1 == null)
                return null; // Si el usuario cancela.
            String str2 = JOptionPane.showInputDialog("Ingrese numero de columnas 🙂:");
            if (str2 == null)
                return null; // Si el usuario cancela.
            int i = Integer.parseInt(str1.trim()); // Convierte la entrada de filas a entero.
            int j = Integer.parseInt(str2.trim()); // Convierte la entrada de columnas a entero.
            if (i <= 4 || j <= 4) { // Valida que las dimensiones sean mayores a 4.
                JOptionPane.showMessageDialog(null, "Debe ingresar valores mayores a 4 😁.");
                return null;
            }
            return new int[] { i, j }; // Retorna las dimensiones válidas.
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(null, "Debe ingresar numero valido 😁" ); // Mensaje de error si la entrada no es un número.
            return null;
        }
    }

    /**
     * Reinicia el laberinto, solicitando nuevas dimensiones al usuario y creando un nuevo MazeFrame.
     */
    private void reiniciarLaberinto() {
        int[] arrayOfInt = solicitarDimensiones(); // Solicita nuevas dimensiones.
        if (arrayOfInt == null)
            return; // Si el usuario cancela o las dimensiones son inválidas, no hace nada.
        dispose(); // Cierra la ventana actual.
        // Crea una nueva instancia de MazeFrame en el hilo de despacho de eventos de Swing.
        SwingUtilities.invokeLater(() -> new MazeFrame(arrayOfInt[0], arrayOfInt[1]));
    }

    /**
     * Muestra un cuadro de diálogo "Acerca de" con información sobre los desarrolladores y un enlace a GitHub.
     */
    private void mostrarAcercaDe() {
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new BoxLayout(jPanel1, 1)); // Usa BoxLayout para organizar verticalmente.
        JLabel jLabel1 = new JLabel("Desarrollado por: Moises Piguave , Cristian Moscoso , Sebastian Calderon , Pablo Feijon");
        jLabel1.setAlignmentX(0.0F); // Alinea el texto a la izquierda.
        jPanel1.add(jLabel1);
        jPanel1.add(Box.createVerticalStrut(10)); // Espacio vertical.

        JPanel jPanel2 = new JPanel(new FlowLayout(0)); // Panel para el enlace de GitHub.
        JLabel jLabel2 = new JLabel("<html><a href=''>MoisesPiguave/ProyectoFinalDeEstructuraDeDatos</a></html>"); // Enlace HTML.
        jLabel2.setCursor(Cursor.getPredefinedCursor(12)); // Cambia el cursor a mano al pasar por encima.
        // Agrega un MouseListener para abrir la URL en el navegador al hacer clic.
        jLabel2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent param1MouseEvent) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/MoisesPiguave/ProyectoFinalDeEstructuraDeDatos"));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        jPanel2.add(jLabel2);
        jPanel2.setAlignmentX(0.0F);
        jPanel1.add(jPanel2);

        JOptionPane.showMessageDialog(this, jPanel1, "Acerca de", 1); // Muestra el diálogo "Acerca de".
    }

    /**
     * Obtiene el MazePanel asociado a este frame.
     *
     * @return El objeto MazePanel.
     */
    public MazePanel getMazePanel() {
        return this.mazePanel;
    }
}