package study.s3imageuploadexample;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/s3/post")
public class PostController {

    private final PostService postService;

    // 게시글 단건 조회
    @GetMapping("/{postId}")
    public String getPost(@PathVariable("postId") Long postId, Model model) {

        model.addAttribute("post", postService.findPost(postId));

        return "home";
    }

    // 게시글 작성
    @ResponseBody
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> savePost(@ModelAttribute PostDto postDto) {

        postService.save(postDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}