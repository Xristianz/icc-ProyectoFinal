package ec.edu.ups.Estructura.vista;
import ec.edu.ups.dao.AlgorithmResultDAO;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import ec.edu.ups.Estructura.models.AlgorithmResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Clase ResultadosDialog
 *
 * Esta clase representa un cuadro de diálogo (JDialog) que muestra los resultados de la ejecución de algoritmos.
 * Permite visualizar los datos en una tabla y también graficarlos utilizando JFreeChart.
 *
 * @author Moises Piguave
 * @since 7/28/2025
 */
public class ResultadosDialog extends JDialog {
    /**
     * Modelo de tabla para almacenar y mostrar los resultados de los algoritmos.
     */
    private final DefaultTableModel model;

    /**
     * Objeto DAO (Data Access Object) para interactuar con los resultados de los algoritmos en la base de datos.
     */
    private final AlgorithmResultDAO resultDAO;

    /**
     * Lista para almacenar los resultados de los algoritmos recuperados de la base de datos.
     */
    private List<AlgorithmResult> results;

    /**
     * Constructor de la clase ResultadosDialog.
     *
     * @param paramJFrame El marco principal de la aplicación.
     * @param paramAlgorithmResultDAO Objeto DAO para acceder a los resultados de los algoritmos.
     */
    public ResultadosDialog(JFrame paramJFrame, AlgorithmResultDAO paramAlgorithmResultDAO) {
        super(paramJFrame, "Resultados Guardados", true);
        this.resultDAO = paramAlgorithmResultDAO;
        setLayout(new BorderLayout());
        // Inicializa el modelo de la tabla con las columnas "Algoritmo", "Celdas Camino" y "Tiempo (ns)".
        this.model = new DefaultTableModel((Object[])new String[] { "Algoritmo", "Celdas Camino", "Tiempo (ns)" }, 0);
        JTable jTable = new JTable(this.model);
        JScrollPane jScrollPane = new JScrollPane(jTable);
        add(jScrollPane, "Center");
        // Carga los datos existentes en la tabla al iniciar el diálogo.
        loadData();
        JPanel jPanel = new JPanel();
        JButton jButton1 = new JButton("Limpiar Resultados");
        // Agrega un ActionListener al botón "Limpiar Resultados" para borrar todos los datos.
        jButton1.addActionListener(paramActionEvent -> {
            int i = JOptionPane.showConfirmDialog(this, "¿Deseas borrar todos los resultados?", "Confirmar", 0);
            if (i == 0) {
                paramAlgorithmResultDAO.clear(); // Llama al método clear del DAO para borrar los resultados.
                this.model.setRowCount(0); // Limpia las filas del modelo de la tabla.
            }
        });
        JButton jButton2 = new JButton("Graficar Resultados");
        // Agrega un ActionListener al botón "Graficar Resultados" para mostrar un gráfico de los tiempos.
        jButton2.addActionListener(paramActionEvent -> mostrarGrafica());
        jPanel.add(jButton1);
        jPanel.add(jButton2);
        add(jPanel, "South");
        setSize(500, 400);
        setLocationRelativeTo(paramJFrame); // Centra el diálogo en relación con el marco principal.
    }

    /**
     * Carga los resultados de los algoritmos desde el DAO y los agrega al modelo de la tabla.
     */
    private void loadData() {
        this.results = this.resultDAO.findAll(); // Obtiene todos los resultados del DAO.
        for (AlgorithmResult algorithmResult : this.results) {
            // Agrega cada resultado como una nueva fila en el modelo de la tabla.
            this.model.addRow(new Object[] {
                    algorithmResult.getAlgorithmName(),
                    Integer.valueOf(algorithmResult.getPathSize()),
                    Long.valueOf(algorithmResult.getTimeNs())
            });
        }
    }

    /**
     * Muestra un gráfico de líneas de los tiempos de ejecución por algoritmo utilizando JFreeChart.
     */
    private void mostrarGrafica() {
        if (this.results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos para graficar.");
            return;
        }
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        // Llena el dataset con los tiempos de ejecución para cada algoritmo.
        for (AlgorithmResult algorithmResult : this.results)
            defaultCategoryDataset.addValue(algorithmResult.getTimeNs(), "Tiempo(ns)", algorithmResult.getAlgorithmName());

        // Crea un gráfico de líneas con los datos.
        JFreeChart jFreeChart = ChartFactory.createLineChart("Tiempos de Ejecución por Algoritmo", "Algoritmo", "Tiempo (ns)", (CategoryDataset)defaultCategoryDataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        JDialog jDialog = new JDialog(this, "Gráficos", true);
        jDialog.setContentPane(chartPanel); // Establece el panel del gráfico como contenido del diálogo.
        jDialog.setSize(600, 400);
        jDialog.setLocationRelativeTo(this); // Centra el diálogo del gráfico.
        jDialog.setVisible(true); // Hace visible el diálogo del gráfico.
    }
}