package simulations.Scripts.Utilities;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * Centralized HTTP protocol configuration for all simulations.
 * Provides a single method to configure HTTP client settings including
 * proxy, base URL, caching, encoding, and language headers.
 */
public class HttpProtocolConfig {

    private HttpProtocolConfig() {
        // Utility class, prevent instantiation
    }

    /**
     * Builds and returns the HTTP protocol configuration.
     * 
     * Configuration includes:
     * - Proxy settings (if configured)
     * - Base URL
     * - Caching disabled
     * - Accept-Encoding header
     * - Accept-Language header
     */
    public static HttpProtocolBuilder build() {
        HttpProtocolBuilder builder = http
            .baseUrl(AppConfig.UrlConfig.AUTH_URL)
            .disableCaching()
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-US,en;q=0.9");

        // Add proxy if configured
        if (isProxyEnabled()) {
            builder = builder.proxy(
                io.gatling.javaapi.http.HttpDsl.Proxy(
                    AppConfig.ProxyConfig.HOST,
                    AppConfig.ProxyConfig.PORT
                )
            );
        }

        return builder;
    }

    /**
     * Checks if proxy is enabled based on configuration.
     * Proxy is considered enabled if the ENABLED flag is true and HOST is set and not empty.
     */
    private static boolean isProxyEnabled() {
        return AppConfig.ProxyConfig.ENABLED
            && AppConfig.ProxyConfig.HOST != null 
            && !AppConfig.ProxyConfig.HOST.trim().isEmpty();
    }
}
