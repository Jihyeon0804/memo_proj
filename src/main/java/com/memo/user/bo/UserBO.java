package com.memo.user.bo;

import org.springframework.stereotype.Service;

import com.memo.user.entity.UserEntity;

@Service
public class UserBO {

	// input : loginId
	// output : UserEntity (null 이거나 Entity)
	public UserEntity getUserEntityByLoginId(String loginId) {
		return null; 
	}
}
