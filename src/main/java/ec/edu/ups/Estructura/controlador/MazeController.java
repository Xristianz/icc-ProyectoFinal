package ec.edu.ups.Estructura.controlador;
import java.awt.Color;
import javax.swing.JButton;

import ec.edu.ups.Estructura.models.Cell;
//import ec.edu.ups.models.CellState;
import ec.edu.ups.vista.MazePanel;

public class MazeController {
    private final MazePanel panel;

    private Cell startCell;

    private Cell endCell;

    private Mode currentMode = Mode.WALL;

    public enum Mode {
        START, END, WALL;
    }

    public MazeController(MazePanel paramMazePanel) {
        this.panel = paramMazePanel;
        paramMazePanel.setController(this);
    }

    public void setMode(Mode paramMode) {
        this.currentMode = paramMode;
    }

    public void onCellClicked(int paramInt1, int paramInt2) {
        switch (this.currentMode) {
            case START:
                setStartCell(paramInt1, paramInt2);
                break;
            case END:
                setEndCell(paramInt1, paramInt2);
                break;
            case WALL:
                toggleWall(paramInt1, paramInt2);
                break;
        }
    }

    public void onCellClickedLegacy(int paramInt1, int paramInt2) {
        Cell cell = this.panel.getCells()[paramInt1][paramInt2];
        JButton jButton = this.panel.getButton(paramInt1, paramInt2);
        switch (this.currentMode) {
            case START:
                if (this.startCell != null)
                    this.panel.getButton(this.startCell.row, this.startCell.col).setBackground(Color.WHITE);
                this.startCell = cell;
                cell.state = CellState.START;
                jButton.setBackground(Color.GREEN);
                break;
            case END:
                if (this.endCell != null)
                    this.panel.getButton(this.endCell.row, this.endCell.col).setBackground(Color.WHITE);
                this.endCell = cell;
                cell.state = CellState.END;
                jButton.setBackground(Color.RED);
                break;
            case WALL:
                if (cell.state == CellState.WALL) {
                    cell.state = CellState.EMPTY;
                    jButton.setBackground(Color.WHITE);
                    break;
                }
                cell.state = CellState.WALL;
                jButton.setBackground(Color.BLACK);
                break;
        }
    }

    public void limpiar() {
        this.panel.limpiarCeldasVisitadas();
    }

    public Cell getStartCell() {
        return this.startCell;
    }

    public Cell getEndCell() {
        return this.endCell;
    }

    public void setEndCell(int paramInt1, int paramInt2) {
        Cell cell = this.panel.getCells()[paramInt1][paramInt2];
        JButton jButton = this.panel.getButton(paramInt1, paramInt2);
        if (this.endCell != null) {
            this.panel.getButton(this.endCell.row, this.endCell.col).setBackground(Color.WHITE);
            this.endCell.state = CellState.EMPTY;
        }
        this.endCell = cell;
        cell.state = CellState.END;
        jButton.setBackground(Color.RED);
    }

    public void setStartCell(int paramInt1, int paramInt2) {
        Cell cell = this.panel.getCells()[paramInt1][paramInt2];
        JButton jButton = this.panel.getButton(paramInt1, paramInt2);
        if (this.startCell != null) {
            this.panel.getButton(this.startCell.row, this.startCell.col).setBackground(Color.WHITE);
            this.startCell.state = CellState.EMPTY;
        }
        this.startCell = cell;
        cell.state = CellState.START;
        jButton.setBackground(Color.GREEN);
    }

    public void toggleWall(int paramInt1, int paramInt2) {
        Cell cell = this.panel.getCells()[paramInt1][paramInt2];
        if (cell.state == CellState.EMPTY) {
            cell.state = CellState.WALL;
            this.panel.getButton(paramInt1, paramInt2).setBackground(Color.BLACK);
        } else if (cell.state == CellState.WALL) {
            cell.state = CellState.EMPTY;
            this.panel.getButton(paramInt1, paramInt2).setBackground(Color.WHITE);
        }
    }
}
