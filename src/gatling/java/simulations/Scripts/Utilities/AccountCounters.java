package simulations.Scripts.Utilities;

import java.util.concurrent.atomic.AtomicInteger;
import simulations.Scripts.Utilities.AppConfig.TestingConfig;

public final class AccountCounters {

    public static final AtomicInteger fixed = new AtomicInteger(0);
    public static final AtomicInteger fine = new AtomicInteger(0);
    public static final AtomicInteger conditional = new AtomicInteger(0);

    private AccountCounters() {}

    public static boolean isComplete() {
        return fixed.get() >= TestingConfig.FIXED_TARGET
            && fine.get() >= TestingConfig.FINE_TARGET
            && conditional.get() >= TestingConfig.CONDITIONAL_TARGET;
    }
}
