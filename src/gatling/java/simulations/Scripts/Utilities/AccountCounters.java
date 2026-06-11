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
    
    private AccountCounters() {
    }
}