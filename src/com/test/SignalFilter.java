package com.test;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SignalFilter implements Filter {

    private final int limit;
    private final AtomicInteger count;
    private final AtomicLong start;
    private final long secondsToWait;

    private long end;

    public SignalFilter (int limit) {
        this.limit = limit;
        this.count = new AtomicInteger(0);
        this.start = new AtomicLong(0);
        this.secondsToWait = TimeUnit.SECONDS.toNanos(10);
    }

    @Override
    public boolean isSignalAllowed() {
        long current = System.nanoTime();
        long started = start.get();
        long difference = current - started;
        if (difference > secondsToWait)  {
            boolean reset = start.compareAndSet(started, current);
            if (reset) {
                count.set(0);
            }
        }
        return count.getAndIncrement() < limit;
    }
}
