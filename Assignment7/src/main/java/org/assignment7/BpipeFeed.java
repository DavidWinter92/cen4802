package org.assignment7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;

/**
 * This class simulates a Bloomberg B-PIPE market data feed.
 * It produces normal bid, ask, and trade price updates to represent
 * live price changes for a ticker such as NVIDIA.
 * <p>
 * This feed does not have any client-impacting errors, so it does not
 * include severity-level log tags. It only logs regular data updates.
 */
public class BpipeFeed {
    private static final Logger logger = LogManager.getLogger("BPIPE_LOGGER");
    private final Random random = new Random();

    /**
     * Simulates normal market updates for the NVIDIA ticker on the Bloomberg B-PIPE feed.
     * The prices fluctuate randomly between 219.000 and 227.000, changing by up to ten cents
     * per update. The method prints out bid, ask, and trade price (TRD_PRC) values to represent
     * regular activity on a healthy feed.
     */
    public void simulateNvidiaFeed() {
        String ticker = "NVDA";
        String layer = "MBPIPE";
        String feed = "B-PIPE";
        double bid = 219.000;
        double ask = 219.100;

        while (true) {
            double tradePrice = bid + (ask - bid) / 2;

            logger.info("TICKER: {} LAYER: {} FEED: {} BID: {} ASK: {} TRD_PRC: {}",
                    ticker, layer, feed,
                    String.format("%.3f", bid),
                    String.format("%.3f", ask),
                    String.format("%.3f", tradePrice));

            double nextChange = (random.nextDouble() * 0.10);
            bid += nextChange;
            ask += nextChange;

            if (ask > 227.000) {
                bid = 219.000;
                ask = 219.100;
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Simulates a missing data situation (blank TRD_PRC, BID, and ASK values)
     * for the AMD ticker on the Bloomberg B-PIPE feed.
     * <p>
     * This issue is recorded as a P3 severity level because it may prevent
     * a client from subscribing to the ticker, but does not cause stale or
     * incorrect data to be delivered.
     */
    public void simulateNullDataAMD() {
        while (true) {
            logger.error("[NULL][P3] Feed: B-PIPE Ticker: AMD TRD_PRC: <blank_data> BID: <blank_data> ASK: <blank_data>");
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
