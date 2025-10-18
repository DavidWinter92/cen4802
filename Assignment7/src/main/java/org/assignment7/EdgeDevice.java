package org.assignment7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class simulates an Elektron Edge device that can experience
 * a network or service disconnection. A disconnection at this level
 * is considered a P1 severity issue, meaning it is critical and has
 * a high client impact.
 */
public class EdgeDevice {
    private static final Logger logger = LogManager.getLogger("EDGE_LOGGER");

    /**
     * Simulates fatal disconnection events on an Elektron Edge device.
     * The log includes a [FATAL][P1] severity tag to represent a device
     * or network failure that affects multiple clients.
     */
    public void simulateDisconnection() {
        String device = "EDGC0934";
        String endpoint = "iaase005934";

        while (true) {
            String message = String.format(
                    "[OUTAGE][P1] FAIL: Outage '%s'://%s",
                    endpoint, device
            );

            logger.fatal(message);

            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
