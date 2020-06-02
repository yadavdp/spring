package com.batch.demo.steps;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.batch.demo.dto.StudentDto;

@Component
public class ItemWriterStepOne implements ItemWriter<StudentDto>{

	@Override
	public void write(List<? extends StudentDto> items) throws Exception {
		items.stream().forEach(item -> {
			System.out.println("ItemWriter :: email = " + item.getEmailAddress() + ", name = " + item.getName()
					+ ", purchase = " + item.getPurchasedPackage());
		});
	}

}
