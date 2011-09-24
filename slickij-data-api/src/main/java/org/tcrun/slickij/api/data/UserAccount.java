package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
@Entity("users")
public class UserAccount implements Serializable
{
	@Id
	private String accountName;

	@Property
	private String password;

	@Reference
	private Configuration userConfiguration;

	@Property
	private String salt;

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@JsonIgnore
	public void setNewPassword(String password)
	{
		salt = getRandomSalt();
		this.password = encryptPassword(salt + password);
	}

	public Configuration getUserConfiguration()
	{
		return userConfiguration;
	}

	public void setUserConfiguration(Configuration userConfiguration)
	{
		this.userConfiguration = userConfiguration;
	}

	public boolean authenticate(String password)
	{
		if(this.password == null || this.salt == null)
			return false;
		if(encryptPassword(salt + password).equals(this.password))
			return true;
		return false;
	}

	@JsonIgnore
	private String getRandomSalt()
	{
		return (new BigInteger(32, new SecureRandom())).toString(32);
	}

	private String encryptPassword(String password)
	{
		// from: http://www.mkyong.com/java/java-sha-hashing-example/
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("SHA-256");
		} catch(NoSuchAlgorithmException ex)
		{
			// hopefully this doesn't happen ;-)  I know, I know, bad coding
		}
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
		return sb.toString();
	}
}
