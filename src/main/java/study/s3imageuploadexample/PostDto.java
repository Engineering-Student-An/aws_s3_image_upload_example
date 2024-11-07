package study.s3imageuploadexample;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostDto {

    private String title;
    private String content;

    // 다수의 이미지 (리스트) 입력받기 위해 추가
    private List<MultipartFile> imageList;
}
