import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {


    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logLine) {
        String[] parts = logLine.split("\"");
        String firstPart = parts[0].trim();
        String[] firstPartComponents = firstPart.split("\\s+");

        this.ipAddr = firstPartComponents[0];

        String dateTimeStr = firstPart.substring(firstPart.indexOf('[') + 1, firstPart.indexOf(']'));
        this.time = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

        String request = parts[1];
        String[] requestParts = request.split("\\s+");

        this.method = HttpMethod.fromString(requestParts[0]);
        this.path = requestParts[1];

        String[] statusParts = parts[2].trim().split("\\s+");
        this.responseCode = Integer.parseInt(statusParts[0]);
        this.responseSize = Integer.parseInt(statusParts[1]);

        this.referer = parts[3];

        if (parts.length >= 6) {
            this.userAgent = new UserAgent(parts[5]);
        } else {
            this.userAgent = new UserAgent("");
        }
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}
