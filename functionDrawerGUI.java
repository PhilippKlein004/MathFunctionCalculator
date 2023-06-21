import javax.swing.*;
import java.awt.*;

/**
 * @author Philipp Klein
 * @version 1.0.1
 * @since 11.21.2022
 */

public class functionDrawerGUI extends JFrame {

    functionCalculator f;

    // Standard properties of the window

    public functionDrawerGUI(String title, functionCalculator f) {

        this.f = f;
        this.setTitle(title);
        this.setSize(500,500);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void paint(Graphics g) {

        // Create a new pen
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(3));

        // x-axis
        g2D.drawLine(0,250,500,250);

        // y-axis
        g2D.drawLine(250,0,250,500);

        // Set pen stroke
        g2D.setStroke(new BasicStroke(1));

        // Vertical Lines
        for( int i = 10 ; i < 500 ; i += 10 ) {
            g2D.drawLine(500-i,0,500-i,500);
        }

        // Horizontal Lines
        for( int i = 10 ; i < 500 ; i += 10 ) {
            g2D.drawLine(0,500-i,500,500-i);
        }

        // Drawing the function : one block = 10 pixels
        // Per section : 25 blocks ≈ 250 pixels
        g2D.setStroke(new BasicStroke(2));
        g2D.setColor(Color.blue);
        int y1, y2;
        for( int i = 0 ; i != 500 ; i += 10 ) {
            y1 = (int) f.calculate(Double.parseDouble("" + (-25 + i/10)));
            y2 = (int) f.calculate(Double.parseDouble("" + (-25 + (i+10)/10)));
            g2D.drawLine(i,250 - y1*10,i+10,250 - y2*10);
        }
        System.out.println("Drawing of " + f + " finished");



    }


}
