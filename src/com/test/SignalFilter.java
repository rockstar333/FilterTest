package com.test;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SignalFilter implements Filter, Resetable {

    private final int limit;

    private final AtomicInteger count;

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public SignalFilter (int limit) {
       this.limit = limit;
        this.count = new AtomicInteger(0);
        service.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                count.set(0);
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    @Override
    public boolean isSignalAllowed() {
        return count.getAndIncrement() < limit;
    }


    @Override
    public void reset() {
        count.set(0);
    }
}
