
import javax.mail.Address
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import com.kms.katalon.core.annotation.Keyword

public class FahmiEmailHelper {

	@Keyword
	def runEmail() {

		String host = "imap-mail.outlook.com";
		String username ="tb-qa@outlook.com" ;
		String password ="bsefmmrjyafsaffr";

		// Set the properties
		Properties properties = new Properties();
		properties.setProperty("mail.store.protocol", "imaps");
		properties.setProperty("mail.imaps.host", host);
		properties.setProperty("mail.imaps.port", "993");

		try {
			// Connect to the IMAP server
			Session session = Session.getInstance(properties);
			Store store = session.getStore();
			store.connect(host, username, password);

			// Select the mailbox
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Fetch the emails
			Message[] messages = inbox.getMessages();

			// Process the emails
			for (Message message : messages) {
				String subject = message.getSubject();
				Address[] fromAddresses = message.getFrom();

				System.out.println("Subject: " + subject);
				System.out.println("From: " + fromAddresses[0].toString());
				System.out.println("Content: " + message.getContent().toString());
				System.out.println("-----------------------------------");
			}

			// Close the connection
			inbox.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}