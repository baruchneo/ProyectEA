package co.com.sc.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jboss.seam.util.Base64;

public class PasswordUtils
{
	public static String generateSaltedHash(String password, String saltPhrase, String algorithm)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance(algorithm);

			if (saltPhrase != null)
			{
				md.update(saltPhrase.getBytes());
				byte[] salt = md.digest();

				md.reset();
				md.update(password.getBytes());
				md.update(salt);
			}
			else
			{
				md.update(password.getBytes());
			}

			byte[] raw = md.digest();
			return Base64.encodeBytes(raw);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static String getSha1HashFromPassword(String password)
	{
		String algorithm = "SHA1";
		byte[] digest = null;
		byte[] buffer = password.getBytes();
		try 
		{
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.reset();
			messageDigest.update(buffer);
			digest = messageDigest.digest();
		} 
		catch (NoSuchAlgorithmException ex) 
		{
			throw new RuntimeException(ex);
		}
		return byteToHexa(digest);
	}
	
	private static String byteToHexa(byte[] digest)
	{
		 String hash = "";
		 for(byte aux : digest) 
		 {
			 int b = aux & 0xff;
			 if (Integer.toHexString(b).length() == 1)
			 {
				 hash += "0";
			 }
			 hash += Integer.toHexString(b);
		 }
		 return hash;
	}
}
