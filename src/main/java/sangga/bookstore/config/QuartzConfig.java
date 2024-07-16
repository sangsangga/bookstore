package sangga.bookstore.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sangga.bookstore.scheduler.CsvImportJob;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail csvImportJobDetail() {
        return JobBuilder.newJob(CsvImportJob.class)
                .withIdentity("csvImportJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger csvImportJobTrigger(JobDetail csvImportJobDetail) {
        Date triggerTime = Date.from(LocalDateTime.now().plusSeconds(10).atZone(ZoneId.systemDefault()).toInstant());

        return TriggerBuilder
                .newTrigger()
                .startAt(triggerTime)
                .forJob(csvImportJobDetail)
                .withIdentity("csvImportJobTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever())
                .build();
    }
}
