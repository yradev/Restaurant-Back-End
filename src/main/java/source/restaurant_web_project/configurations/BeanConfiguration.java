package source.restaurant_web_project.configurations;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Properties;

@Configuration
@EnableScheduling
public class BeanConfiguration {
    @Value("${aws.bucket-region}")
    private String awsRegion;
    @Value("${aws.access-key}")
    private String awsAccessKey;
    @Value("${aws.secret-key}")
    private String awsSecretKey;

    @Value("${aws.endpoint}")
    private String awsEndpoint;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setPropertyCondition(Conditions.not(a -> a.getSource() == null || a.getSource().toString().equals("0")));

        return modelMapper;
    }

    @Bean
    public ResourceBundleMessageSource bundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender javaMailSender(@Value("${spring.mail.host}") String host,
                                         @Value("${spring.mail.port}") int port,
                                         @Value("${spring.mail.username}") String username,
                                         @Value("${spring.mail.password}") String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.ssl.trust", host);

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

    @Bean
    public AmazonS3 amazonS3(){

        BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setSignerOverride("AWSS3V4SignerType");

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsEndpoint,awsRegion))
                .withClientConfiguration(clientConfig)
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
