package io.javabrains.ipldashboard.data;

import javax.sql.DataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
// batch configuration is for reading ,transforming and writting the data to the database;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.javabrains.ipldashboard.model.Match;

@Configuration
public class batchConfig {

        private final String[] FIELD_NAMES = new String[] {
                        "id", "season", "city", "date", "match_type", "player_of_match", "venue", "team1", "team2",
                        "toss_winner",
                        "toss_decision", "winner", "result", "result_margin", "target_runs", "target_overs",
                        "super_over", "method",
                        "umpire1", "umpire2"
        };

        // @Autowired
        // public JobBuilder jobBuilderFactory;
        // @Autowired
        // public StepBuilder stepBuilderFactory;

        @Bean
        public FlatFileItemReader<MatchInput> reader() {
                return new FlatFileItemReaderBuilder<MatchInput>()
                                .name("MatchDataReader")
                                .resource(new ClassPathResource("matches.csv"))
                                .delimited()
                                .names(FIELD_NAMES)
                                .targetType(MatchInput.class)
                                .build();
        }

        @Bean
        public MatchDataProcessor processor() {
                return new MatchDataProcessor();
        }

        @Bean
        public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
                return new JdbcBatchItemWriterBuilder<Match>()
                                .sql(
                                                "INSERT INTO Match (id, city, date,player_of_match, team1,team2,venue,toss_winner,toss_decision, Matchwinner,  result,result_margin, umpire1,umpire2)"
                                                                + "VALUES ( :id,:city, :date,:playerOfMatch,:team1,:team2,:venue,:tossWinner,:tossDecision, :Matchwinner,:result,:resultMargin,:umpire1,:umpire2)")
                                .dataSource(dataSource)
                                .beanMapped()
                                .build();
        }

        @Bean
        public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
                return new JobBuilder("importUserJob", jobRepository)
                                .listener(listener)
                                .start(step1)
                                .build();
        }

        @Bean
        public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                        JdbcBatchItemWriter<Match> writer) {
                return new StepBuilder("step1", jobRepository)
                                .<MatchInput, Match>chunk(10, transactionManager)
                                .reader(reader())
                                .processor(processor())
                                .writer(writer)
                                .build();
        }

}
