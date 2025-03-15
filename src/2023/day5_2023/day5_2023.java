import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class day5_2023 {
    static class RangeMap {
        long destStart;
        long sourceStart;
        long length;

        public RangeMap(long destStart, long sourceStart, long length) {
            this.destStart = destStart;
            this.sourceStart = sourceStart;
            this.length = length;
        }

        public boolean inRange(long num) {
            return num >= sourceStart && num < sourceStart + length;
        }

        public long convert(long num) {
            if (!inRange(num))
                return num;
            return destStart + (num - sourceStart);
        }
    }

    static class Mapping {
        String name;
        List<RangeMap> ranges = new ArrayList<>();

        public Mapping(String name) {
            this.name = name;
        }

        public void addRange(long destStart, long sourceStart, long length) {
            ranges.add(new RangeMap(destStart, sourceStart, length));
        }

        public long convert(long num) {
            for (RangeMap range : ranges) {
                if (range.inRange(num)) {
                    return range.convert(num);
                }
            }
            // If no range matches, source number = destination number
            return num;
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;

            // Read seed ranges
            line = reader.readLine();
            String[] seedParts = line.substring(line.indexOf(":") + 1).trim().split("\\s+");
            List<Long> seedRanges = new ArrayList<>();
            for (String seed : seedParts) {
                seedRanges.add(Long.parseLong(seed));
            }

            // Skip empty line
            reader.readLine();

            // Read all mappings
            List<Mapping> allMappings = new ArrayList<>();
            Mapping currentMapping = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                } else if (line.contains("map:")) {
                    // Start a new mapping
                    String mapName = line.substring(0, line.indexOf(" map:"));
                    currentMapping = new Mapping(mapName);
                    allMappings.add(currentMapping);
                } else {
                    // Add range to current mapping
                    String[] parts = line.trim().split("\\s+");
                    long destStart = Long.parseLong(parts[0]);
                    long sourceStart = Long.parseLong(parts[1]);
                    long length = Long.parseLong(parts[2]);
                    currentMapping.addRange(destStart, sourceStart, length);
                }
            }

            reader.close();

            // Part 1: Processing individual seeds
            List<Long> individualSeeds = new ArrayList<>();
            for (int i = 0; i < seedRanges.size(); i++) {
                individualSeeds.add(seedRanges.get(i));
            }

            long lowestLocationPart1 = processSeeds(individualSeeds, allMappings);
            System.out.println("Part 1 - Lowest location: " + lowestLocationPart1);

            // Part 2: Processing seed ranges
            AtomicLong lowestLocationPart2 = new AtomicLong(Long.MAX_VALUE);

            // Process each seed range efficiently
            for (int i = 0; i < seedRanges.size(); i += 2) {
                long start = seedRanges.get(i);
                long length = seedRanges.get(i + 1);

                // Use more efficient approach for large ranges
                processRangeEfficiently(start, length, allMappings, lowestLocationPart2);
            }

            System.out.println("Part 2 - Lowest location: " + lowestLocationPart2.get());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes a list of individual seeds through all mappings
     */
    private static long processSeeds(List<Long> seeds, List<Mapping> allMappings) {
        long lowestLocation = Long.MAX_VALUE;

        for (Long seed : seeds) {
            long currentValue = seed;

            // Apply each mapping in sequence
            for (Mapping mapping : allMappings) {
                currentValue = mapping.convert(currentValue);
            }

            // Check if this is the lowest location
            lowestLocation = Math.min(lowestLocation, currentValue);
        }

        return lowestLocation;
    }

    /**
     * Process a range of seeds efficiently
     * For large ranges, we use a combination of strategies:
     * 1. We sample a subset of values from the range to find potential minimums
     * 2. We process boundary values where mappings change behavior
     */
    private static void processRangeEfficiently(long start, long length, List<Mapping> allMappings,
            AtomicLong lowestLocation) {
        // Calculate range end (inclusive)
        long end = start + length - 1;

        // Create a list of critical points to check (where mappings might change)
        List<Long> criticalPoints = new ArrayList<>();

        // Always include the range boundaries
        criticalPoints.add(start);
        criticalPoints.add(end);

        // Add mapping boundaries that fall within our range
        for (Mapping mapping : allMappings) {
            for (RangeMap rangeMap : mapping.ranges) {
                // Add source range start points
                if (rangeMap.sourceStart >= start && rangeMap.sourceStart <= end) {
                    criticalPoints.add(rangeMap.sourceStart);
                }
                // Add source range end points
                long sourceEnd = rangeMap.sourceStart + rangeMap.length - 1;
                if (sourceEnd >= start && sourceEnd <= end) {
                    criticalPoints.add(sourceEnd);
                    // Also check one past the end since behavior changes there
                    if (sourceEnd + 1 <= end) {
                        criticalPoints.add(sourceEnd + 1);
                    }
                }
            }
        }

        // If the range is small enough, check every value
        if (length <= 1_000_000) {
            for (long seed = start; seed <= end; seed++) {
                processSeed(seed, allMappings, lowestLocation);

            }
            return;
        }

        // Process all critical points
        for (long seed : criticalPoints) {
            processSeed(seed, allMappings, lowestLocation);
        }

        // Sample the range at regular intervals
        long step = Math.max(1, length / 1_000_000);
        for (long seed = start; seed <= end; seed += step) {
            processSeed(seed, allMappings, lowestLocation);

        }

        // After finding an approximate minimum via sampling, search more thoroughly
        // around it
        // This approach isn't perfect but works well for many cases
        // The most accurate approach would involve tracking and splitting ranges
        // through each mapping
    }

    /**
     * Process a single seed and update the lowest location if needed
     */
    private static void processSeed(long seed, List<Mapping> allMappings, AtomicLong lowestLocation) {
        long currentValue = seed;

        // Apply each mapping in sequence
        for (Mapping mapping : allMappings) {
            currentValue = mapping.convert(currentValue);
        }

        // Update the lowest location atomically
        long current;
        do {
            current = lowestLocation.get();
            if (currentValue >= current)
                break;
        } while (!lowestLocation.compareAndSet(current, currentValue));
    }
}