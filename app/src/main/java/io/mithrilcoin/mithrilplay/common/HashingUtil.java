package io.mithrilcoin.mithrilplay.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashingUtil {

	/**
	 * Password rules 8 to 20 characters, one or more uppercase letters, one or more special symbols (! @ # $), Including numbers
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
