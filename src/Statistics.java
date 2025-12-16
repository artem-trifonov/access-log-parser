import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Statistics {
    long totalTraffic;
    LocalDateTime minTime;
    LocalDateTime maxTime;
    HashSet<String> existingPages;
    HashMap<String, Integer> osFrequency;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.existingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        LocalDateTime entryTime = entry.getTime();

        if (this.minTime == null || entryTime.isBefore(this.minTime)) {
            this.minTime = entryTime;
        }

        if (this.maxTime == null || entryTime.isAfter(this.maxTime)) {
            this.maxTime = entryTime;
        }

        if (entry.getResponseCode() == 200 ){
            existingPages.add(entry.getPath());
        }

        osFrequency.put(entry.getUserAgent().getOs(),osFrequency.getOrDefault(entry.getUserAgent().getOs(),0)+1);
    }

    public double getTrafficRate() {
        return (double) totalTraffic / Duration.between(minTime, maxTime).toHours();
    }
    public List<String> getExistingPages() {
        return new ArrayList<>(existingPages);
    }

    public HashMap<String, Double> getOsStatistics() {
        HashMap<String, Double> osStatistics = new HashMap<>();

        int totalCount = 0;
        for (int count : osFrequency.values()) {
            totalCount += count;
        }

        for (var entry : osFrequency.entrySet()) {
            double proportion = (double) entry.getValue() / totalCount;
            osStatistics.put(entry.getKey(), proportion);
        }

        return osStatistics;
    }
}
