package org.assignment7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class simulates a Refinitiv LDR feed that can produce stale
 * or missing market data. It demonstrates how different severity
 * levels (P2 and P3) appear in logs for client-impacting issues.
 */
public class LdrFeed {
    private static final Logger logger = LogManager.getLogger("LDR_LOGGER");
    /**
     * Simulates a stale data condition for the NVIDIA ticker on the Refinitiv LDR feed.
     * The same bid, ask, and trade prices are logged repeatedly to represent a feed
     * that is frozen and not updating. This is logged as a P2 severity since it means
     * clients are receiving incorrect or outdated data.
     */
    public void simulateStaleNvidia() {
        String ric = "NVDA";
        String layer = "MFA";
        String feed = "LDR";
        double bid = 216.161;
        double ask = 216.164;
        double trd = 216.164;

        while (true) {
            logger.error("[STALE][P2] RIC: {} Layer: {} Feed: {} BID: {} ASK: {} TRD_PRC: {}",
                    ric, layer, feed,
                    String.format("%.3f", bid),
                    String.format("%.3f", ask),
                    String.format("%.3f", trd));
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Simulates valid market data for the AMD ticker on the Refinitiv LDR feed.
     * This feed is healthy, so there are no errors. The log simply shows
     * time, bid, ask, and trade price updates without any severity tags.
     */
    public void simulateValidAMD() {
        double bid = 53.480;
        double ask = 53.485;
        double trd = 53.482;

        while (true) {
            logger.info("Ticker: AMD Feed: LDR TRD_PRC: {} BID: {} ASK: {}",
                    String.format("%.3f", trd),
                    String.format("%.3f", bid),
                    String.format("%.3f", ask));
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }
    }
}