package org.usayi.preta.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@SuppressWarnings( "unused")
public class BatchConfiguration
{
	  @Autowired
	  private JobBuilderFactory jobBuilderFactory;

	  @Autowired
	  private StepBuilderFactory stepBuilderFactory;
	  
//	  @Bean
//	  public Step step1() {
//		  return stepBuilderFactory.get( "step1")
//				  				   .tasklet( new ExpiryReminderTasklet())
//				  				   .build();
//	  }
//	  
//	  @Bean
//	  public Job job1( Step step1) throws Exception
//	  {
//		  return jobBuilderFactory.get( "job1")
//				 .incrementer( new RunIdIncrementer())
//				 .start(step1)
//				 .build();
//	  }
}
