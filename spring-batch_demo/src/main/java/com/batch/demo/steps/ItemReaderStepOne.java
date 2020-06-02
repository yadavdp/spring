package com.batch.demo.steps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;

import com.batch.demo.dto.StudentDto;

@Component
public class ItemReaderStepOne implements ItemReader<StudentDto>, ItemStream {
	private int nextStudentIndex;
	private List<StudentDto> studentData;

	ItemReaderStepOne() {
		initialize();
	}

	private void initialize() {
		StudentDto tony = new StudentDto();
		tony.setEmailAddress("tony.tester@gmail.com");
		tony.setName("Tony Tester");
		tony.setPurchasedPackage("master");

		StudentDto nick = new StudentDto();
		nick.setEmailAddress("nick.newbie@gmail.com");
		nick.setName("Nick Newbie");
		nick.setPurchasedPackage("starter");

		StudentDto ian = new StudentDto();
		ian.setEmailAddress("ian.intermediate@gmail.com");
		ian.setName("Ian Intermediate");
		ian.setPurchasedPackage("intermediate");

		studentData = Collections.unmodifiableList(Arrays.asList(tony, nick, ian));
		nextStudentIndex = 0;
	}

	@Override
	public StudentDto read() throws Exception {
		System.out.println("ItemReaderStepOne :: index = "+nextStudentIndex+", size = "+studentData.size());
		StudentDto nextStudent = null;

		if (nextStudentIndex < studentData.size()) {
			nextStudent = studentData.get(nextStudentIndex);
			nextStudentIndex++;
		}

		return nextStudent;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		System.out.println("ItemReaderStepOne --> open");
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		System.out.println("ItemReaderStepOne --> update");
	}

	@Override
	public void close() throws ItemStreamException {
		System.out.println("ItemReaderStepOne --> close");
	}
}
