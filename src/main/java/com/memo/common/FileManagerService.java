package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component		// spring bean(불러올 때는 Autowired 사용)
public class FileManagerService {

	private Logger logger = LoggerFactory.getLogger(FileManagerService.class);
	
	// 실제 업로드가 된 이미지가 저장될 경로(서버)
	// !!경로 마지막에 반드시 / 붙이기!!
	public static final String FILE_UPLOAD_PATH = "D:\\김지현\\5_spring_project\\memo\\workspace\\images/";
	
	// input : userLoginId, file(이미지)
	// output : web imagePath
	public String saveFile(String loginId, MultipartFile file) {
		
		// 서버에 폴더 생성
		// aaaa_시간(ms 단위로 지정하지만 겹칠 수도 있으니 사용자명과 결합)
		String directoryName = loginId + "_" + System.currentTimeMillis();
		// FILE_UPLOAD_PATH 마지막에 / 를 붙였으므로 바로 directoryName 이어 붙여주면 됨
		// D:\\김지현\\5_spring_project\\memo\\workspace\\images/aaaa_1698922577076 형태
		String filePath = FILE_UPLOAD_PATH + directoryName;
		
		File directory = new File(filePath);
		if (directory.mkdir() == false) {
			// 폴더 생성 실패 시 이미지 경로 null로 리턴
			return null;
		}
		
		
		// 생성된 폴더 내에 파일 업로드 : byte 단위로 업로드
		try {
			byte[] bytes = file.getBytes();
			// ★★★★ 한글 이름 이미지를 올릴 수 없으므로 나중에 영문자로 바꿔서 올리기 ★★★★
			Path path = Paths.get(filePath + "/" + file.getOriginalFilename());	// 디렉토리 경로 + 사용자가 올린 파일명(확장자 포함)
			Files.write(path, bytes); // 파일 업로드
		} catch (IOException e) {
			logger.error("[이미지 업로드] 업로드 실패 loginId:{}, filePath:{}", loginId, filePath);
			return null;	// 이미지 업로드 실패 시 null 리턴
		}
		
		
		// 파일 업로드 성공 시 웹 이미지 url path 리턴
		// 주소는 이렇게 될 것이다.(예언)
		// /images/aaaa_1698922577076/sun.png
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
	}
	
	// 이미지 삭제
	// input : imagePath - /images/aaaa_1698923956245/chess-8348280_640.jpg
	// output : X
	public void deleteFile(String imagePath) {
		// D:\\김지현\\5_spring_project\\memo\\workspace\\images/aaaa_1698923956245/chess-8348280_640.jpg
		// 주소에 겹치는 /images/ 지운다.
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		
		// 폴더 안에 있는 이미지 제거 후 폴더 제거
		if (Files.exists(path)) {	// 이미지가 존재하는가
			// 이미지 삭제
			try {
				Files.delete(path);
			} catch (IOException e) {
				logger.error("[이미지 삭제] 파일 삭제 실패 imagePath:{}", imagePath);
				return;
			}
			
			// 폴더(디렉토리) 삭제
			path = path.getParent();
			if(Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					logger.error("[이미지 삭제] 디렉토리 삭제 실패 imagePath:{}", imagePath);
				}
			}
		}
	}
}
