package sangga.bookstore.scheduler;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvImportJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CsvImportJob.class);
    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job csvImporterJob;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            logger.info("Starting job");
            jobLauncher.run(csvImporterJob, new JobParameters());
            logger.info("Job executed");
        } catch (Exception e) {
            logger.error("Error executing job", e);
        }
    }
}
