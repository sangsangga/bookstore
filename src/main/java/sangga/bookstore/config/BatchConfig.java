package sangga.bookstore.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.TransactionManager;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import sangga.bookstore.model.Book;
import sangga.bookstore.repository.BookRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Bean
    public FlatFileItemReader<Book> reader (){
        return new FlatFileItemReaderBuilder<Book>()
                .name("bookItemReader")
                .resource(new ClassPathResource("books.csv"))
                .delimited()
                .delimiter(",")
                .names("title", "author", "isbn", "publisher", "publicationYear", "price")
                .linesToSkip(1)
                .fieldSetMapper(fieldSet -> {
                    return Book
                            .builder()
                            .title(fieldSet.readString("title"))
                            .author(fieldSet.readString("author"))
                            .isbn(fieldSet.readString("isbn"))
                            .price(fieldSet.readDouble("price"))
                            .publicationYear(fieldSet.readInt("publicationYear"))
                            .publisher(fieldSet.readString("publisher"))
                            .build();

                })
                .build();
    }

    @Bean
    public JpaItemWriter<Book> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Book>()
                .entityManagerFactory(entityManagerFactory)
                .build();

    }

    @Bean
    public Step csvImporterStep(PlatformTransactionManager transactionManager,
                                ItemWriter<Book> writer,
                                ItemReader<Book> reader,
                                JobRepository jobRepository
                                ){
        return new StepBuilder("csvImporterStep", jobRepository)
                .<Book, Book>chunk(50, transactionManager)
                .reader(reader)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job csvImporterJob(Step csvImporterStep, JobRepository jobRepository) {
        return new JobBuilder("csvImporterJob", jobRepository).incrementer(new RunIdIncrementer()).flow(csvImporterStep).build().build();

    }
}
