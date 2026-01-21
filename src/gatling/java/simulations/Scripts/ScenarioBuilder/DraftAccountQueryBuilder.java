package simulations.Scripts.ScenarioBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class DraftAccountQueryBuilder {

    public static String build(
        List<String> businessUnitIds,
        List<String> businessUnitUserIds,
        List<String> statuses,
        String notSubmittedByParamName
    ) {
        String businessUnitParams = businessUnitIds.stream()
            .map(id -> "business_unit=" + id)
            .collect(Collectors.joining("&"));

        String statusParams = statuses.stream()
            .map(status -> "status=" + URLEncoder.encode(status, StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));

        String notSubmittedByParams = businessUnitUserIds.stream()
            .map(id -> notSubmittedByParamName + "=" + id)
            .collect(Collectors.joining("&"));

        return String.join("&",
            businessUnitParams,
            statusParams,
            notSubmittedByParams
        );
    }
}
