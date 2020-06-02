package com.batch.demo.exception.handler;

import org.springframework.batch.core.launch.support.ExitCodeMapper;

public class ExitCodeMapperCustorm implements ExitCodeMapper{

	@Override
	public int intValue(String exitCode) {
		System.out.println("Exit Code Mapper :: "+exitCode);
		return 0;
	}

}
