package outils;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SymmetricAESKey {

	private static final Logger LOG = Logger.getLogger(SymmetricAESKey.class.getName());

	private static final String CIPHER_ALGORITHM = "AES";
	private static final String KEY_ALGORITHM = "AES"; 
	private static final String PROPS_FILE = "key.properties";
	private static final String PROPS_VALUE_KEY ="key";

	public static String encrypt(String message) {
		LOG.log(Level.INFO,"encrypting... ");
		try {
			Cipher cipher = buildCipher(Cipher.ENCRYPT_MODE);
			byte[] dataToSend = message.getBytes(StandardCharsets.UTF_8.name());
			byte[] encryptedData = cipher.doFinal(dataToSend);
			return Base64.encodeBase64URLSafeString(encryptedData);
		} catch (Exception e) {
			LOG.log(Level.SEVERE,e.getMessage(),e);
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String encryptedMessage) {
		LOG.log(Level.INFO,"decrypting... ");
		try {
			Cipher cipher = buildCipher(Cipher.DECRYPT_MODE);
			byte[] encryptedData = Base64.decodeBase64(encryptedMessage);
			byte[] data = cipher.doFinal(encryptedData);
			return new String(data, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			LOG.log(Level.SEVERE,e.getMessage(),e);
			throw new RuntimeException(e);
		}
	}

	private static Cipher buildCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(mode, getKey());
		return cipher;
	}

	private static Key getKey() {
		String keyStr = Utils.getProps(PROPS_FILE,PROPS_VALUE_KEY);
		return new SecretKeySpec(Base64.decodeBase64(keyStr),KEY_ALGORITHM);
	}

	//permet de gen�rer une cl� d'encryption lors d'une nouvelle installation
	//attention si elle change tous les token des apps ne seront plus valable
	private static String generateKeyAES () throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
		keyGen.init(256); 
		SecretKey secretKey = keyGen.generateKey();
		return Base64.encodeBase64String(secretKey.getEncoded());
	}

}