//import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
//
//import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
//
//import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
//
//import org.nd4j.linalg.lossfunctions.LossFunctions;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class NameGenerator {
//
//    private static final int WINDOW_SIZE = 2;
//    private static final int HIDDEN_LAYER_SIZE = 150;
//    private static final double LEARNING_RATE = 0.01;
//    private static final int EPOCHS = 100;
//
//    public static void main(String[] args) {
//
//        // Load data
//        String[] names = new String[]{"Alice", "Bob", "Charlie", "Dave", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy", "Kevin", "Laura", "Mallory", "Nate", "Olivia", "Peggy", "Quentin", "Randy", "Sybil", "Trudy", "Victor", "Wendy", "Xavier", "Yvonne", "Zelda"};
//
//        // Create bigram frequency table
//        Map<String, Double> bigramFrequencyTable = createBigramFrequencyTable(names);
//
//        // Create vocabulary
//        List<Character> vocabulary = createVocabulary(bigramFrequencyTable);
//
//        // Create training data
//        List<List<Integer>> trainingData = createTrainingData(names, vocabulary);
//
//        // Build network configuration
//        NeuralNetConfiguration configuration = new NeuralNetConfiguration.Builder()
//                .seed(12345)
//                .updater(new Adam(LEARNING_RATE))
//                .weightInit(WeightInit.XAVIER)
//                .list()
//                .layer(0, new LSTM.Builder()
//                        .nIn(vocabulary.size())
//                        .nOut(HIDDEN_LAYER_SIZE)
//                        .activation(Activation.TANH)
//                        .build())
//                .layer(1, new LSTM.Builder()
//                        .nIn(HIDDEN_LAYER_SIZE)
//                        .nOut(HIDDEN_LAYER_SIZE)
//                        .activation(Activation.TANH)
//                        .build())
//                .layer(2, new RnnOutputLayer.Builder()
//                        .nIn(HIDDEN_LAYER_SIZE)
//                        .nOut(vocabulary.size())
//                        .lossFunction(LossFunctions.LossFunction.MCXENT)
//                        .activation(Activation.SOFTMAX)
//                        .build())
//                .build();
//
//        // Create model
//        MultiLayerNetwork model = new MultiLayerNetwork(configuration);
//        model.init();
//
//        // Create iterators for training
//        SequenceRecordReader trainData = new InMemorySequenceRecordReader(trainingData);
//        DataSetIterator trainIterator = new SequenceRecordReaderDataSetIterator(trainData, names.length, -1, vocabulary.size(), true);
//
//        // Train model
//        for (int i = 0; i < EPOCHS; i++) {
//            model.fit(trainIterator);
//            trainIterator.reset();
//        }
//
//        // Generate names using the model
//        System.out.println("Generated names:");
//        for (int i = 0; i < 10; i++) {
//            String name = generateName(model, vocabulary);
//            System.out.println(name);
//        }
//
//    }
//
//    private static Map<String, Double> createBigramFrequencyTable(String[] names) {
//        Map<String, Double> frequencyTable = new HashMap<>();
//        for (String name : names) {
//            for (int i = 0; i < name.length() - 1; i++) {
//                String bigram = name.substring(i, i + WINDOW_SIZE);
//                frequencyTable.merge(bigram, 1.0, Double::sum);
//            }
//        }
//        return frequencyTable;
//    }
//
//    private static List<Character> createVocabulary(Map<String, Double> bigramFrequencyTable) {
//        List<Character> vocabulary = new ArrayList<>();
//        for (String bigram : bigramFrequencyTable.keySet()) {
//            char[] chars = bigram.toCharArray();
//            for (char c : chars) {
//                if (!vocabulary.contains(c)) {
//                    vocabulary.add(c);
//                }
//            }
//        }
//        return vocabulary;
//    }
//    private static List<List<Integer>> createTrainingData(String[] names, List<Character> vocabulary) {
//        List<List<Integer>> trainingData = new ArrayList<>();
//        for (String name : names) {
//            List<Integer> nameData = new ArrayList<>();
//            for (int i = 0; i < name.length() - 1; i++) {
//                char[] chars = name.substring(i, i + WINDOW_SIZE).toCharArray();
//                int[] charIndices = new int[chars.length];
//                for (int j = 0; j < chars.length; j++) {
//                    charIndices[j] = vocabulary.indexOf(chars[j]);
//                }
//                nameData.add(charIndices[0]);
//                nameData.add(charIndices[1]);
//            }
//            trainingData.add(nameData);
//        }
//        return trainingData;
//    }
//
//    private static String generateName(MultiLayerNetwork model, List<Character> vocabulary) {
//        StringBuilder nameBuilder = new StringBuilder();
//        int[] input = new int[WINDOW_SIZE];
//        // Initialize input with random characters from the vocabulary
//        for (int i = 0; i < WINDOW_SIZE; i++) {
//            input[i] = (int) (Math.random() * vocabulary.size());
//        }
//        while (nameBuilder.length() < 10) {
//            INDArray inputVector = Nd4j.zeros(1, vocabulary.size(), WINDOW_SIZE);
//            // Set input vector to the current input
//            for (int i = 0; i < WINDOW_SIZE; i++) {
//                int index = input[i];
//                inputVector.putScalar(new int[]{0, index, i}, 1);
//            }
//            // Predict the next character using the model
//            INDArray output = model.rnnTimeStep(inputVector);
//            int[] probabilities = new int[vocabulary.size()];
//            for (int i = 0; i < vocabulary.size(); i++) {
//                double probability = output.getDouble(0, i);
//                probabilities[i] = (int) (probability * 1000);
//            }
//            int nextCharIndex = sampleFromDistribution(probabilities);
//            char nextChar = vocabulary.get(nextCharIndex);
//            nameBuilder.append(nextChar);
//            // Shift input by one character to the right
//            System.arraycopy(input, 1, input, 0, input.length - 1);
//            input[input.length - 1] = nextCharIndex;
//        }
//        return nameBuilder.toString();
//    }
//
//    private static int sampleFromDistribution(int[] distribution) {
//        int sum = Arrays.stream(distribution).sum();
//        int randomValue = (int) (Math.random() * sum);
//        int i = 0;
//        int runningSum = distribution[i];
//        while (runningSum < randomValue) {
//            i++;
//            runningSum += distribution[i];
//        }
//        return i;
//    }
//}
