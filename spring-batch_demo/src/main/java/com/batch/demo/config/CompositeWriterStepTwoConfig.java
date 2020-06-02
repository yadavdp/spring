//package com.batch.demo.config;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.support.CompositeItemWriter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CompositeWriterStepTwoConfig {
//	
//	@Bean
//	public CompositeItemWriter<List<ItemWriter>> compositeItemWriter(){
//		List<ItemWriter> writers = new ArrayList<>(2);
//		writers.add(fileWriter1());
//		writers.add(fileWriter2());
//
//		CompositeItemWriter itemWriter = new CompositeItemWriter();
//
//		itemWriter.setDelegates(writers);
//
//		return itemWriter;
//	}
//	
//	public ItemWriter fileWriter1() {
//		ItemWriter<StudentDto> writer2 = new ItemWriter
//		URL url =getClass().getResource("../filewriter1.txt");
//		try {
//			BufferedWriter bw = Files.newBufferedWriter(Paths.get(url.getPath()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public ItemWriter fileWriter1() {
//		
//	}
//}
