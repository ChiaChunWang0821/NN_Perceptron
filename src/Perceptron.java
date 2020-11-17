import java.util.ArrayList;
import java.util.HashMap;

public class Perceptron {

    protected HashMap<Integer, Boolean> label = new HashMap<>();
    protected ArrayList<String> input = new ArrayList<>();

    private int trainingSet;
    private int testingSet;

    private double threshold;
    private double learningRate;
    private int maxLearningCycle;
    private int dimension;
    private double trainingRecognitionRate;
    private double testingRecognitionRate;

    protected ArrayList<double[]> trainingX = new ArrayList<>();
    protected ArrayList<double[]> testingX = new ArrayList<>();
    protected ArrayList<Double> trainingD = new ArrayList<>();
    protected ArrayList<Double> testingD = new ArrayList<>();
    protected ArrayList<Double> d = new ArrayList<>();
    protected ArrayList<Double> w = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();

    public Perceptron(){
    }

    protected void setJFrame(){
        PerceptronJFrame perceptronJFrame = new PerceptronJFrame(this);
    }

    protected void start(){
        if(d.size() <= 2){
            training(d.get(0));
        }
        else{
            d.forEach(this::training);
        }
    }

    private void randomizationWeight(){
        w.clear();
        w.add(threshold);
        for(int i = 1; i <= dimension; i++){
            w.add(Math.random() - 0.5);
        }
    }

    private void training(double currentClass) {
        if (trainingX.size() == 0){
            return;
        }

        randomizationWeight();

        int cycle = 0;
        int correct = 0;

        while (cycle < maxLearningCycle) {
            correct = 0;

            for(int i = 0; i < trainingX.size(); i++){
                double v = 0;
                for(int j = 0; j <= dimension; j++){
                    v += w.get(j) * trainingX.get(i)[j];
                }

                double equal;
                if(trainingD.get(i).equals(currentClass)){
                    if(Math.signum(v) > 0){
                        correct++;
                        equal = 0;
                    }
                    else {
                        equal = 1;
                    }
                }
                else {
                    if(Math.signum(v) < 0){
                        correct++;
                        equal = 0;
                    }
                    else {
                        equal = -1;
                    }
                }

                for(int k = 0; k <= dimension; k++){
                    w.set(k, w.get(k) + (equal * learningRate * trainingX.get(i)[k]));
                }
            }

            if (correct == trainingX.size()) {
                break;
            }

            cycle++;
        }

        trainingRecognitionRate = (double) correct / trainingX.size() * 100;

        testing(currentClass);
    }

    private void testing(double currentClass) {
        if (testingX.size() == 0){
            return;
        }

        int correct = 0;

        for(int i = 0; i < testingX.size(); i++){
            double v = 0;

            for(int j = 0; j <= dimension; j++){
                v += w.get(j) * testingX.get(i)[j];
            }

            if(testingD.get(i).equals(currentClass)){
                if(Math.signum(v) > 0){
                    correct++;
                }
            }
            else {
                if(Math.signum(v) < 0){
                    correct++;
                }
            }
        }

        testingRecognitionRate = (double) correct / testingX.size() * 100;
    }

    protected void clear(){
        trainingSet = 0;
        testingSet = 0;
        threshold = 0;
        learningRate = 0.1;
        maxLearningCycle = 500;
        dimension = 0;
        trainingRecognitionRate = 0;
        testingRecognitionRate = 0;

        label.clear();
        input.clear();
        trainingX.clear();
        testingX.clear();
        trainingD.clear();
        testingD.clear();
        d.clear();
        w.clear();
        y.clear();
    }

    protected void setThreshold(double mThreshold){
        threshold = mThreshold;
    }

    protected void setLearningRate(double mLearningRate){
        learningRate = mLearningRate;
    }

    protected void setMaxLearningCycle(int mMaxLearningCycle){
        maxLearningCycle = mMaxLearningCycle;
    }

    protected int getMaxLearningCycle(){
        return maxLearningCycle;
    }

    protected void setDimension(String data){
        String[] splitData = data.split("\\s+");
        dimension = splitData.length - 1;
    }

    protected int getDimension(){
        return dimension;
    }

    protected void setTrainingSet(int mTrainingSet){
        trainingSet = mTrainingSet;
    }

    protected int getTrainingSet(){
        return trainingSet;
    }

    protected void setTestingSet(int mTestingSet){
        testingSet = mTestingSet;
    }

    protected int getTestingSet(){
        return testingSet;
    }

    protected double getTrainingRecognitionRate(){
        return trainingRecognitionRate;
    }

    protected double getTestingRecognitionRate(){
        return testingRecognitionRate;
    }
}
