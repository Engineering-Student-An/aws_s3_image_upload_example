package study.s3imageuploadexample;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    // AWS 자격 증명을 application.yml에서 읽어오기 위한 필드
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;


    @Bean
    // AmazonS3 클라이언트를 생성하는 메서드
    public AmazonS3 amazonS3() {

        // AWS 자격 증명을 기본 자격 증명 클래스를 사용하여 생성
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        // AmazonS3 클라이언트 빌더를 사용하여 S3 클라이언트 구성
        return AmazonS3ClientBuilder
                .standard() // 기본 클라이언트 빌더 사용
                .withCredentials(new AWSStaticCredentialsProvider(credentials)) // 인증 정보 제공
                .withRegion(region) // 사용할 AWS 리전 설정
                .build();
    }
}