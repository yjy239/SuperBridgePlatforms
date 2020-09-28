package com.yjy.bridgeregister.utils
import org.gradle.api.Project

class Logger {
    static org.gradle.api.logging.Logger logger
    static boolean isEnable

    static void make(Project project,boolean enable) {
        logger = project.getLogger()
        isEnable = enable
    }

    static void i(String info) {
        if (isEnable&&null != info && null != logger) {
            logger.info("SuperBridge::Register >>> " + info)
        }
    }

    static void e(String error) {
        if (isEnable&&null != error && null != logger) {
            logger.error("SuperBridge::Register >>> " + error)
        }
    }

    static void w(String warning) {
        if (isEnable&&null != warning && null != logger) {
            logger.warn("SuperBridge::Register >>> " + warning)
        }
    }
}