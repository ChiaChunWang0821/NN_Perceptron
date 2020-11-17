import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;

public class PerceptronJFrame{

    private Perceptron perceptron;

    private int windowWidth;
    private int windowHeight;
    private int drawPanelSize; // width
    private int scaleRatio = 50;

    private Color lineColor = Color.RED;
    private Color[] pointColor = {Color.MAGENTA, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN};

    private JFrame perceptronFrame;
    private JPanel perceptronPanel;
    private JPanel controlPanel;
    private JPanel drawPanel;
    private JButton loadFileButton;
    private JButton showResultButton;
    private JButton largeButton;
    private JButton smallButton;
    private JTextField thresholdTextField;
    private JTextField learningRateTextField;
    private JTextField maxLearningCycleTextField;
    private JLabel filePathLabel;
    private JLabel thresholdLabel;
    private JLabel learningRateLabel;
    private JLabel maxLearningCycleLabel;
    private JLabel dimensionLabel;
    private JLabel setLabel;
    private JLabel modifieldThresholdLabel;
    private JLabel weightLabel;
    private JLabel trainingRateLabel;
    private JLabel testingRateLabel;
    private JLabel dimensionValueLabel;
    private JLabel setValueLabel;
    private JLabel modifiedThresholdValueLabel;
    private JLabel weightValueLabel;
    private JLabel trainingRateValueLabel;
    private JLabel testingRateValueLabel;
    private JPanel showPanel;

    public PerceptronJFrame(Perceptron mPerceptron){
        perceptron = mPerceptron;

        windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        perceptronFrame = new JFrame("Perceptron");
        perceptronFrame.setContentPane(perceptronPanel);
        perceptronFrame.setVisible(true);
        perceptronFrame.setSize(windowWidth - 100, windowHeight - 100);
        perceptronFrame.setLocationRelativeTo(null);
        perceptronFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        actionListener();
    }

    private void actionListener(){
        loadFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Text Files(*.txt)", "txt", "text");
            jFileChooser.setFileFilter(fileNameExtensionFilter);

            if(jFileChooser.showOpenDialog(perceptronFrame) == JFileChooser.APPROVE_OPTION){
                File file = jFileChooser.getSelectedFile();

                filePathLabel.setForeground(null);
                filePathLabel.setText(file.getPath());

                perceptron = DataPreprocessor.spliteData(DataPreprocessor.readFile(perceptron, file));

                dimensionValueLabel.setText(Integer.toString(perceptron.getDimension()));
                setValueLabel.setText(perceptron.getTrainingSet() + "/ " + perceptron.getTestingSet());
            }

            if(perceptron.getTrainingSet() != 0){
                showResultButton.setEnabled(true);

                if(perceptron.getTrainingSet() > perceptron.getMaxLearningCycle()){
                    perceptron.setMaxLearningCycle(perceptron.getTrainingSet() * 2);
                    maxLearningCycleTextField.setText(Integer.toString(perceptron.getMaxLearningCycle()));
                }

                drawPanel.removeAll();
                drawPanel.repaint();
            }
        });

        showResultButton.addActionListener(e -> {
            perceptron.start();

            StringBuilder weightString = new StringBuilder("(");
            weightString.append(perceptron.w.get(1));
            for (int i = 2; i < perceptron.w.size(); i++) {
                weightString.append(", ").append(perceptron.w.get(i));
            }
            weightString.append(")");
            weightValueLabel.setText(weightString.toString());

            modifiedThresholdValueLabel.setText(perceptron.w.get(0).toString());
            trainingRateValueLabel.setText(perceptron.getTrainingRecognitionRate() + "%");
            testingRateValueLabel.setText(perceptron.getTestingRecognitionRate() + "%");

            drawPanel.removeAll();
            drawPanel.repaint();
        });

        largeButton.addActionListener(e -> {
            if(scaleRatio < 100){
                scaleRatio += 10;

                drawPanel.removeAll();
                drawPanel.repaint();
            }
        });

        smallButton.addActionListener(e -> {
            if(scaleRatio > 10){
                scaleRatio -= 10;

                drawPanel.removeAll();
                drawPanel.repaint();
            }
        });

        thresholdTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeThreshold();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeThreshold();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeThreshold();
            }

            private void changeThreshold(){
                try {
                    thresholdTextField.setBackground(Color.white);
                    perceptron.setThreshold(Double.valueOf(thresholdTextField.getText()));
                } catch (NumberFormatException e){
                    thresholdTextField.setBackground(Color.pink);
                    perceptron.setThreshold(0);
                }
            }
        });

        learningRateTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeLearningRate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeLearningRate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeLearningRate();
            }

            private void changeLearningRate(){
                try {
                    learningRateTextField.setBackground(Color.white);
                    perceptron.setLearningRate(Double.valueOf(learningRateTextField.getText()));
                } catch (NumberFormatException e){
                    learningRateTextField.setBackground(Color.pink);
                    perceptron.setLearningRate(0.1);
                }
            }
        });

        maxLearningCycleTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeMaxLearningCycle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeMaxLearningCycle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeMaxLearningCycle();
            }

            private void changeMaxLearningCycle(){
                try {
                    maxLearningCycleTextField.setBackground(Color.white);
                    perceptron.setMaxLearningCycle(Integer.valueOf(maxLearningCycleTextField.getText()));
                } catch (NumberFormatException e){
                    maxLearningCycleTextField.setBackground(Color.pink);
                    perceptron.setMaxLearningCycle(500);
                }
            }
        });
    }

    private void createUIComponents() {
        drawPanel = new GridPanel();
    }

    private class GridPanel extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            drawPanelSize = drawPanel.getSize().width;

            g.drawLine(drawPanelSize / 2, 0, drawPanelSize / 2, drawPanelSize);
            g.drawLine(0, drawPanelSize / 2, drawPanelSize, drawPanelSize / 2);

            Graphics2D g2 = (Graphics2D) g;

            // draw grid
            for (double i = (drawPanelSize / 2d); i >= 0; i -= 5.0 * scaleRatio / 10) {
                drawGrid(g2, i);
            }
            for (double i = (drawPanelSize / 2d); i <= drawPanelSize; i += 5.0 * scaleRatio / 10) {
                drawGrid(g2, i);
            }

            // draw training point
            if (perceptron.trainingX.size() > 0 && perceptron.trainingX.get(0).length == 3) {
                drawPoint(g2, perceptron.trainingX, perceptron.trainingD);
            }

            // draw testing point
            if (perceptron.testingX.size() > 0 && perceptron.testingX.get(0).length == 3) {
                drawPoint(g2, perceptron.testingX, perceptron.testingD);
            }

            // draw line
            if (perceptron.w.size() != 0 && perceptron.testingX.get(0).length == 3) {
                drawLine(g2, perceptron.w);
            }
        }

        private void drawGrid(Graphics2D g2, double i) {
            g2.setStroke(new BasicStroke(1));

            double[] top, btn;
            double scaleLength = (i % (5.0 * scaleRatio / 5) == 0) ? 2.0 * scaleRatio / 20 : 1.0 * scaleRatio / 20;
            top = convertCoordinate(new double[]{(i - (drawPanelSize / 2)) / scaleRatio, scaleLength / scaleRatio});
            btn = convertCoordinate(new double[]{(i - (drawPanelSize / 2)) / scaleRatio, -scaleLength / scaleRatio});
            g2.draw(new Line2D.Double(top[0], top[1], btn[0], btn[1]));
            top = convertCoordinate(new double[]{-scaleLength / scaleRatio, ((drawPanelSize / 2) - i) / scaleRatio});
            btn = convertCoordinate(new double[]{scaleLength / scaleRatio, ((drawPanelSize / 2) - i) / scaleRatio});
            g2.draw(new Line2D.Double(top[0], top[1], btn[0], btn[1]));
        }

        private void drawPoint(Graphics2D g2, ArrayList<double[]> data, ArrayList<Double> output){
            g2.setStroke(new BasicStroke(3));

            for(int i = 0; i < data.size(); i++){
                double[] x = data.get(i);
                double[] point = convertCoordinate(new double[]{x[1], x[2]});
                g2.setColor(pointColor[(int)Math.round(output.get(i))]);
                g2.draw(new Line2D.Double(point[0], point[1], point[0], point[1]));
            }
        }

        private void drawLine(Graphics2D g2, ArrayList<Double> points){
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(2));

            double[] lineStart, lineEnd;
            if (points.get(2) != 0) {
                lineStart = convertCoordinate(new double[]{-(double) drawPanelSize / scaleRatio, (points.get(0) + (double) drawPanelSize / scaleRatio * points.get(1)) / points.get(2)});
                lineEnd = convertCoordinate(new double[]{(double) drawPanelSize / scaleRatio, (points.get(0) - (double) drawPanelSize / scaleRatio * points.get(1)) / points.get(2)});
            } else {
                lineStart = convertCoordinate(new double[]{perceptron.w.get(0) / points.get(1), (double) drawPanelSize / scaleRatio});
                lineEnd = convertCoordinate(new double[]{perceptron.w.get(0) / points.get(1), -(double) drawPanelSize / scaleRatio});
            }
            g2.draw(new Line2D.Double(lineStart[0], lineStart[1], lineEnd[0], lineEnd[1]));
        }

        private double[] convertCoordinate(double[] beforePoint) {
            double[] afterPoint = new double[2];
            afterPoint[0] = (beforePoint[0] * scaleRatio) + (drawPanelSize / 2);
            afterPoint[1] = (drawPanelSize / 2) - (beforePoint[1] * scaleRatio);
            return afterPoint;
        }
    }

}
