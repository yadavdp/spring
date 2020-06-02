package com.batch.demo.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionListenerCustom implements JobExecutionListener{

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("before Job execution Listener");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("after Job execution Listener");
	}

}
