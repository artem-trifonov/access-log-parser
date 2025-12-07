public class UserAgent {
    private final String os;
    private final String browser;

    public String getOs() {
        return os;
    }

    public String getBrowser() {
        return browser;
    }

    private String parseOperatingSystem(String userAgentString) {
        String ua = userAgentString.toLowerCase();

        if (ua.contains("windows")) {
            return "Windows";
        } else if (ua.contains("mac os") || ua.contains("macos")) {
            return "macOS";
        } else if (ua.contains("linux")) {
            return "Linux";
        } else if (ua.contains("android")) {
            return "Android";
        } else if (ua.contains("ios")) {
            return "iOS";
        } else {
            return "Other";
        }
    }

    private String parseBrowser(String userAgentString) {
        String ua = userAgentString.toLowerCase();

        if (ua.contains("edge")) {
            return "Edge";
        } else if (ua.contains("firefox")) {
            return "Firefox";
        } else if (ua.contains("chrome") && !ua.contains("chromium")) {
            return "Chrome";
        } else if (ua.contains("safari") && !ua.contains("chrome")) {
            return "Safari";
        } else if (ua.contains("opera")) {
            return "Opera";
        } else {
            return "Other";
        }
    }

    public UserAgent(String userAgentString) {
        this.os = parseOperatingSystem(userAgentString);
        this.browser = parseBrowser(userAgentString);
    }
}
