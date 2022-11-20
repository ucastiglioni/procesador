package com.example.procesador.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.logging.Level;


public class LogControl {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(LogControl.class);

    public static void log(String level, String msg) {
        switch (level) {
            case "error":
                logger.error(msg);
                break;
            case "warning":
                logger.warn(msg);
                break;
            case "info":
                logger.info(msg);
                break;
            case "debug":
                logger.debug(msg);
                break;
            case "trace":
                logger.trace(msg);
                break;
            default:
                logger.info(msg);
                break;
        }
    }
}
