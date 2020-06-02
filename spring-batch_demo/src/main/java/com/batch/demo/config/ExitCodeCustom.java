package com.batch.demo.config;

import org.springframework.batch.core.ExitStatus;

public class ExitCodeCustom extends ExitStatus {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4763910989797852550L;
	
	public static final ExitStatus SKIPPED = new ExitStatus("SKIPPED");
	
	public ExitCodeCustom(String exitCode) {
		super(exitCode);
	}

	public ExitCodeCustom(String exitCode, String exitDescription) {
		super(exitCode, exitDescription);
	}

}
