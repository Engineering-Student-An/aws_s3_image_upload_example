package study.s3imageuploadexample;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    // Amazon S3 클라이언트를 주입받음
    private final AmazonS3 amazonS3;

    // S3 버킷 이름을 application.yml에서 읽어옴
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    // 이미지 파일을 S3에 업로드하는 메서드
    @Transactional
    public List<String> uploadImages(List<MultipartFile> imageList) throws IOException {

        // 업로드한 파일 리스트가 null인 경우, null을 반환
        if (imageList == null) return null;

        // 업로드 파일이 이미지 형식인지 확인
        if (!isImageFile(imageList)) throw new IllegalArgumentException("사진 이외의 파일은 업로드 불가능합니다!");

        // S3에 업로드된 이미지 URL을 저장할 리스트 생성
        List<String> imageUrlList = new ArrayList<>();

        // 각 이미지 파일을 S3에 업로드
        for (MultipartFile image : imageList) {

            // 원본 파일 이름 가져오기
            String originalFileName = image.getOriginalFilename();

            // 중복을 피하기 위해 UUID를 추가한 새로운 파일 이름 생성
            String modifiedFileName = UUID.randomUUID() + "_" + originalFileName;

            // S3에 업로드할 PutObjectRequest 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, modifiedFileName,
                    image.getInputStream(), null) // InputStream과 메타데이터(null)를 사용하여 요청 생성
                    .withCannedAcl(CannedAccessControlList.PublicRead); // 파일을 공개적으로 읽을 수 있도록 설정

            // S3에 파일 업로드
            amazonS3.putObject(putObjectRequest);

            // 업로드된 파일의 URL을 리스트에 추가
            imageUrlList.add(String.valueOf(amazonS3.getUrl(bucketName, modifiedFileName)));
        }

        // 업로드된 이미지의 URL 리스트 반환
        return imageUrlList;
    }

    // 파일이 이미지 형식인지 검사하는 메서드
    private static boolean isImageFile(List<MultipartFile> imageList) {
        // 각 이미지 파일의 MIME 타입을 검사
        for (MultipartFile image : imageList) {
            String contentType = image.getContentType();
            // MIME 타입이 null이거나 'image/'로 시작하지 않으면 false 반환
            if (contentType == null || !contentType.startsWith("image/")) return false;
        }

        // 모든 파일이 이미지 형식이라면 true 반환
        return true;
    }
}
