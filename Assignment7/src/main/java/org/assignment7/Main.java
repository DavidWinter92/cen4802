package org.assignment7;

import java.io.File;

/**
 * Main class to run the log simulation and parse results.
 * <p>
 * This class simulates multiple data feeds (BPIPE, LDR, EdgeDevice)
 * by running their feed methods on separate threads.
 * When the program terminates, a shutdown hook parses the generated
 * JSON logs using dedicated parsers and prints a summary.
 * <p>
 * The program demonstrates:
 * <ul>
 *     <li>Simulating normal and abnormal feed events</li>
 *     <li>Counting events of different severities (P1, P2, P3)</li>
 *     <li>Tracking first and most recent occurrences of specific events</li>
 *     <li>Generating a human-readable log summary</li>
 * </ul>
 */
public class Main {

    /**
     * Entry point for the simulation program.
     * <p>
     * It performs the following steps:
     * <ol>
     *     <li>Creates instances of BpipeFeed, LdrFeed, and EdgeDevice</li>
     *     <li>Starts feed simulations on separate threads</li>
     *     <li>Registers a shutdown hook to parse log files and generate a summary</li>
     * </ol>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        BpipeFeed bpipe = new BpipeFeed();
        LdrFeed ldr = new LdrFeed();
        EdgeDevice edge = new EdgeDevice();

        new Thread(edge::simulateDisconnection).start();
        new Thread(ldr::simulateStaleNvidia).start();
        new Thread(bpipe::simulateNvidiaFeed).start();
        new Thread(bpipe::simulateNullDataAMD).start();
        new Thread(ldr::simulateValidAMD).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down simulation, parsing JSON logs...");

            File logsDir = new File("logs");
            File[] logFiles = logsDir.listFiles((dir, name) -> name.endsWith(".json"));

            if (logFiles != null && logFiles.length > 0) {

                // Create individual parser instances
                BpipeParser bpipeParser = new BpipeParser();
                LdrParser ldrParser = new LdrParser();
                EdgeParser edgeParser = new EdgeParser();

                // Parse files individually based on file name
                for (File file : logFiles) {
                    String name = file.getName().toUpperCase();
                    if (name.contains("BPIPE")) {
                        bpipeParser.parse(file);
                    } else if (name.contains("LDR")) {
                        ldrParser.parse(file);
                    } else if (name.contains("EDGE")) {
                        edgeParser.parse(file);
                    }
                }

                // Generate and print summary
                LogSummary summary = new LogSummary(bpipeParser, ldrParser, edgeParser);
                summary.printSummary();

            } else {
                System.out.println("No JSON log files found to parse.");
            }
        }));
    }
}
