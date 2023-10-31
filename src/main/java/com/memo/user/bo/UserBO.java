package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.user.entity.UserEntity;
import com.memo.user.repository.UserRepository;

@Service
public class UserBO {

	@Autowired
	private UserRepository userRepository;
	
	// input : loginId
	// output : UserEntity (null 이거나 Entity)
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId); 
	}
	
	
	// input : 4개의 파라미터 (loginId, password(해싱된 password), name, email)
	// output : id(pk) ; UserEntity보다는 pk인 id를 주는 것이 좋음
	// 부득이하게 id가 null일 수 있으므로 Integer로 지정
	public Integer addUser(String loginId, String password, String name, String email) {
		// UserEntity = save(UserEntity);
		UserEntity userEntity = userRepository.save(
									UserEntity.builder()
									.loginId(loginId)
									.password(password)
									.name(name)
									.email(email)
									.build());
		return userEntity == null ? null : userEntity.getId();
	}
	
	
	
	// input : loginId, password(해싱된 password)
	// output : id(pk) ; null 이거나 int
	public UserEntity getUserEntityByLoginIdPassword(String loginId, String password) {
		UserEntity userEntity =  userRepository.findByLoginIdAndPassword(loginId, password);
//		return userEntity == null ? null : userEntity.getId();
		return userEntity;
	}
}
