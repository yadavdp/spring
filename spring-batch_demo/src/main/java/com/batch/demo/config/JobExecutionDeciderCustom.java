package com.batch.demo.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//@Component("decider")
@Slf4j
public class JobExecutionDeciderCustom implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		log.info("JobExecutionDeciderCustom --> decide -> status0");
		log.info("JobExecutionDeciderCustom --> decide -> status1: {}",jobExecution.getExecutionContext());
		ExitStatus status = stepExecution.getExitStatus();
		String stepName = stepExecution.getStepName();
		log.info("JobExecutionDeciderCustom --> decide -> status2: {}", status);
		FlowExecutionStatus flowExStatus = null;
		switch (stepName) {
		case "stepOne":
			flowExStatus = new FlowExecutionStatus("SKIPPED");
			break;
		case "stepTwo":
			flowExStatus = new FlowExecutionStatus("SKIPPED3");
			break;
		case "stepThree":
			flowExStatus = FlowExecutionStatus.COMPLETED;
			break;
		case "stepFour":
			flowExStatus = FlowExecutionStatus.UNKNOWN;
			break;
		case "stepFive":
			flowExStatus = new FlowExecutionStatus("five");
			break;
		default:
			flowExStatus = FlowExecutionStatus.FAILED;
			break;
		}
		log.info("JobExecutionDeciderCustom --> decide -> status3: {}", flowExStatus);
		return flowExStatus;
	}

}
