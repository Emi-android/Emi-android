package com.hcb.util;

import org.slf4j.Logger;

import java.util.Arrays;

public class LoggerUtil {

	public static void t(final Logger log, Object... args) {
		if (log.isTraceEnabled() && null != args && args[0] instanceof String) {
			final String info = (String) args[0];
			if (args.length == 1) {
				log.trace(info);
			} else {
				log.trace(info, tailArray(args, 1));
			}
		}
	}

	public static void d(final Logger log, Object... args) {
		if (log.isDebugEnabled() && null != args && args[0] instanceof String) {
			final String info = (String) args[0];
			if (args.length == 1) {
				log.debug(info);
			} else {
				log.debug(info, tailArray(args, 1));
			}
		}
	}

	public static void i(final Logger log, Object... args) {
		if (log.isInfoEnabled() && null != args && args[0] instanceof String) {
			final String info = (String) args[0];
			if (args.length == 1) {
				log.info(info);
			} else {
				log.info(info, tailArray(args, 1));
			}
		}
	}

	private static Object[] tailArray(final Object[] args, final int start) {
		return Arrays.copyOfRange(args, start, args.length);
	}

}
