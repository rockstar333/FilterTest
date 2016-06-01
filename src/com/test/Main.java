package com.test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int numberOfSignalsPerProducer = 1000;
    private static final int numberOfSignalsProducers = 100;

    private static class TestProducer extends Thread {
        private final Filter filter;
        private final AtomicInteger totalPassed;

        private TestProducer(Filter filter, AtomicInteger totalPassed) {
            this.filter = filter;
            this.totalPassed = totalPassed;
        }

        @Override
        public void run() {
            Random rnd = new Random ();
            try {
                for (int j = 0; j < numberOfSignalsPerProducer; j++) {
                    if (filter.isSignalAllowed())
                        totalPassed.incrementAndGet();
                    Thread.sleep(rnd.nextInt(100));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String ... args) throws InterruptedException {
        final int N = 100;
        Filter filter = new SignalFilter(N);

        AtomicInteger totalPassed = new AtomicInteger();
        Thread [] producers = new Thread[numberOfSignalsProducers];
        for (int i=0; i < producers.length; i++)
            producers[i] = new TestProducer(filter, totalPassed);

        for (Thread producer : producers)
            producer.start();

        for (Thread producer : producers)
            producer.join();

        System.out.println("Filter allowed " + totalPassed + " signals out of " + (numberOfSignalsPerProducer * numberOfSignalsProducers));
    }
}
