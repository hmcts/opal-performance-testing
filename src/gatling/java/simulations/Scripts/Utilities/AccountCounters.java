package simulations.Scripts.Utilities;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;


public final class AccountCounters {

    public static final AtomicInteger TOTAL_CREATED = new AtomicInteger(0);

    public static final AtomicInteger FIXED_CREATED = new AtomicInteger(0);
    public static final AtomicInteger FINE_CREATED = new AtomicInteger(0);
    public static final AtomicInteger CONDITIONAL_CREATED = new AtomicInteger(0);
    public static final AtomicInteger APPROVED = new AtomicInteger(0);
    public static final AtomicInteger REJECTED = new AtomicInteger(0);
    public static final Set<String> CLAIMED_ACCOUNTS = ConcurrentHashMap.newKeySet();

    // Parent & Guardian counters
    public static final AtomicInteger PG_ADDED = new AtomicInteger();
    public static final AtomicInteger PG_REMOVED = new AtomicInteger();
    public static final AtomicInteger PG_CHANGED = new AtomicInteger();

    // Enforcement action counters
    public static final AtomicInteger ENFORCEMENT_ADDED = new AtomicInteger();
    public static final AtomicInteger ENFORCEMENT_REMOVED = new AtomicInteger();
    public static final AtomicInteger ENFORCEMENT_CHANGED = new AtomicInteger();
    
    // Payment change action counters

    public static final AtomicInteger PAYMENT_CHANGE_ADDED = new AtomicInteger();

    // Minor Creditor action counters
    public static final AtomicInteger MINOR_CREDITOR_UPDATED = new AtomicInteger();
    public static final AtomicInteger MINOR_CREDITOR_CHANGED = new AtomicInteger();
    public static final AtomicInteger MINOR_CREDITOR_PAYMENT_REMOVED = new AtomicInteger();

    // Search
    public static final AtomicInteger SEARCH_ACCOUNT = new AtomicInteger();
    public static final AtomicInteger FIXED_ACCOUNT = new AtomicInteger();

    // Errror counters
    // public static final AtomicInteger ERROR_COUNT = new AtomicInteger();

    // public static int getTotalErrorCount() {
    //     return ERROR_COUNT.get();
    // }

    public static final AtomicInteger ERROR_COUNT = new AtomicInteger(0);

    private static final ConcurrentHashMap<String, AtomicInteger> ERRORS_BY_CODE =
            new ConcurrentHashMap<>();

    public static int incrementErrorCountForCode(Integer statusCode) {
        String code = statusCode == null
                ? "NO_HTTP_STATUS"
                : statusCode.toString();

        return ERRORS_BY_CODE
                .computeIfAbsent(code, key -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public static Map<String, Integer> getErrorCountsByCode() {
        Map<String, Integer> snapshot = new HashMap<>();

        ERRORS_BY_CODE.forEach((code, count) ->
                snapshot.put(code, count.get())
        );

        return Collections.unmodifiableMap(snapshot);
    }
    
    private AccountCounters() {
    }
}