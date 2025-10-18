package org.assignment7;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Parser for EdgeDevice logs.
 * Tracks P1 events (device outages) and their first/latest occurrences.
 */
public class EdgeParser implements LogSourceParser {

    private int p1Count = 0;
    private String firstEdge = null;
    private String latestEdge = null;
    private String firstEdgeTime = "";
    private String latestEdgeTime = "";

    private final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    /**
     * Parses an Edge log file line by line.
     * Detects device outage events (P1).
     *
     * @param file the Edge log file to parse
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
            System.err.println("Error reading Edge file: " + e.getMessage());
        }
    }

    /**
     * Processes a single log entry and updates P1 count and event timestamps.
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

        if (messageUpper.contains("[OUTAGE]") && messageUpper.contains("[P1]")) {
            p1Count++;
            if (firstEdge == null) {
                firstEdge = message;
                firstEdgeTime = timestamp;
            }
            latestEdge = message;
            latestEdgeTime = timestamp;
        }
    }

    @Override public int getP1Count() { return p1Count; }
    @Override public int getP2Count() { return 0; }
    @Override public int getP3Count() { return 0; }

    @Override public String getFirstEvent() { return firstEdge; }
    @Override public String getFirstEventTime() { return firstEdgeTime; }
    @Override public String getLatestEvent() { return latestEdge; }
    @Override public String getLatestEventTime() { return latestEdgeTime; }
}
