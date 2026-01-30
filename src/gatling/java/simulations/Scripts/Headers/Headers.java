package simulations.Scripts.Headers;

import simulations.Scripts.Utilities.AppConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages HTTP headers for different types of requests in the application.
@ -13,244 +12,206 @@ import java.util.Objects;
 */
public final class Headers {

    // Private constructor to prevent instantiation
    private Headers() {
        throw new AssertionError("Utility class - do not instantiate");
    }

    /**
     * Adds additional headers conditionally to an existing header map.
     *
     * @param headers Existing headers to modify
     * @param userId Whether to add user_id header
     * @param eventStream Whether to use event-stream accept header
     * @return Modified headers map (immutable)
     */
    /**
     * Gets headers for a specific header type
     * @param headerTypeValue The header type value
     * @return Map of headers for the specified type
     */
    /**
     * Gets a map of headers for a specific header type.
     * 
     * @param headerTypeValue The header type value from HeaderType enum
     * @return An immutable map of headers for the specified type
     * @throws IllegalArgumentException if headerTypeValue is invalid
     */

    public static final String BROWSER_UA =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) " +
        "Chrome/143.0.0.0 Safari/537.36";
    
    public static Map<String, String> getHeaders(int headerTypeValue) {
        HeaderType headerType = HeaderType.fromValue(headerTypeValue);
        Map<String, String> headers = new HashMap<>();

     

        switch (headerType) {
            case TEST_1:
            headers.put("Sec-Fetch-Dest", "document");
            headers.put("Sec-Fetch-Mode", "navigate");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("Upgrade-Insecure-Requests", "1");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            headers.put("Referer","https://opal-frontend.test.platform.hmcts.net/");
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36");


            break;
            
            case TEST_2:
            headers.put("Accept", "application/json");
            headers.put("Content-type", "application/json; charset=UTF-8");
            headers.put("Origin", "https://login.microsoftonline.com");
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("canary", "#{apiCanary}");
            headers.put("client-request-id", "#{getClientRequestId}");
            headers.put("hpgact", "1800");
            headers.put("hpgid", "1104");
            headers.put("hpgrequestid", "#{sessionId}");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            break;

            case TEST_3:
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Origin", "https://login.microsoftonline.com");
                headers.put("Referer", "https://login.microsoftonline.com/" + AppConfig.TenantConfig.AAD_TENANT_ID + "/oauth2/v2.0/authorize");
                headers.put("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9," +
                    "image/avif,image/webp,image/apng,*/*;q=0.8," +
                    "application/signed-exchange;v=b3;q=0.7"
                );
                headers.put("User-Agent", Headers.BROWSER_UA);
                headers.put("sec-ch-ua",
                    "\"Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24\""
                );
                headers.put("sec-ch-ua-mobile", "?0");
                headers.put("sec-ch-ua-platform", "\"Windows\"");

                break;

            case TEST_4:       
            headers.put("Cache-Control", "max-age=0");
            headers.put("Origin", "https://login.microsoftonline.com");
            headers.put("Sec-Fetch-Dest", "document");
            headers.put("Sec-Fetch-Mode", "navigate");
            headers.put("Sec-Fetch-Site", "cross-site");
            headers.put("Upgrade-Insecure-Requests", "1");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            break;

            case TEST_5:
            headers.put("Accept", "application/json, text/plain, */*");
            headers.put("Cache-Control", "no-cache");
            headers.put("Expires", "0");
            headers.put("Pragma", "no-cache");
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            break;

            case TEST_6:
            headers.put("Accept", "application/json, text/plain, */*");
            headers.put("If-None-Match", "W/\"35-pzeKyq0NE9c2fLCr1PPYqFCAl+w\"");
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            break;

            case TEST_7:
            headers.put("Accept", "application/json, text/plain, */*");
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            break;

            case TEST_8:
            headers.put("Connection", "keep-alive");
            headers.put("Upgrade-Insecure-Requests", "1");
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            headers.put("Accept-Language", "en-US,en;q=0.9");
            headers.put("Referer","https://opal-frontend.test.platform.hmcts.net/");
            headers.put("Sec-Fetch-Site", "cross-site");
            headers.put("Sec-Fetch-Mode", "navigate");
            headers.put("Sec-Fetch-Dest", "document");
            headers.put("sec-ch-ua", "\"Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24\"");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "\"Windows\"");
            break;

            case TEST_9:
            headers.put("Sec-Fetch-Dest", "document");
            headers.put("Sec-Fetch-Mode", "navigate");
            headers.put("Sec-Fetch-Site", "cross-site");
            headers.put("sec-ch-ua",  "\"Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24\"");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            break;

            case TEST_10:
            headers.put("Sec-Fetch-Dest", "document");
            headers.put("Sec-Fetch-Mode", "navigate");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("Sec-Fetch-User", "?1");
            headers.put("Upgrade-Insecure-Requests", "1");
            headers.put("sec-ch-ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
            headers.put("Accept-Language", "en-US,en;q=0.9");
            headers.put("Connection", "keep-alive");
            break;

            case TEST_11:
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("cache-control", "no-cache");
            headers.put("expires", "0");
            headers.put("pragma", "no-cache");
           // headers.put("request-id", "|a4d8145674b44132a9331303de1b9516.3303b477b1de4ee8");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
           // headers.put("traceparent", "00-a4d8145674b44132a9331303de1b9516-3303b477b1de4ee8-01");
            break;

            case TEST_12:
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
          //  headers.put("request-id", "|a4d8145674b44132a9331303de1b9516.9ec923d6645f4b42");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
           // headers.put("traceparent", "00-a4d8145674b44132a9331303de1b9516-9ec923d6645f4b42-01");            
            break; 

            case TEST_13:
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("cache-control", "no-cache");
            headers.put("expires", "0");
            headers.put("pragma", "no-cache");
        //    headers.put("request-id", "|a46d288395244ba59db15adbcdc068df.461dc629d7ec41f2");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
        //    headers.put("traceparent", "00-a46d288395244ba59db15adbcdc068df-461dc629d7ec41f2-01");

            case TEST_14:
            headers.put("Origin", "https://opal-frontend.test.platform.hmcts.net");
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("content-type", "application/json");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            break; 

            case TEST_15:
            headers.put("Origin", "https://opal-frontend.test.platform.hmcts.net");
            headers.put("Sec-Fetch-Dest", "empty");
            headers.put("Sec-Fetch-Mode", "cors");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("content-type", "application/json");
            headers.put("sec-ch-ua", "Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "Windows");
            headers.put("Cache-Control", "max-age=0");
            headers.put("if-match", "0");
            default:
            break; 
        }

        
        return Collections.unmodifiableMap(headers);
    }
}