public enum HttpMethod {
    GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH, CONNECT, TRACE, OTHER;
    public static HttpMethod fromString(String method) {
        try {
            return valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }

}
