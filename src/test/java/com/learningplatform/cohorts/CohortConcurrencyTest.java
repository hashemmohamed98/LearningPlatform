package com.learningplatform.cohorts;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CohortConcurrencyTest {

    @Test
    void concurrentJoinsNeverExceedCapacityOrLoseAStudent()
            throws InterruptedException {

        for (int r = 0; r < 200; r++) {
            Cohort cohort = new Cohort();

            int threadCount = 12;
            CountDownLatch ready = new CountDownLatch(threadCount);
            CountDownLatch start = new CountDownLatch(1);

            boolean[] joined = new boolean[threadCount];
            Throwable[] errors = new Throwable[threadCount];
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                StudentId student = new StudentId("Student-" + i);

                threads[i] = new Thread(() -> {
                    ready.countDown();

                    try {
                        start.await();
                        cohort.join(student);
                        joined[index] = true;
                    } catch (CohortFullException expected) {
                        // Expected for students beyond capacity.
                    } catch (Throwable t) {
                        errors[index] = t;
                    }
                }, "join-" + i);
            }

            for (Thread thread : threads) {
                thread.start();
            }

            assertTrue(
                    ready.await(5, TimeUnit.SECONDS),
                    "round " + r + ": threads did not become ready"
            );

            start.countDown();

            for (Thread thread : threads) {
                thread.join(5_000);

                assertFalse(
                        thread.isAlive(),
                        "round " + r + ": thread " + thread.getName() + " did not finish"
                );
            }

            for (int i = 0; i < threadCount; i++) {
                assertNull(errors[i],
                        "round " + r + ": unexpected error in thread " + i
                );
            }

            assertEquals(
                    6,
                    cohort.getStudents().size(),
                    "round " + r + ": cohort size must equal capacity"
            );

            int joinedCount = 0;
            for (boolean didJoin : joined) {
                if (didJoin) {
                    joinedCount++;
                }
            }

            assertEquals(
                    6,
                    joinedCount,
                    "round " + r + ": successful joins must equal capacity"
            );
        }
    }

}
