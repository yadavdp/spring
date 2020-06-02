package com.batch.demo.steps;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.demo.dto.StudentDto;

@Component
public class ItemProcessorStepOne implements ItemProcessor<StudentDto, StudentDto>{

	@Override
	public StudentDto process(StudentDto item) throws Exception {
		System.out.println("ItemProcessor = "+item);
		item.setEmailAddress("changed");
		return item;
	}

}
