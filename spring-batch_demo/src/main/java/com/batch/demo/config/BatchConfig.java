package com.batch.demo.config;

import java.util.LinkedList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import com.batch.demo.dto.Person;
import com.batch.demo.dto.StudentDto;
import com.batch.demo.exceptions.SkipStepException;
import com.batch.demo.listener.ChunkListenerCustom;
import com.batch.demo.listener.JobExecutionListenerCustom;
import com.batch.demo.listener.StepExecutionListenerCustom;
import com.batch.demo.mapper.StudentDtoLineMapper;
import com.batch.demo.steps.ItemProcessorStepOne;
import com.batch.demo.steps.ItemReaderStepOne;
import com.batch.demo.steps.ItemWriterStepOne;
import com.batch.demo.steps.TaskThreeService;
import com.batch.demo.steps.TaskTwoService;
import com.batch.demo.validator.JobParameterValidatorCustom;

@Configuration
@EnableBatchProcessing
@Order(value = 1)
public class BatchConfig implements CommandLineRunner{
	
	@Autowired
	public JobBuilderFactory jobs;
	
	@Autowired
	public StepBuilderFactory steps;
	
	@Autowired
	public JobLauncher jobJauncher;
	
	@Autowired
	public JobExecutionListenerCustom jobListener;
	
	@Autowired
	public StepExecutionListenerCustom stepListener;
	
	@Autowired
	public JobParameterValidatorCustom jobParamValidator;
	
	@Autowired
	public ItemReaderStepOne readerStepOne;
	
	@Autowired
	public ItemProcessorStepOne processorStepOne;
	
	@Autowired
	public ItemWriterStepOne writerStepOne;
	
	@Autowired
	@Qualifier("decider")
	public JobExecutionDeciderCustom decider;
	
	FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow");
	
	@Bean
	public Step stepOne() {
		return steps.get("stepOne")
				.listener(stepListener)
				.<StudentDto,StudentDto>chunk(3)
				.reader(readerStepOne)
				.processor(processorStepOne)
				.writer(writerStepOne)
				.faultTolerant()
				.skipLimit(1)
				.skip(Exception.class)
				.noSkip(SkipStepException.class)
				.retryLimit(3)
				.retry(DeadlockLoserDataAccessException.class)
				.noRollback(ValidationException.class)
				
//				.readerIsTransactionalQueue() // when exception occurred while processing an item read from queue will be rolled back and put in queue
//				.tasklet(new TaskOneService())
				.startLimit(1) //restart limit configured as 1, this step will be executed once during the job execution
				.build();
	}
	
	@Bean
	public Step stepTwo() {
		DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
		attribute.setPropagationBehavior(Propagation.REQUIRED.value());
		attribute.setIsolationLevel(Isolation.DEFAULT.value());
		attribute.setTimeout(30);
		
		return steps.get("stepTwo")
				.listener(stepListener)
				.tasklet(new TaskTwoService())
				.transactionAttribute(attribute)
				.build();
	}
	
	@Bean
	public Step stepThree() {
		return steps.get("stepThree")
				.listener(stepListener)
				.tasklet(new TaskThreeService())
				.allowStartIfComplete(Boolean.TRUE)
				.listener(new ChunkListenerCustom())
				.build();
	}
	
	@Bean
	public Step stepFour() {
		return steps.get("stepFour").listener(stepListener).tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("TaskFour :: Started");
				System.out.println("TaskFour :: Ended");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step stepFive() {
		return steps.get("stepFive").listener(stepListener).tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("TaskFive :: Started");
				System.out.println("TaskFive :: Ended");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	/**
	 * FlatFileItemReader file reader from a scv file
	 * @return
	 */
	@Bean
	public FlatFileItemReader<Person> reader() {
		return new FlatFileItemReaderBuilder<Person>().name("personFileReader")
				.resource(new ClassPathResource("person.csv"))
				.delimited()
				.names(new String[] {"id","name","age","address","mobile"})
				.linesToSkip(1)
				.targetType(Person.class)
				.lineMapper(new DefaultLineMapper<>())
				.saveState(true)
				.build();
	}
	
	@Bean
	@DependsOn(value = {"studentDtoMapper"})
	public FlatFileItemReader<StudentDto> studentDtoReader(BeanWrapperFieldSetMapper mapper){
		return new FlatFileItemReaderBuilder().name("studentDtoReader")
				.fieldSetMapper(mapper)
				.lineMapper(new StudentDtoLineMapper().setTokenizer(new DelimitedLineTokenizer()).setFieldSetMapper(mapper))
				.build();
	}
	
	@Bean("studentDtoMapper")
	public BeanWrapperFieldSetMapper fieldSetMapper() {
		BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper();
		mapper.setTargetType(StudentDto.class);
		mapper.setPrototypeBeanName("prototype");
		return mapper;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public CompositeItemProcessor  compositeProcess(){
		CompositeItemProcessor<Person, StudentDto> cip = new CompositeItemProcessor<>();
		
		List processors = new LinkedList();
		processors.add(new ItemProcessor<Person, StudentDto>() {

			@Override
			public StudentDto process(Person item) throws Exception {
				System.out.println("composite :: "+item);
				StudentDto std = new StudentDto();
				std.setName(item.getName());
				std.setEmailAddress(item.getName()+item.getMobile()+"@gmail.com");
				std.setPurchasedPackage(item.getAddress()+item.getAge());
				return std;
			}
		});
		processors.add(processorStepOne);
		
		cip.setDelegates(processors);
		return cip;
		
	}
	
	@SuppressWarnings("unchecked")
	@Bean
	public Step fileReaderStep() {
		return steps.get("personfileReaderStep").<Person,Person>chunk(3)
				.reader(reader())
				.processor(compositeProcess())
				.writer(writerStepOne)
				.build();
	}
	/**
	 * first under this demoJob for Job execution
	 */
//	@Bean
//	public Job demoJob() {
//		return jobs.get("demoJob")
//		.listener(jobListener)
//		.incrementer(new RunIdIncrementer())
//		.validator(jobParamValidator)
//		.start(stepOne())
//		.next(stepTwo())
//		.next(stepThree())
////		.preventRestart()
//		.build();
//	}
	
	/**
	 * second under this demoJob for Job execution
	 */
//	@Bean
//	public Job demoJob() {
//		Step step1 = stepOne();
//		Step step2 = stepTwo();
//		Step step3 = stepThree();
//		return jobs.get("demoJob")
//		.listener(jobListener)
//		.incrementer(new RunIdIncrementer())
//		.validator(jobParamValidator)
//				.start(step1).on("FAILED").fail()/* .end() */ //.fail() equal to BatchStatus with failed and end() equals to BatchStatus to COMPLETED
//		.from(step1).on("*").to(step2)
//		.from(step2).on("SKIPPED").to(step3)
//		.from(step2).on("SKIPPED3").end()
//		.from(step3).on("*").end()
//		.end()
////		.preventRestart() prevent restart of this job
//		.build();
//	}
	/**
	 * third read it 
	 * @return
	 */
//	@Bean
//	public Job demoJob() {
//		System.out.println("decider = ");
//		Step step1 = stepOne();
//		Step step2 = stepTwo();
//		Step step3 = stepThree();
//		Step step4 = stepFour();
//		Step step5 = stepFive();
//		
//		return jobs.get("demoJob")
//		.listener(jobListener)
//		.incrementer(new RunIdIncrementer())
//		.validator(jobParamValidator)
//		.flow(step1)
//		.next(decider).on("FAILED").fail()/* .end() */ //.fail() equal to BatchStatus with failed and end() equals to BatchStatus to COMPLETED
//			.from(decider).on("SKIPPED").to(step2)
//			.next(step3).on("COMPLETED").to(step4)
//			.from(step3).on("UNKNOWN").to(step5)
//			.from(decider).on("FAILED").fail()
//			.from(decider).on("*").end()
//		.end()
////		.preventRestart() prevent restart of this job
//		.build();
//	}
	
	/**
	 * fourth rea it
	 * @return
	 */
//	@Bean
//	public Job demoJob() {
//		System.out.println("decider = ");
//		Step step1 = stepOne();
//		Step step2 = stepTwo();
//		Step step3 = stepThree();
//		Step step4 = stepFour();
//		Step step5 = stepFive();
//		
//		Flow flow1 = flowBuilder
//				.start(step1).on("FAILED").fail()
//				.from(step1).on("COMPLETED").to(step2).end();
//		
//		Flow flow2 = flowBuilder.from(step2)
//		.next(decider)
//			.on("FAILED").fail()
//		.from(decider)
//			.on("COMPLETED").to(step3).build();
//		
//		Flow flow3 = flowBuilder.from(step3)
//				.next(decider)
//					.on("FAILED").fail()
//				.from(decider)
//					.on("COMPLETED").to(step4).end();
//						
//		
//		return jobs.get("demoJob")
//		.listener(jobListener)
//		.incrementer(new RunIdIncrementer())
//		.validator(jobParamValidator)
//		.start(flow1)
//		.next(decider).on("FAILED").fail()/* .end() */ //.fail() equal to BatchStatus with failed and end() equals to BatchStatus to COMPLETED
//		.from(decider).on("COMPLETED").to(flow2)
//		.from(decider).on("flow3").to(flow3)
//		.end()
//		.build();
//	}
	
	@Bean
	public Job demoJob() {
		return jobs.get("fileJob")
		.listener(jobListener)
		.incrementer(new RunIdIncrementer())
		.validator(jobParamValidator)
		.start(fileReaderStep())
		.next(stepOne())
		.next(stepTwo())
//		.next(stepThree())
//		.preventRestart()
		.build();
	}
	
	@Bean
	public JobExecutionDeciderCustom decider() {
		return new JobExecutionDeciderCustom();
	}
	@Override
	public void run(String... args) throws Exception {
		
	}

}
