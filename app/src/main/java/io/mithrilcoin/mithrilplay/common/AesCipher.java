package io.mithrilcoin.mithrilplay.common;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCipher {

	static final byte[] HEX_CHAR_TABLE = {
		(byte)'0', (byte)'1', (byte)'2', (byte)'3',
		(byte)'4', (byte)'5', (byte)'6', (byte)'7',
		(byte)'8', (byte)'9', (byte)'a', (byte)'b',
		(byte)'c', (byte)'d', (byte)'e', (byte)'f'
	};

	public static String getHexString(byte[] raw){
		byte[] hex = new byte[2 * raw.length];
		int index = 0;

		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		try{
			return new String(hex, "ASCII");
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}	

	protected Cipher ecipher;
	protected Cipher dcipher;

	public AesCipher(byte[] key, byte[] initializationVector)
	throws InvalidKeyException, InvalidAlgorithmParameterException {

		try {
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(initializationVector);
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

			// AES 128bit CBC mode, PKCS5 Padding
			ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// CBC requires an initialization vector
			ecipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (NoSuchAlgorithmException e) {
			Log.e("AesCipher", e.getMessage().toString());
		} catch (NoSuchPaddingException e) {
			Log.e("AesCipher", e.getMessage().toString());
		}
	}

	public byte[] encrypt(byte[] plain) {
		try {
			return ecipher.doFinal(plain);
		} catch (Exception e) {
			Log.e("AesCipher", e.getMessage().toString());
		}
		return null;
	}

	public byte[] decrypt(byte[] encoded) {
		try {
			return dcipher.doFinal(encoded);
		} catch (Exception e) {
			Log.e("AesCipher", e.getMessage().toString());
		}
		return null;
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public static byte[] getAESKey() {
		byte[] keyFromServer = hexToByteArray(MithrilPreferences.getString(CommonApplication.getApplication(), MithrilPreferences.PREF_KEY, ""));
		
		if (keyFromServer == null) {
			Log.e("AesCipher", "keyFromServer value is null");
			return "".getBytes();
		}
		
		byte[] key = new byte[keyFromServer.length/2];
		System.arraycopy(keyFromServer, 0, key, 0, keyFromServer.length/2);
		return key;
	}

	public static byte[] getAESKey( String hexKey ) {
		 byte[] keyFromServer = hexToByteArray(hexKey);
		 
		 if (keyFromServer == null) {
			 Log.e("AesCipher", "keyFromServer value is null");
			return "".getBytes();
		 }
		 
		 byte[] key = new byte[keyFromServer.length/2];
		 System.arraycopy(keyFromServer, 0, key, 0, keyFromServer.length/2);
		 return key;
	}

	public static byte[] getInitializationVector() {
		byte[] keyFromServer = hexToByteArray(MithrilPreferences.getString(CommonApplication.getApplication(), MithrilPreferences.PREF_KEY, ""));
		
		if (keyFromServer == null) {
			Log.e("AesCipher", "keyFromServer value is null");
			return "".getBytes();
		}
		
		byte[] initializationVector = new byte[keyFromServer.length/2];
		System.arraycopy(keyFromServer, 16, initializationVector, 0, keyFromServer.length/2);
		return initializationVector;
	}

	public static byte[] getInitializationVector( String hexKey ) {
		 byte[] keyFromServer = hexToByteArray(hexKey);
			
		 if (keyFromServer == null) {
			 Log.e("AesCipher", "keyFromServer value is null");
			 return "".getBytes();
		 }
			
		 byte[] initializationVector = new byte[keyFromServer.length/2];
		 System.arraycopy(keyFromServer, 16, initializationVector, 0, keyFromServer.length/2);
		 return initializationVector;
	}
}
