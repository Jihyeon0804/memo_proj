package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final int POST_MAX_SIZE = 3;
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManager;
	
	// input : userId
	// output : List<Post>
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		// 게시글 번호 :  10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 현재 내가 4 3 2 페이지에 있을 때
		// 1) 다음 : 현재 페이지 중 가장 작은 2보다 작은 3개 DESC
		// 2) 이전 : 4보다 큰 3개 ASC - 5 6 7 => List reverse - 7 6 5
		// 3) 첫 페이지 : 이전, 다음 없음 => DESC 3개
		
		String direction = null; 	// 방향
		Integer standardId = null;	// 기준이 되는 postId
		
		if (prevId != null) {			// 이전
			direction ="prev";
			standardId = prevId;
			
			List<Post> postList = postMapper.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			
			// reverse 		5 6 7 => 7 6 5
			Collections.reverse(postList); 		// reverse 후 저장
			
			return postList;
		} else if (nextId != null) { 	// 다음
			direction = "next";
			standardId = nextId; 
		}
		
		// 첫 페이지 or 다음
		return postMapper.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	// 이전 페이지의 마지막인가?
	public boolean isPrevLastPageByUserId(int prevId, int userId) {
		int postId = postMapper.selectPostIdByUserIdAndSort(userId, "DESC");
		return postId == prevId;		// 같으면 true, 다르면 false return
	}
	
	
	// 다음 페이지의 마지막인가?
	public boolean isNextLastPageByUserId(int nextId, int userId) {
		int postId = postMapper.selectPostIdByUserIdAndSort(userId, "ASC");
		return postId == nextId;		// 같으면 true, 다르면 false return
	}
	
	
	
	
	
	// input : postId, userId
	// output : Post(단건) or null
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdUserId(postId, userId);
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
	
	// input : params
	// output : X
	public void updatePost(int userId, String userLoginId,
			int postId, String subject, String content, MultipartFile file) {
		
		// 기존에 이미지가 존재한다면 지운 후에 update 해야함
		// 이미지만 update 실패한다면 error를 띄우지 않고 제목과 내용만 변경하고 이미지는 기존에 있던 것으로 return
		
		
		
		// 기존 글을 가져와본다
		// 	1) 이미지 교체 시 삭제 위해
		// 	2) 업데이트 대상이 있는지 확인하기 위해
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		
		if (post == null) {
			logger.error("[글 수정] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}
		 
		
		// 파일이 있다면
		// 1) 새 이미지를 업로드 한다.
		// 2) 새 이미지 업로드 성공 시 기존 이미지 제거(기존 이미지가 존재하는 경우)
		String imagePath = null;
		if (file != null) {
			// 업로드 (로컬에 이미지 저장) ; 업로드 실패 => null 반환
			imagePath = fileManager.saveFile(userLoginId, file);
			
			// 업로드 성공 시 기존 이미지 제거(있으면)
			if (imagePath != null && post.getImagePath() != null) {
				// 업로드가 성공을 했고, 기존 이미지가 존재한다면 => 삭제
				// 기존 이미지 제거 (imagePath 아님)
				fileManager.deleteFile(post.getImagePath());
			}
		}
		
		// DB 글 update
		postMapper.updatePostByPostIdUserId(postId, userId, subject, content, imagePath);
	}
	
	// input : postId, userId
	// output : X
	public void deletePostByPostIdUserId(int postId, int userId) {
		
		// 기존 글 가져오기 => 이미지가 있으면 삭제 해야 하기 때문
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if (post == null) {
			logger.info("[글 삭제] post가 null. postId:{}, userId:{}", postId, userId);
			return;
		}
		
		// 기존 이미지가 존재하면 => 삭제
		if (post.getImagePath() != null) {
			fileManager.deleteFile(post.getImagePath());
		}
		
		// db delete
		postMapper.deletePostByPostIdUserId(postId, userId);
	}
}
