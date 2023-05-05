import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

public class BigramTableImage {

    private static final int CELL_SIZE = 30;
    private static final int FONT_SIZE = 14;
    private static final int PADDING = 10;

    public static void visualize(Map<String, Double> bigramTable, String outputPath) {
        int numRows = 26;
        int numCols = 26;

        // Create the image
        BufferedImage image = new BufferedImage(numCols * CELL_SIZE + PADDING * 2, numRows * CELL_SIZE + PADDING * 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Set the background color
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Draw the table
        g.setColor(Color.BLACK);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
        g.setFont(font);

        int x, y;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                String bigram = Character.toString((char) (i + 65)) + Character.toString((char) (j + 65));
                Double probability = bigramTable.getOrDefault(bigram, 0.0);

                x = PADDING + j * CELL_SIZE;
                y = PADDING + i * CELL_SIZE;

                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                g.drawString(String.format("%.2f", probability), x + CELL_SIZE / 2 - FONT_SIZE, y + CELL_SIZE / 2 + FONT_SIZE / 2);
            }
        }

        // Save the image
        try {
            File output = new File(outputPath);
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clean up
        g.dispose();
    }

}
