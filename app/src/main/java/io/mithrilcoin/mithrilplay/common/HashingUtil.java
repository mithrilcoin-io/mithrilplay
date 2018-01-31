package io.mithrilcoin.mithrilplay.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashingUtil {

	/**
	 * 비밀번호 규칙 8자~20자, 1개이상 대문자, 1개이상 특수기호(!@#$), 숫자포함
	 *
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(String password) {

		boolean isValid = false;
		String passCheck = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$]).{8,20}";

		Pattern pwPattern = Pattern.compile(passCheck);
		Matcher matcher_pw = pwPattern.matcher(password);

		if(matcher_pw.find()){
			isValid = true;
		}else{
			isValid = false;
		}

		return isValid;
	}


	/**
	 * email check
	 *
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(mail);
		Matcher m = p.matcher(email);
		return m.matches();
	}


}
