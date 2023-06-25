package bk.edu.utils;

public class HostUtils {
    public static String getBackHost() {
        return System.getenv("BACKEND_HOST") == null
                ? "20.196.248.69"
                : System.getenv("BACKEND_HOST");
    }
}
