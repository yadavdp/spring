package com.batch.demo.steps;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class TaskThreeService implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("TaskThree :: Started");
		String[] attrs = chunkContext.attributeNames();
		for (int i = 0; i < attrs.length; i++) {
			System.out.println("beforeChunk Listener :: "+attrs[i]);
		}
		System.out.println("TaskThree :: Ended");
		return RepeatStatus.FINISHED;
	}
}
