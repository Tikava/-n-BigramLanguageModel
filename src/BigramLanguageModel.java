import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class BigramLanguageModel {

    static HashMap<String, Integer> bigrams;
    static Map<String, Double> bigramProbabilities;
    final static Random random = new Random();

    public static void main(String[] args) {
        bigrams = readData(new File("names.txt"));
        bigramProbabilities = calculateBigramProbabilities(bigrams);
        System.out.println(generateName());
        ProbabilityTableVisualizer visualizer = new ProbabilityTableVisualizer(bigramProbabilities);
        visualizer.createTable();
        BigramTableImage.visualize(bigramProbabilities, "bigram_table.png");

    }

    public static String generateName() {

        char currentChar = randomChar();
        StringBuilder name = new StringBuilder("^" + currentChar);


        while (currentChar != '$') {

            Map<Character, Double> possibleChars = new HashMap<>();
            for (Map.Entry<String, Double> entry : bigramProbabilities.entrySet()) {
                String bigram = entry.getKey();
                if (bigram.charAt(0) == currentChar) {
                    char nextChar = bigram.charAt(1);
                    double probability = entry.getValue();
                    possibleChars.put(nextChar, probability);
                }
            }

            double totalProbability = possibleChars.values().stream().mapToDouble(Double::doubleValue).sum();
            double randomNumber = random.nextDouble() * totalProbability;
            double cumulativeProbability = 0.0;
            char nextChar = ' ';

            for (Map.Entry<Character, Double> entry : possibleChars.entrySet()) {
                cumulativeProbability += entry.getValue();
                if (randomNumber < cumulativeProbability) {
                    nextChar = entry.getKey();
                    break;
                }
            }
            name.append(nextChar);
            currentChar = nextChar;
        }
        return name.toString();

    }


    public static char randomChar() {
        return (char)('a' + random.nextInt(26));
    }

    public static HashMap<String, Integer> readData(File file) {
        try {
            Scanner reader = new Scanner(file);
            bigrams = new HashMap<>();

            while (reader.hasNext()) {
                StringBuilder line = new StringBuilder("^$");
                line.insert(1, reader.next());
                List<String> list = getBigrams(line.toString());
                for (String bigram : list) {
                    bigrams.put(bigram, bigrams.getOrDefault(bigram, 0) + 1);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return bigrams;
    }

    public static List<String> getBigrams(String word) {
        List<String> bigrams = new ArrayList<>();
        for (int i = 0; i < word.length() - 1; i++) {
            bigrams.add(word.substring(i, i + 2));
        }
        return bigrams;
    }

    public static Map<String, Double> calculateBigramProbabilities(HashMap<String, Integer> bigrams) {
        bigramProbabilities = new HashMap<>();

        int totalBigramCount = 0;
        for (Map.Entry<String, Integer> entry : bigrams.entrySet()) {
            totalBigramCount += entry.getValue();
        }

        for (Map.Entry<String, Integer> entry : bigrams.entrySet()) {
            String bigram = entry.getKey();
            int count = entry.getValue();


            double probability = (double) count / totalBigramCount;
            bigramProbabilities.put(bigram, probability);
        }

        return bigramProbabilities;
    }
}
