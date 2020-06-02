package com.batch.demo.mapper;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.batch.demo.dto.StudentDto;

public class StudentDtoLineMapper implements LineMapper<StudentDto>, InitializingBean{
	
	private LineTokenizer tokenizer;
	
	private FieldSetMapper<StudentDto> fieldSetMapper;

	@Override
	public StudentDto mapLine(String line, int lineNumber) throws Exception {
		System.out.println("StudentDtoLineMapper --> mapLine -> line-no: "+lineNumber+" , line: "+line);
		return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
	}

	/**
	 * @param tokenizer the tokenizer to set
	 */
	public StudentDtoLineMapper setTokenizer(LineTokenizer tokenizer) {
		this.tokenizer = tokenizer;
		return this;
	}

	/**
	 * @param fieldSetMapper the fieldSetMapper to set
	 */
	public StudentDtoLineMapper setFieldSetMapper(FieldSetMapper<StudentDto> fieldSetMapper) {
		this.fieldSetMapper = fieldSetMapper;
		return this;
	}
	
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(tokenizer, "The LineTokenizer must be set");
		Assert.notNull(fieldSetMapper, "The FieldSetMapper must be set");
	}
}
