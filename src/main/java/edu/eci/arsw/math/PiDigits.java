package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.eci.arsw.PiDigitsThread;


///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;

    /**
     * Returns a range of hexadecimal digits of pi.
     * @param totalDigits The total number of digits to compute.
     * @param nThreads The number of threads to use for computation.
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int totalDigits, int nThreads) throws InterruptedException {
        byte[] digits = new byte[totalDigits];
        int range = totalDigits / nThreads;
        List<PiDigitsThread> threads = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {
            int start = i * range;
            int end = (i == nThreads - 1) ? totalDigits : start + range;
            PiDigitsThread thread = new PiDigitsThread(start, end, digits);
            threads.add(thread);
            thread.start();
        }

        for (PiDigitsThread thread : threads) {
            thread.join();
        }

        return digits;
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

}