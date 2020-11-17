import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;

public class DataPreprocessor {

    protected static Perceptron readFile(Perceptron perceptron, File file){
        perceptron.clear();

        int inputSet = 0;

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                perceptron.label.put(inputSet++, true);

                String tmp = scanner.nextLine();
                perceptron.input.add(tmp);

                perceptron.setDimension(tmp);
            }

            perceptron.setTrainingSet((int) Math.round((Double.valueOf(inputSet) * 2d) / 3d));
            perceptron.setTestingSet(inputSet - perceptron.getTrainingSet());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return perceptron;
    }

    protected static Perceptron spliteData(Perceptron perceptron){
        setTrainingData(perceptron);
        setTestingData(perceptron);

        return perceptron;
    }

    private static void setTrainingData(Perceptron perceptron){
        int trainingSet = perceptron.getTrainingSet();
        int testingSet = perceptron.getTestingSet();
        int dimension = perceptron.getDimension();

        perceptron.trainingX.clear();
        perceptron.trainingD.clear();
        perceptron.d.clear();

        for(int i = 0; i < trainingSet; i++){
            int random = (int) (Math.random() * (trainingSet + testingSet));
            while (!perceptron.label.get(random)){
                random = (int) (Math.random() * (trainingSet + testingSet));
            }
            perceptron.label.put(random, false);

            String[] numbers = perceptron.input.get(random).split("\\s+");

            double[] x_tmp = new double[dimension + 1];
            x_tmp[0] = -1;
            for(int j = 1; j < x_tmp.length; j++){
                x_tmp[j] = Double.parseDouble(numbers[j - 1]);
            }
            perceptron.trainingX.add(x_tmp);

            perceptron.trainingD.add(Double.parseDouble(numbers[dimension]));

            if(!perceptron.d.contains(perceptron.trainingD.get(i))){
                perceptron.d.add(perceptron.trainingD.get(i));
            }
        }

        Collections.sort(perceptron.d);
    }

    private static void setTestingData(Perceptron perceptron){
        int testingSet = perceptron.getTestingSet();
        int dimension = perceptron.getDimension();

        perceptron.testingX.clear();
        perceptron.testingD.clear();

        for(int i = 0; i < testingSet; i++){
            String string = null;
            for(int k = 0; k < perceptron.label.size(); k++){
                if(perceptron.label.get(k)){
                    string = perceptron.input.get(k);
                    perceptron.label.put(k, false);
                    break;
                }
            }
            String[] numbers = string.split("\\s+");

            double[] x_tmp = new double[dimension + 1];
            x_tmp[0] = -1;
            for(int j = 1; j < x_tmp.length; j++){
                x_tmp[j] = Double.parseDouble(numbers[j - 1]);
            }
            perceptron.testingX.add(x_tmp);

            perceptron.testingD.add(Double.parseDouble(numbers[dimension]));
        }
    }
}
