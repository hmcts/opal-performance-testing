package simulations.Scripts.Utilities;

import java.util.concurrent.atomic.AtomicInteger;

public final class AccountCounters {

    public static final AtomicInteger TOTAL_CREATED = new AtomicInteger(0);

    public static final AtomicInteger FIXED_CREATED = new AtomicInteger(0);
    public static final AtomicInteger FINE_CREATED = new AtomicInteger(0);
    public static final AtomicInteger CONDITIONAL_CREATED = new AtomicInteger(0);
    public static final AtomicInteger APPROVED = new AtomicInteger(0);
    public static final AtomicInteger REJECTED = new AtomicInteger(0);
    
    private AccountCounters() {
    }
}