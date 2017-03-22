package pl.kasprowski;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorFrame extends JFrame {
    JPanel mainPanel, buttonPanel;
    JButton button;
    JLabel numLabel1, numLabel2, questionLabel;
    JTextField textField1, textField2;
    JCheckBox currencyBox, commaBox;
    JRadioButton addButton, subtractButton, multiplyButton, divisionButton;
    ButtonGroup buttonGroup;
    JSlider slider;

    public CalculatorFrame() {
        this.setSize(400, 300);
        this.setLocation();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mainPanel = new JPanel();

        button = new JButton("Oblicz");
        button.setPreferredSize(new Dimension(70, 30));
        button.addActionListener(new ButtonListener());
        mainPanel.add(button);

        numLabel1 = new JLabel("Liczba 1");
        mainPanel.add(numLabel1);
        textField1 = new JTextField(6);
        textField1.setToolTipText("Podaj liczbę całkowitą!");
        mainPanel.add(textField1);

        numLabel2 = new JLabel("Liczba 2");
        mainPanel.add(numLabel2);
        textField2 = new JTextField(6);
        textField2.setToolTipText("Podaj liczbę całkowitą!");
        mainPanel.add(textField2);

        currencyBox = new JCheckBox("Waluta");
        mainPanel.add(currencyBox);
        commaBox = new JCheckBox("Przecinki");
        mainPanel.add(commaBox);

        buttonPanel = new JPanel();
        buttonPanel.setBorder(new TitledBorder("Typ operacji"));
        addButton = new JRadioButton("Dodawanie");
        addButton.setActionCommand("add");
        addButton.setSelected(true);
        subtractButton = new JRadioButton("Odejmowanie");
        subtractButton.setActionCommand("subtract");
        multiplyButton = new JRadioButton("Mnożenie");
        multiplyButton.setActionCommand("multiply");
        divisionButton = new JRadioButton("Dzielenie");
        divisionButton.setActionCommand("division");
        buttonGroup = new ButtonGroup();
        buttonGroup.add(addButton);
        buttonGroup.add(subtractButton);
        buttonGroup.add(multiplyButton);
        buttonGroup.add(divisionButton);
        buttonPanel.add(addButton);
        buttonPanel.add(subtractButton);
        buttonPanel.add(multiplyButton);
        buttonPanel.add(divisionButton);
        mainPanel.add(buttonPanel);

        questionLabel = new JLabel("Ile razy wykonać działanie?");
        mainPanel.add(questionLabel);

        slider = new JSlider(1, 10, 1);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(4);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        mainPanel.add(slider);

        this.add(mainPanel);
        this.setVisible(true);
        textField1.requestFocus();
    }

    private void setLocation() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int locationY = dimension.width / 2 - this.getWidth() / 2;
        int locationX = dimension.height / 2 - this.getHeight() / 2;
        this.setLocation(locationY, locationX);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button) {
                String operationType = getOperationType();
                double[] values = getValues();
                if (validateInputNumbers(values)) {
                    int iterations = slider.getValue();
                    double result = calculateLoop(operationType, values, iterations);
                    showResult(result);
                }
            }
        }
    }

    private double calculateLoop(String operationType, double[] values, int iterations) {
        double result = 0.0;
        for (int i = 0; i < iterations; i++) {
            result = calculate(operationType, values);
            values[0] = result;
        }
        return result;
    }

    private double calculate(String operationType, double[] values) {
        switch (operationType) {
            case "add":
                return values[0] + values[1];
            case "subtract":
                return values[0] - values[1];
            case "multiply":
                return values[0] * values[1];
            case "division":
                return values[0] / values[1];
            default:
                return 0.0;
        }
    }

    private double[] getValues() {
        double[] values = null;
        String textNum1 = textField1.getText();
        String textNum2 = textField2.getText();

        try {
            double num1 = Double.parseDouble(textNum1);
            double num2 = Double.parseDouble(textNum2);
            values = new double[2];
            values[0] = num1;
            values[1] = num2;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return values;
    }

    private String getOperationType() {
        return buttonGroup.getSelection().getActionCommand();
    }

    private boolean validateInputNumbers(double[] values) {
        if (values == null) {
            JOptionPane.showMessageDialog(button, "Błędne dane!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void showResult(double result) {
        if (result == Double.POSITIVE_INFINITY) {
            JOptionPane.showMessageDialog(button, "Nie dziel przez 0! ", "Wynik", JOptionPane.ERROR_MESSAGE);
        } else {
            StringBuffer sb = new StringBuffer("Wynik działania ");

            if(commaBox.isSelected()){
                String[] splitedNumber = String.valueOf(result).split("\\.");
                sb.append(splitedNumber[0] + "," + splitedNumber[1]);
            }else{
                sb.append(result);
            }
            if(currencyBox.isSelected()){
                sb.append(" PLN");
            }

            JOptionPane.showMessageDialog(button, sb.toString(), "Wynik", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
