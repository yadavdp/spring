package com.batch.demo.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import com.batch.demo.dto.StudentDto;

/**
 * @author dp
 * @document use to provide functionality when exception occurred or to skip a step
 * @use either implement SkipListener or use @OnSkipInRead, @OnSkipInWrite, @OnSkipInProcess
 */
public class SkipListenerCustom /* implements SkipListener<StudentDto, StudentDto> */{

	@OnSkipInRead
	public void onSkipInRead(Throwable t) {
		System.out.println("OnSkipInRead");
	}

	@OnSkipInWrite
	public void onSkipInWrite(StudentDto item, Throwable t) {
		System.out.println("OnSkipInWrite :: item = "+item);
	}

	@OnSkipInProcess
	public void onSkipInProcess(StudentDto item, Throwable t) {
		System.out.println("OnSkipInProcess :: item = "+item);	
	}

}
