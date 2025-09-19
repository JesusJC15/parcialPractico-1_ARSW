package edu.eci.arsw;

import java.util.concurrent.atomic.AtomicBoolean;

public class PiDigitsThread extends Thread {
    private final int start;
    private final int end;
    private final byte[] result;
    private final AtomicBoolean paused;
    private int processedDigits = 0;

    public PiDigitsThread(int start, int end, byte[] result, AtomicBoolean paused) {
        this.start = start;
        this.end = end;
        this.result = result;
        this.paused = paused;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            synchronized (paused) {
                while (paused.get()) {
                    try {
                        paused.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            result[i] = calculatePiDigit(i);
            processedDigits++;

            if (i % 1000 == 0 && paused.get()) {
                System.out.println("Thread " + getId() + " processed digits: " + processedDigits);
            }
        }
    }

    private byte calculatePiDigit(int position) {
        return (byte) (position % 16);
    }

    public int getProcessedDigits() {
        return processedDigits;
    }
}