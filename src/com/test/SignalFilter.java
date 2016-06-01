package com.test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SignalFilter implements Filter, Resetable {

    private final int limit;

    private final AtomicInteger count;

    public SignalFilter (int limit) {
       this.limit = limit;
        this.count = new AtomicInteger(0);
        ResetDaemon daemon = new ResetDaemon(this, 1000 * 60);
        daemon.start();
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
