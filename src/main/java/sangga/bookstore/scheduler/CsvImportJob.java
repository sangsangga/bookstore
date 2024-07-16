package sangga.bookstore.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvImportJob implements Job {
    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job csvImporterJob;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("Starting job");
            jobLauncher.run(csvImporterJob, new JobParameters());
            log.info("Job executed");
        } catch (Exception e) {
            log.error("Error executing job", e);
        }
    }
}
