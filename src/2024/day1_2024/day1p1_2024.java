
// day1p1_2024.java
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class day1p1_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));

        List<Long> leftList = new ArrayList<>();
        List<Long> rightList = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;
            String[] tokens = line.split("\\s+");
            if (tokens.length < 2) {
                throw new IllegalArgumentException("Formato non valido: " + line);
            }
            leftList.add(Long.parseLong(tokens[0]));
            rightList.add(Long.parseLong(tokens[1]));
        }

        Collections.sort(leftList);
        Collections.sort(rightList);

        long totalDistance = 0;
        for (int i = 0, n = leftList.size(); i < n; i++) {
            totalDistance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        System.out.println(totalDistance);
    }
}
