package org.assignment7;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Parser for BPIPE feed logs.
 * Tracks P3 events (null tickers) and their first/latest occurrences.
 */
public class BpipeParser implements LogSourceParser {

    private int p3Count = 0;
    private String firstNull = null;
    private String latestNull = null;
    private String firstNullTime = "";
    private String latestNullTime = "";

    private final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    /**
     * Parses a BPIPE log file line by line.
     * Detects null ticker events (P3) and counts normal feed messages as P3 events.
     *
     * @param file the BPIPE log file to parse
     */
    @Override
    public void parse(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder jsonBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                jsonBuilder.append(line);
                try {
                    JsonNode entry = mapper.readTree(jsonBuilder.toString());
                    processEntry(entry);
                    jsonBuilder.setLength(0);
                } catch (IOException e) {
                    jsonBuilder.append(" ");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading BPIPE file: " + e.getMessage());
        }
    }

    /**
     * Processes a single log entry and updates P3 count and event timestamps.
     *
     * @param entry JSON log entry node
     */
    private void processEntry(JsonNode entry) {
        JsonNode messageNode = entry.get("message");
        JsonNode instantNode = entry.get("instant");
        if (messageNode == null || instantNode == null) return;

        String message = messageNode.asText();
        String messageUpper = message.toUpperCase();
        long epochSecond = instantNode.get("epochSecond").asLong();
        int nano = instantNode.get("nanoOfSecond").asInt();
        String timestamp = formatter.format(Instant.ofEpochSecond(epochSecond, nano));

        if (messageUpper.contains("[NULL]") && messageUpper.contains("[P3]")) {
            p3Count++;
            if (firstNull == null) {
                firstNull = message;
                firstNullTime = timestamp;
            }
            latestNull = message;
            latestNullTime = timestamp;
        }

        // Treat normal B-PIPE feeds as P3
        if (!messageUpper.contains("[NULL]") && messageUpper.contains("FEED: B-PIPE")) {
            p3Count++;
        }
    }

    @Override public int getP1Count() { return 0; }
    @Override public int getP2Count() { return 0; }
    @Override public int getP3Count() { return p3Count; }

    @Override public String getFirstEvent() { return firstNull; }
    @Override public String getFirstEventTime() { return firstNullTime; }
    @Override public String getLatestEvent() { return latestNull; }
    @Override public String getLatestEventTime() { return latestNullTime; }
}
