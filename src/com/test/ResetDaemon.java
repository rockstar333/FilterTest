package com.test;

public class ResetDaemon {

    private Checker currentChecker;

    public ResetDaemon(Resetable resetable, long resetPeriod) {
        currentChecker = new Checker(resetable, resetPeriod);
    }

    public void start() {
        currentChecker.start();
    }


    class Checker extends Thread {

        private final Resetable resetable;
        private final long resetPeriod;

        Checker(Resetable resetable, long resetPeriod) {
            this.resetable = resetable;
            this.resetPeriod = resetPeriod;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(resetPeriod);
                    resetable.reset();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // I am not 100% sure should we start a new Checker here and return.
                    // But it make sense to guarantee interruptable behaviour
                    currentChecker = new Checker(resetable, resetPeriod);
                    currentChecker.start();
                    return;
                }
            }
        }

    }
}
