package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManager;
	
	// input : userId
	// output : List<Post>
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
	}
	
	// input : params(userId, subject content)
	// output :
	public void addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		
		String imagePath = null;
		
		// 이미지가 있으면 업로드 로직 => 클래스로 분리하여 재활용
		if (file != null) {
			imagePath = fileManager.saveFile(userLoginId, file);
		}
		
		
		// 이미지가 없으면 null인 상태로 넘기기
		postMapper.insertPost(userId, subject, content, imagePath);
	}
}
