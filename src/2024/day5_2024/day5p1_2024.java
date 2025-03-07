import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day5p1_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        List<int[]> rules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();
        int index = 0;

        while (index < lines.size() && !lines.get(index).trim().isEmpty()) {
            String line = lines.get(index).trim();
            if (line.contains("|")) {
                String[] parts = line.split("\\|");
                int a = Integer.parseInt(parts[0].trim());
                int b = Integer.parseInt(parts[1].trim());
                rules.add(new int[] { a, b });
            }
            index++;
        }

        while (index < lines.size() && lines.get(index).trim().isEmpty()) {
            index++;
        }

        for (; index < lines.size(); index++) {
            String line = lines.get(index).trim();
            if (!line.isEmpty()) {
                String[] parts = line.split(",");
                List<Integer> update = new ArrayList<>();
                for (String part : parts) {
                    update.add(Integer.parseInt(part.trim()));
                }
                updates.add(update);
            }
        }

        int sumMiddle = 0;

        for (List<Integer> update : updates) {
            Map<Integer, Integer> pos = new HashMap<>();
            for (int i = 0; i < update.size(); i++) {
                pos.put(update.get(i), i);
            }

            boolean valid = true;
            for (int[] rule : rules) {
                int pageA = rule[0];
                int pageB = rule[1];
                if (pos.containsKey(pageA) && pos.containsKey(pageB)) {
                    if (pos.get(pageA) >= pos.get(pageB)) {
                        valid = false;
                        break;
                    }
                }
            }

            if (valid) {
                int middleIndex = update.size() / 2;
                sumMiddle += update.get(middleIndex);
            }
        }

        System.out.println(sumMiddle);
    }
}
