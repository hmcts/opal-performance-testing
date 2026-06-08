package simulations.Scripts.Utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class ContentDigestGenerator {

    public static String generateSha512ContentDigest(String requestBody) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digestBytes = md.digest(
                requestBody.getBytes(StandardCharsets.UTF_8)
            );

            String base64Digest =
                Base64.getEncoder().encodeToString(digestBytes);

            return "sha-512=:" + base64Digest + ":";

        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to generate Content-Digest header", e
            );
        }
    }
}