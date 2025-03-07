import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day1p2_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));

        List<Long> leftList = new ArrayList<>();
        Map<Long, Long> rightFrequency = new HashMap<>();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] tokens = line.split("\\s+");
            if (tokens.length < 2) {
                throw new IllegalArgumentException("Formato non valido: " + line);
            }

            long leftNum = Long.parseLong(tokens[0]);
            long rightNum = Long.parseLong(tokens[1]);

            leftList.add(leftNum);
            rightFrequency.put(rightNum, rightFrequency.getOrDefault(rightNum, 0L) + 1);
        }

        long similarityScore = 0;
        for (Long num : leftList) {
            long count = rightFrequency.getOrDefault(num, 0L);
            similarityScore += num * count;
        }

        System.out.println(similarityScore);
    }
}
