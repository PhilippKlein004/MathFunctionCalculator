import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InputMismatchException;

/**
 * @author Philipp Klein
 * @version 1.0
 * @since 10.26.2022
 */

public class functionCalculatorGUI extends JFrame {

    public functionCalculatorGUI() {

        // Add new JButtons here :

        JButton button = new JButton("Calculate");
        button.setBounds(150,300,100,50);

        JButton draw = new JButton("Draw");
        draw.setBounds(250,300,100,50);

        // Add new JTextFields here :

        JTextField input = new JTextField("Type...");
        input.setBounds(175,200,200,25);

        JTextField xInput = new JTextField();
        xInput.setBounds(245,250,30,25);

        // Add new JLabels here :

        JLabel version = new JLabel("Ver. 2.0.1");
        version.setBounds(4,0,80,20);

        JLabel error = new JLabel();
        error.setForeground(Color.RED);
        error.setVisible(false);

        JLabel copyright = new JLabel("Copyright (c) Philipp Klein");
        copyright.setBounds(170,440,170,20);

        JLabel function = new JLabel("f(x) =");
        function.setBounds(140,200,40,25);

        JLabel x = new JLabel("x =");
        x.setBounds(225,250,22,25);

        JLabel result = new JLabel();
        result.setBounds(220,380,200,20);

        // Add new ActionListeners here :

        draw.addActionListener(new ActionListener() {
            // Drawing button pushed
            public void actionPerformed(ActionEvent e) {
                new functionDrawerGUI("Drawing: " + input.getText(),new functionCalculator(input.getText()));
            }
        }
        );

        button.addActionListener(new ActionListener() {
            // Calculate button pushed
            public void actionPerformed(ActionEvent e) {
                System.out.println("f(x) = " + input.getText() + " for x = " + xInput.getText());
                button.setText("Calculates...");
                input.setEditable(false);
                xInput.setEditable(false);

                try {
                    if ( xInput.getText().replaceAll("\\s+","").equals("") ) {
                        // If the x input is empty
                        error.setBounds(175,225,400,25);
                        throw new InputMismatchException("Variable x can't be empty!");
                    } else if ( input.getText().replaceAll("\\s+","").equals("") ) {
                        // If the function input is empty
                        error.setBounds(70,225,400,25);
                        throw new InputMismatchException("The given function field can't be empty! Please try again.");
                    } else if ( (!Character.isDigit(xInput.getText().replaceAll("\\s+","").charAt(0)) && xInput.getText().charAt(0) != '-' ) || xInput.getText().matches(".*[a-z].*") ) {
                        // If the x is not a digit
                        error.setBounds(175,225,400,25);
                        throw new InputMismatchException("Variable x must be a digit!");
                    }
                    result.setText("f(" + xInput.getText() + ") = " + new functionCalculator(input.getText()).calculate(Double.parseDouble(xInput.getText())));
                    error.setVisible(false);
                } catch (InputMismatchException i) {
                    error.setText(i.getMessage());
                    error.setVisible(true);
                } catch (NumberFormatException i) {
                    // All alternate errors
                    error.setText("The format of the function is not correct! Please try again.");
                    error.setBounds(70,225,400,25);
                    error.setVisible(true);
                }

                button.setText("Calculate");
                input.setEditable(true);
                xInput.setEditable(true);
            }
        }
        );

        // Customize the Frame or add Components here :

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Function calculator");
        this.setSize(500,500);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Add new Buttons, TextField here :

        this.add(copyright);
        this.add(button);
        this.add(draw);
        this.add(input);
        this.add(function);
        this.add(x);
        this.add(version);
        this.add(xInput);
        this.add(result);
        this.add(error);

    }

}
