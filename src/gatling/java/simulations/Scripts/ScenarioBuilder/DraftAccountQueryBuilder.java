package simulations.Scripts.ScenarioBuilder;

import io.gatling.javaapi.core.Session;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DraftAccountQueryBuilder {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Pure builder: creates a safe query string.
     */
    public static String build(
        List<String> businessUnitIds,
        List<String> businessUnitUserIds,
        List<String> statuses,
        String submittedByParamName,
        boolean useDefaultDates
    ) {

        List<String> params = new ArrayList<>();

        // -------------------------
        // Business Units (deduped)
        // -------------------------
        businessUnitIds.stream()
            .filter(id -> id != null && !id.isBlank())
            .distinct()
            .forEach(id ->
                params.add("business_unit=" + encode(id))
            );

        // -------------------------
        // Submitted By / Not Submitted By
        // -------------------------
        businessUnitUserIds.stream()
            .filter(id -> id != null && !id.isBlank())
            .distinct()
            .forEach(id ->
                params.add(submittedByParamName + "=" + encode(id))
            );

        // -------------------------
        // Statuses
        // -------------------------
        statuses.stream()
            .filter(s -> s != null && !s.isBlank())
            .distinct()
            .forEach(status ->
                params.add("status=" + encode(status))
            );

        // -------------------------
        // Optional date range
        // -------------------------
        if (useDefaultDates) {
            params.add("account_status_date_from=" +
                LocalDate.now().minusDays(7).format(FORMATTER));

            params.add("account_status_date_to=" +
                LocalDate.now().format(FORMATTER));
        }

        // -------------------------
        // Final query string
        // -------------------------
        return String.join("&", params);
    }

    /**
     * Session helper: pulls lists from session and stores query string.
     */
    public static Session buildAndStore(
        Session session,
        String sessionKey,
        List<String> statuses,
        String submittedByParamName,
        boolean useDefaultDates
    ) {

        List<String> businessUnitIds =
            new ArrayList<>(session.getList("businessUnitIds"));

        List<String> businessUnitUserIds =
            new ArrayList<>(session.getList("businessUnitUserIds"));

        if (businessUnitIds.isEmpty() || businessUnitUserIds.isEmpty()) {
            throw new RuntimeException("Business unit data missing from session");
        }

        // Defensive dedupe (extra safety even though build() also does it)
        businessUnitIds = businessUnitIds.stream().distinct().toList();
        businessUnitUserIds = businessUnitUserIds.stream().distinct().toList();

        String query = build(
            businessUnitIds,
            businessUnitUserIds,
            statuses,
            submittedByParamName,
            useDefaultDates
        );

        return session.set(sessionKey, query);
    }

    /**
     * URL encoder helper
     */
    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}