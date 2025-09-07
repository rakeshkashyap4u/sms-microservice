package com.rakesh.sms.util;




import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;

public class Logger {  // keep your class name

    public static void sysLog(int severity, String applicationName, String message) {
        try {
            org.slf4j.Logger log = LoggerFactory.getLogger(applicationName);

            switch (severity) {
                case LogValues.fatal:
                    log.error("[FATAL] " + message);
                    break;
                case LogValues.error:
                    log.error(message);
                    break;
                case LogValues.warn:
                    log.warn(message);
                    break;
                case LogValues.info:
                    log.info(message);
                    break;
                case LogValues.debug:
                    log.debug(message);
                    break;
                default:
                    log.trace(message);
            }

        } catch (Throwable e) {
            System.err.println("jHub: Error doing log severity: " + severity +
                    " Application: " + applicationName +
                    " Message: " + message);
            e.printStackTrace();
        }
    }

    public static String getStack(Throwable e) {
        String result = "There was some error creating the Stack Frame";

        try {
            ByteArrayOutputStream stack = new ByteArrayOutputStream();
            PrintStream printStack = new PrintStream(stack);
            e.printStackTrace(printStack);
            result = stack.toString();
        } catch (Exception ex) {
            result += ("\r\n\t" + ex.toString());
        }

        return "\r\n\t" + result.trim();
    }

    public static void setLogLevel(int level) {
        String levelName;
        switch (level) {
            case LogValues.trace: levelName = "TRACE"; break;
            case LogValues.debug: levelName = "DEBUG"; break;
            case LogValues.info:  levelName = "INFO";  break;
            case LogValues.warn:  levelName = "WARN";  break;
            case LogValues.error: levelName = "ERROR"; break;
            case LogValues.fatal: levelName = "ERROR"; break;
            default:              levelName = "INFO";
        }
        System.out.println("⚠️ Runtime log level change not supported in SLF4J. " +
                "Please set logging.level in application.properties. Requested: " + levelName);
    }
}
