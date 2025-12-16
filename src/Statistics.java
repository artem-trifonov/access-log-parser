import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class Statistics {
    long totalTraffic;
    LocalDateTime minTime;
    LocalDateTime maxTime;
    HashSet<String> existingPages;
    HashSet<String> unExistingPages;
    HashSet<String> uniqueUser;
    HashMap<String, Integer> osFrequency;
    HashMap<String, Integer> userFrequency;
    HashMap<String, Integer> browserFrequency;
    HashMap<Long, Integer> visitsPerSecond;
    HashSet<String> refererDomains;
    int nonBotRequests;
    int errorRequests;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.existingPages = new HashSet<>();
        this.unExistingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
        this.userFrequency = new HashMap<>();
        this.browserFrequency = new HashMap<>();
        this.visitsPerSecond = new HashMap<>();
        this.refererDomains = new HashSet<>();
        this.uniqueUser = new HashSet<>();
        this.nonBotRequests = 0;
        this.errorRequests = 0;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();
        LocalDateTime entryTime = entry.getTime();
        boolean isBot = entry.getUserAgent().getIsBot();
        long second = entry.getTime().getSecond();

        if (!isBot) {
            nonBotRequests++;
            uniqueUser.add(entry.getIpAddr());
            userFrequency.put(entry.getIpAddr(), userFrequency.getOrDefault(entry.getIpAddr(), 0) + 1);

        }

        if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            errorRequests++;
        }

        if (this.minTime == null || entryTime.isBefore(this.minTime)) {
            this.minTime = entryTime;
        }

        if (this.maxTime == null || entryTime.isAfter(this.maxTime)) {
            this.maxTime = entryTime;
        }

        if (entry.getResponseCode() == 200) {
            existingPages.add(entry.getPath());
        }
        if (entry.getResponseCode() == 404) {
            unExistingPages.add(entry.getPath());
        }



        osFrequency.put(entry.getUserAgent().getOs(), osFrequency.getOrDefault(entry.getUserAgent().getOs(), 0) + 1);
        browserFrequency.put(entry.getUserAgent().getBrowser(), browserFrequency.getOrDefault(entry.getUserAgent().getBrowser(), 0) + 1);
        visitsPerSecond.put(second,visitsPerSecond.getOrDefault(second, 0) + 1);

        referParse(entry);
    }

    public double getTrafficRate() {
        return (double) totalTraffic / Duration.between(minTime, maxTime).toHours();
    }

    public List<String> getExistingPages() {
        return new ArrayList<>(existingPages);
    }

    public List<String> getUnExistingPages() {
        return new ArrayList<>(unExistingPages);
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

    public HashMap<String, Double> getBrowserStatistics() {
        HashMap<String, Double> browserStatistics = new HashMap<>();

        int totalCount = 0;
        for (int count : browserFrequency.values()) {
            totalCount += count;
        }

        for (var entry : browserFrequency.entrySet()) {
            double proportion = (double) entry.getValue() / totalCount;
            browserStatistics.put(entry.getKey(), proportion);
        }

        return browserStatistics;
    }

    public double getAverageVisitsPerHour() {
        Duration duration = Duration.between(minTime, maxTime);
        double hours = duration.toHours();
        if (hours < 1.0) {
            hours = 1.0;
        }
        return (double) nonBotRequests / hours;
    }

    public double getAverageFailedVisitsPerHour() {
        Duration duration = Duration.between(minTime, maxTime);
        double hours = duration.toHours();
        if (hours < 1.0) {
            hours = 1.0;
        }
        if (errorRequests == 0) {
            return 0.0;
        }
        return (double) errorRequests / hours;
    }

    public double getAverageVisitsOneUser() {
        return (double) nonBotRequests / uniqueUser.size();
    }

    private void referParse(LogEntry entry) {
        String url = entry.getReferer();

        if  (url.contains("%3A%2F%2F"))
        {
            url=url.replace("%3A%2F%2F","//:");
        }

        if (url.startsWith("http://")) {
            url = url.substring(7);
        } else if (url.startsWith("https://")) {
            url = url.substring(8);
        }

        if (url.startsWith("www.")) {
            url = url.substring(4);
        }

        int slashIndex = url.indexOf('/');
        if (slashIndex > 0) {
            url = url.substring(0, slashIndex);
        }

        int colonIndex = url.indexOf(':');
        if (colonIndex > 0) {
            url = url.substring(0, colonIndex);
        }

        if (!url.isEmpty() && !url.equals("-")) {
            refererDomains.add(url);
        }
    }

    public int getPeakVisitsPerSecond() {
        return Collections.max(visitsPerSecond.values());
    }

    public List<String> getRefererSites() {
        return new ArrayList<>(refererDomains);
    }

    public int getMaxVisitsOneUser() {
        return Collections.max(userFrequency.values());
    }
}
