package edu.eci.arsw;

import java.util.concurrent.atomic.AtomicBoolean;

public class PiDigitsThread extends Thread {
    private final int start;
    private final int end;
    private final byte[] result;
    private int processedDigits = 0;

    public PiDigitsThread(int start, int end, byte[] result) {
        this.start = start;
        this.end = end;
        this.result = result;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            result[i] = calculatePiDigit(i);
            processedDigits++;

            if (i % 1000 == 0) {
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