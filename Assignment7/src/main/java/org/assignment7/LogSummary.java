package org.assignment7;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Summarizes log events from BPIPE, LDR, and Edge parsers.
 * Prints a readable summary to the console and saves it to "logs/summary.log".
 */
public class LogSummary {

    private final LogSourceParser bpipe;
    private final LogSourceParser ldr;
    private final LogSourceParser edge;

    /**
     * Creates a new log summary instance using the provided parsers.
     *
     * @param bpipe BPIPE log parser
     * @param ldr LDR log parser
     * @param edge Edge log parser
     */
    public LogSummary(LogSourceParser bpipe, LogSourceParser ldr, LogSourceParser edge) {
        this.bpipe = bpipe;
        this.ldr = ldr;
        this.edge = edge;
    }

    /**
     * Generates and prints the log summary.
     * Also writes the summary to "logs/summary.log".
     */
    public void printSummary() {
        int totalP1 = edge.getP1Count();
        int totalP2 = ldr.getP2Count();
        int totalP3 = bpipe.getP3Count();

        String summary = "\n===== LOG SUMMARY =====\n" +
                "P1 events: " + totalP1 + "\n" +
                "P2 events: " + totalP2 + "\n" +
                "P3 events: " + totalP3 + "\n\n" +

                "Initially detected stale ticker: " + ldr.getFirstEvent() + " on " + ldr.getFirstEventTime() + "\n" +
                "Most recent stale ticker: " + ldr.getLatestEvent() + " on " + ldr.getLatestEventTime() + "\n\n" +

                "Initially detected null ticker: " + bpipe.getFirstEvent() + " on " + bpipe.getFirstEventTime() + "\n" +
                "Most recent null ticker: " + bpipe.getLatestEvent() + " on " + bpipe.getLatestEventTime() + "\n\n" +

                "Initially detected edge disconnect: " + edge.getFirstEvent() + " on " + edge.getFirstEventTime() + "\n" +
                "Most recent edge disconnect: " + edge.getLatestEvent() + " on " + edge.getLatestEventTime() + "\n" +
                "========================";

        System.out.println(summary);

        try (FileWriter writer = new FileWriter("logs/summary.log")) {
            writer.write(summary);
        } catch (IOException e) {
            System.err.println("Error writing summary.log: " + e.getMessage());
        }
    }
}
