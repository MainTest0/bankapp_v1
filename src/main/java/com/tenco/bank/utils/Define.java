package com.tenco.bank.utils;

public class Define {
	
	// 한번 선언하면 변경할 수 없도록 만들기 -> 상수
	// 서버 프로그램에서 상태값을 변경할 수 있는 변수는 절대 지양
	public final static String PRINCIPAL = "principal";
	
	// 이미지 처리 관련
	public final static String UPLOAD_DIRECTORY="C:\\spring_upload\\bank\\upload";
	public final static int MAX_FILE_SIZE = 1024 * 1024 * 20; // 20MB
	// 1byte = 8bit
	// 1024 = 1KB 
	// 1,048,576 = 1MB ( 1024 * 1024 )

}
