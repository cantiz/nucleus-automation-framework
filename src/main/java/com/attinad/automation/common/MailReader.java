package com.attinad.automation.common;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import com.attinad.automation.utils.CantizAutomationCoreException;
import org.jsoup.Jsoup;

/**
 * Created by anandxp (anand.xavier@attinadsoftware.com) on 17/01/17.
 * This utility class can be used to read mails from various mail servers. The class
 * supports imap, pop3 and smtp mail servers. Configuration for the mail servers can be provided
 * to this class via method signatures or using property files.
 */
public class MailReader {

	public static String getMailContent() throws CantizAutomationCoreException {
		PropertyReader propertyReader = PropertyReader.getInstance();
		if(null == propertyReader.getMailUserName())
			throw new CantizAutomationCoreException(Constants.MAIL_USER_NAME_PROPERTY+" ");
		return getMailContent(propertyReader.getMailUserName(),propertyReader.getMailPassword());
	}

	public static String getMailContent(String user,String password) throws CantizAutomationCoreException {
		PropertyReader propertyReader = PropertyReader.getInstance();
		if(null == propertyReader.getMailHost())
			throw new CantizAutomationCoreException(Constants.MAIL_HOST_PROPERTY+" should be defined in application configuration file");
		return getMailContent(propertyReader.getMailHost(),propertyReader.getMailPort(),propertyReader.getMailType(),user,password);
	}

	/**
	 * This method should be called if mail settings needs to be passed directly to automation core.
	 *
	 * @param host
	 * @param port
	 * @param storeType
	 * @param user
	 * @param password
	 * @return
	 * @throws CantizAutomationCoreException
	 */
	public static String getMailContent(String host, String port, String storeType, String user, String password) throws CantizAutomationCoreException {
		String result = "";
		Store store = null;
		Folder emailFolder = null;
		try {

			if (storeType.equalsIgnoreCase("smtp")) {
				Properties properties = new Properties();
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", port);
				properties.put("mail.smtp.starttls.enable", "true");
				Session emailSession = Session.getDefaultInstance(properties);
				// create the SMTP store object and connect with the pop server
				store = emailSession.getStore("smtp");
				store.connect(host, user, password);
			} else if (storeType.equalsIgnoreCase("pop3")) {
				Properties properties = new Properties();
				properties.put("mail.pop3.host", host);
				properties.put("mail.pop3.port", port);
				properties.put("mail.pop3.starttls.enable", "true");
				Session emailSession = Session.getDefaultInstance(properties);
				// create the POP3 store object and connect with the pop server
				store = emailSession.getStore("pop3s");
				store.connect(host, user, password);
			} else if (storeType.equalsIgnoreCase("imap")) {
				Properties properties = new Properties();
				properties.put("mail.imap.host", host);
				properties.put("mail.imap.port", port);
				properties.put("mail.imap.starttls.enable", "true");
				Session emailSession = Session.getDefaultInstance(properties);
				// create the POP3 store object and connect with the pop server
				store = emailSession.getStore("imaps");
				store.connect(host, user, password);
			}

			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.HOLDS_MESSAGES);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			int msglength = messages.length;
			if(msglength == 0)
				return result;
				// length - 1 gives the latest message
				Message message = messages[msglength - 1];

				Object content = message.getContent();
				// String result = "";
				if (content instanceof String) {
					String body = (String) content;
					result = body;
				} else if (content instanceof Multipart) {

					MimeMultipart mimeMultipart = (MimeMultipart) content;
					int count = mimeMultipart.getCount();

					for (int i = 0; i < count; i++) {
						BodyPart bodyPart = mimeMultipart.getBodyPart(i);
						if (bodyPart.isMimeType("text/plain")) {
							result = result + "\n" + bodyPart.getContent();
							break; // without break same text appears twice in
									// my
									// tests
						} else if (bodyPart.isMimeType("text/html")) {
							String html = (String) bodyPart.getContent();
							result = result + "\n" + Jsoup.parse(html).text();
						}
					}
				}
			return result;

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new CantizAutomationCoreException(e.getMessage());
		} catch (IOException e) {
			throw new CantizAutomationCoreException(e.getMessage());
		} finally{
			if(null != emailFolder && emailFolder.isOpen()){
				try {
					emailFolder.close(false);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			if(null != store && store.isConnected()){
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
