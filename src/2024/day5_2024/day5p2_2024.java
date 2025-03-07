import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day5p2_2024 {
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
            if (isOrdered(update, rules)) {
                continue;
            } else {
                List<Integer> corrected = topologicalSort(update, rules);
                int middleIndex = corrected.size() / 2;
                sumMiddle += corrected.get(middleIndex);
            }
        }

        System.out.println(sumMiddle);
    }

    private static boolean isOrdered(List<Integer> update, List<int[]> rules) {
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < update.size(); i++) {
            pos.put(update.get(i), i);
        }
        for (int[] rule : rules) {
            int a = rule[0];
            int b = rule[1];
            if (pos.containsKey(a) && pos.containsKey(b) && pos.get(a) >= pos.get(b)) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> topologicalSort(List<Integer> update, List<int[]> rules) {
        Set<Integer> pages = new HashSet<>(update);

        Map<Integer, Set<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> indegree = new HashMap<>();
        for (Integer page : pages) {
            graph.put(page, new HashSet<>());
            indegree.put(page, 0);
        }

        for (int[] rule : rules) {
            int a = rule[0];
            int b = rule[1];
            if (pages.contains(a) && pages.contains(b)) {
                if (!graph.get(a).contains(b)) {
                    graph.get(a).add(b);
                    indegree.put(b, indegree.get(b) + 1);
                }
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (Integer page : pages) {
            if (indegree.get(page) == 0) {
                queue.add(page);
            }
        }

        List<Integer> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            ordered.add(node);
            for (Integer neighbor : graph.get(node)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (ordered.size() != pages.size()) {
            throw new RuntimeException("Cycle detected in update ordering, input invalid!");
        }

        return ordered;
    }
}
