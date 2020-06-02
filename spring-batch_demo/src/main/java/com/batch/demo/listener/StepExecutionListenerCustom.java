package com.batch.demo.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @author dp
 * @description @BeforeStep and @AfterStep can be used instead of implementing StepExeuctionListener or StepListener
 *
 */
@Component
public class StepExecutionListenerCustom implements StepExecutionListener {
	
	int count =1;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("before Step execution Listener "+stepExecution.getExecutionContext());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("after Step execution Listener stepName: "+stepExecution.getStepName()+", se= "+stepExecution.getExecutionContext());
//		return ExitStatus.FAILED;
		return new ExitStatus("COMPLETED","custom skipped afterStep");
	}

}
