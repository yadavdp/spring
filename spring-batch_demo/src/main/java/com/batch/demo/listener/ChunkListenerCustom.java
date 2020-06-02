package com.batch.demo.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;

public class ChunkListenerCustom implements ChunkListener{

	@Override
	public void beforeChunk(ChunkContext context) {
		System.out.println("beforeChunk Listener");

		context.setAttribute("one", "one:1");
		String[] attrs = context.attributeNames();
		for (int i = 0; i < attrs.length; i++) {
			System.out.println("beforeChunk Listener :: "+attrs[i]);
		}
		StepContext stepContext = context.getStepContext();
		System.out.println("beforeChunk Listener :: stepContext = "+stepContext);
	}

	@Override
	public void afterChunk(ChunkContext context) {
		System.out.println("afterChunk Listener");
		String[] attrs = context.attributeNames();
		for (int i = 0; i < attrs.length; i++) {
			System.out.println("afterChunk Listener :: "+attrs[i]+", val :: "+context.getAttribute(attrs[i]));
		}
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		System.out.println("afterChunkError Listener");
	}

}
