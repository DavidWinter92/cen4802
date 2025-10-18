package org.assignment7;

import java.io.File;

/**
 * Interface representing a parser for a specific log source.
 * Each parser processes its own feed log file (BPIPE, LDR, or EdgeDevice)
 * and tracks event counts and first/latest occurrences.
 */
public interface LogSourceParser {

    /**
     * Parses a log file and updates counts and event tracking.
     *
     * @param file the log file to parse
     */
    void parse(File file);

    /**
     * Returns the number of P1 (high severity) events detected.
     *
     * @return P1 event count
     */
    int getP1Count();

    /**
     * Returns the number of P2 (medium severity) events detected.
     *
     * @return P2 event count
     */
    int getP2Count();

    /**
     * Returns the number of P3 (low severity) events detected.
     *
     * @return P3 event count
     */
    int getP3Count();

    /**
     * Returns the first event message detected.
     *
     * @return first event message
     */
    String getFirstEvent();

    /**
     * Returns the timestamp of the first event detected.
     *
     * @return timestamp of first event
     */
    String getFirstEventTime();

    /**
     * Returns the latest event message detected.
     *
     * @return latest event message
     */
    String getLatestEvent();

    /**
     * Returns the timestamp of the latest event detected.
     *
     * @return timestamp of latest event
     */
    String getLatestEventTime();
}
