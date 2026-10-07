package simulations.Scripts.Utilities;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;

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

    // Minor Creditor action counters
    public static final AtomicInteger MINOR_CREDITOR_UPDATED = new AtomicInteger();
    public static final AtomicInteger MINOR_CREDITOR_CHANGED = new AtomicInteger();
    public static final AtomicInteger MINOR_CREDITOR_PAYMENT_REMOVED = new AtomicInteger();
    
    private AccountCounters() {
    }
}