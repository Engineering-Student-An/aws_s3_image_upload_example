package study.s3imageuploadexample;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3ImageService s3ImageService;

    public Post findPost(Long id) {

        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(PostDto postDto) {

        Post post = new Post(postDto.getTitle(), postDto.getContent());

        // 이미지 저장 로직 추가
        try {
            List<String> imageUrlList = s3ImageService.uploadImages(postDto.getImageList());
            post.setImageUrlList(imageUrlList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        postRepository.save(post);

    }
}
