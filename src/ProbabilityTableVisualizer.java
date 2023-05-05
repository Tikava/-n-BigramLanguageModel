import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.PrinterJob;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ProbabilityTableVisualizer {

    private Map<String, Double> probabilityTable;

    public ProbabilityTableVisualizer(Map<String, Double> probabilityTable) {
        this.probabilityTable = probabilityTable;
    }

    public void createTable() {
        // Create JFrame and JPanel
        JFrame frame = new JFrame("Probability Table");
        JPanel panel = new JPanel(new BorderLayout());

        // Create JTable
        DefaultTableModel model = new DefaultTableModel(0, 0);
        JTable table = new JTable(model);

        // Set table header
        String[] headers = new String[] {"First", "Second", "Probability"};
        model.setColumnIdentifiers(headers);

        // Populate table data
        for (Map.Entry<String, Double> entry : probabilityTable.entrySet()) {
            String bigram = entry.getKey();
            String[] row = {Character.toString(bigram.charAt(0)), Character.toString(bigram.charAt(1)), String.format("%f", entry.getValue())};
            model.addRow(row);
        }

        // Set table cell renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        // Set table font
        Font font = new Font("Verdana", Font.PLAIN, 12);
        table.setFont(font);

        // Set table row height
        table.setRowHeight(20);

        // Set table background color
        table.setBackground(Color.white);

        // Add table to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scrollPane to panel
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add panel to frame
        frame.add(panel);

        // Set frame properties
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
