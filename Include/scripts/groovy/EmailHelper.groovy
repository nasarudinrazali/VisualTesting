
import internal.GlobalVariable;
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint;
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase;
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData;
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject;

import com.kms.katalon.core.annotation.Keyword;
import com.kms.katalon.core.checkpoint.Checkpoint;
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testcase.TestCase;
import com.kms.katalon.core.testdata.TestData;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;



public class EmailHelper {

	static String[] number;

	public static void main(String[] args) {
		startExecution();
	}

	@Keyword
	static List<String> startExecution() {
		List<String> extractedNumbers = connect();

		// Use the extracted numbers in your logic
		for (String number : extractedNumbers) {
			System.out.println(number); // Verification code number
		}

		// Assign the extracted numbers to the number variable
		if (!extractedNumbers.isEmpty()) {
			number = (String[])extractedNumbers.get(0); // Assuming you want to store the first extracted number
		}


	}


	static List<String> connect() {
		// Email account credentials
		String username = "nasincorp42@outlook.com";
		String password = "nndbfhzbxieyucnb";

		// IMAP server properties
		String host = "imap-mail.outlook.com";
		String port = "993";

		try {
			// Set properties
			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "imaps");
			properties.setProperty("mail.imaps.host", host);
			properties.setProperty("mail.imaps.port", port);
			properties.setProperty("mail.imaps.ssl.enable", "true");

			// Create session
			Session session = Session.getInstance(properties);

			// Connect to the IMAP server
			Store store = session.getStore("imaps");
			store.connect(host, username, password);

			// Open the inbox folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Get the messages in the inbox folder
			Message[] messages = inbox.getMessages();

			// Check if there are any messages
			if (messages.length > 0) {
				// Get the latest message
				Message latestMessage = messages[messages.length - 1];

				// Check if the content is multipart
				if (latestMessage.isMimeType("multipart/*")) {
					Multipart multipart = (Multipart) latestMessage.getContent();
					int partCount = multipart.getCount();

					// Iterate over the parts of the multipart content
					for (int i = 0; i < partCount; i++) {
						BodyPart part = multipart.getBodyPart(i);

						// Check if the part is of type text
						if (part.isMimeType("text/plain")) {
							// Store the email body in a String variable
							String body = (String) part.getContent();
							System.out.println("Email Body: " + body);

							// Extract 6-digit numbers from the body
							List<String> extractedNumbers = extractSixDigitNumbers(body);

							// Return the extracted numbers
							return extractedNumbers;
						}
					}
				}
			}

			// Close the connections
			inbox.close(false);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>(); // Return an empty list if no numbers were found or an error occurred
	}

	static List<String> extractSixDigitNumbers(String text) {
		List<String> sixDigitNumbers = new ArrayList<>();

		// Regular expression pattern to match 6-digit numbers
		Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
		Matcher matcher = pattern.matcher(text);

		// Find all matches and add them to the list
		while (matcher.find()) {
			String number = matcher.group();
			sixDigitNumbers.add(number);
		}

		return sixDigitNumbers;
	}

}