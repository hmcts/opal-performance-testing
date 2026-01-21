package simulations.Scripts.ScenarioBuilder;

import io.gatling.javaapi.core.Session;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DraftAccountQueryBuilder {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Pure builder: builds the query string from given parameters.
     * If useDefaultDates = true, adds a 7-day window from 7 days ago -> today.
     * If useDefaultDates = false, no date parameters are added.
     */
    public static String build(
        List<String> businessUnitIds,
        List<String> businessUnitUserIds,
        List<String> statuses,
        String submittedByParamName,
        boolean useDefaultDates
    ) {

        // Business units
        String businessUnitParams = businessUnitIds.stream()
            .map(id -> "business_unit=" + id)
            .collect(Collectors.joining("&"));

        // Statuses
        String statusParams = statuses.stream()
            .map(status -> "status=" + URLEncoder.encode(status, StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));

        // submitted_by / not_submitted_by
        String submittedByParams = businessUnitUserIds.stream()
            .map(id -> submittedByParamName + "=" + id)
            .collect(Collectors.joining("&"));

        // Optional default dates
        String dateFromParam = "";
        String dateToParam = "";
        if (useDefaultDates) {
            dateFromParam = "account_status_date_from=" + LocalDate.now().minusDays(7).format(FORMATTER);
            dateToParam = "account_status_date_to=" + LocalDate.now().format(FORMATTER);
        }

        // Combine all parts
        return List.of(
                businessUnitParams,
                statusParams,
                submittedByParams,
                dateFromParam,
                dateToParam
            ).stream()
            .filter(s -> !s.isBlank())
            .collect(Collectors.joining("&"));
    }

    /**
     * Session-based helper: extracts lists from session and stores the query string
     */
    public static Session buildAndStore(
        Session session,
        String sessionKey,
        List<String> statuses,
        String submittedByParamName,
        boolean useDefaultDates
    ) {

        @SuppressWarnings("unchecked")
        List<String> businessUnitIds = (List<String>) session.get("businessUnitIds");

        @SuppressWarnings("unchecked")
        List<String> businessUnitUserIds = (List<String>) session.get("businessUnitUserIds");

        if (businessUnitIds == null || businessUnitUserIds == null) {
            throw new RuntimeException("Business unit data missing from session");
        }

        String query = build(
            businessUnitIds,
            businessUnitUserIds,
            statuses,
            submittedByParamName,
            useDefaultDates
        );

        return session.set(sessionKey, query);
    }
}
