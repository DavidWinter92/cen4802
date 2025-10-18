package org.assignment7;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Parser for LDR (London Data Repository) feed logs.
 * Tracks P2 events (stale tickers) and their first/latest occurrences.
 */
public class LdrParser implements LogSourceParser {

    private int p2Count = 0;
    private String firstStale = null;
    private String latestStale = null;
    private String firstStaleTime = "";
    private String latestStaleTime = "";

    private final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    /**
     * Parses an LDR log file line by line.
     * Detects stale tickers (P2) and counts normal feed messages as P2 events.
     *
     * @param file the LDR log file to parse
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
            System.err.println("Error reading LDR file: " + e.getMessage());
        }
    }

    /**
     * Processes a single log entry and updates P2 count and event timestamps.
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

        if (messageUpper.contains("[STALE]") && messageUpper.contains("[P2]")) {
            p2Count++;
            if (firstStale == null) {
                firstStale = message;
                firstStaleTime = timestamp;
            }
            latestStale = message;
            latestStaleTime = timestamp;
        }

        // Treat normal LDR feeds as P2
        if (!messageUpper.contains("[STALE]") && messageUpper.contains("FEED: LDR")) {
            p2Count++;
        }
    }

    @Override public int getP1Count() { return 0; }
    @Override public int getP2Count() { return p2Count; }
    @Override public int getP3Count() { return 0; }

    @Override public String getFirstEvent() { return firstStale; }
    @Override public String getFirstEventTime() { return firstStaleTime; }
    @Override public String getLatestEvent() { return latestStale; }
    @Override public String getLatestEventTime() { return latestStaleTime; }
}
