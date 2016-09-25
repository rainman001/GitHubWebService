package com.github;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.util.Base64;

@SpringBootApplication
public class GitHubWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubWebServiceApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			final String orgName = "";
			
			String credentials = ":";
			byte[] credsAsBytes = credentials.getBytes();
			byte[] base64CredsAsBytes = Base64.encode(credsAsBytes);
			String base64Creds = new String(base64CredsAsBytes);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + base64Creds);
			
			HttpEntity<String> request = new HttpEntity<String>(headers);
			ResponseEntity<Member[]> response = restTemplate.exchange("https://api.github.com/orgs/" + orgName + "/members", HttpMethod.GET, request, Member[].class);
			Member[] members = response.getBody();
			
			List<String> memberLogins = new ArrayList<>();
			System.out.println("Login: " + members[0].getLogin());
			// Next item is to make a call out the user endpoint for each user to get the required email and name info
			Member member = null;
			for (int i = 0; i < members.length; i++) {
				member = restTemplate.getForObject("https://api.github.com/users/" + members[i].getLogin(), Member.class);
				memberLogins.add(members[i].getLogin());
				System.out.println("Name: " + member.getName());
				System.out.println("Email: " + member.getEmail());
				if (member.getName() == null || member.getName() == "") {
					if (member.getEmail() != null || member.getEmail() != "") {
						setUpEmail(member.getEmail());
					}
				}
			}

			// Write list to file
			this.bufferedWrite(memberLogins, "./src/main/java/com/github/users.txt");
			
			// Create new bucket
			Bucket bucket = new Bucket();
			bucket.createBucket();
			bucket.uploadObject();
		};
	}
	
	private void setUpEmail(String to) {
		final String username = "";
		final String password = ""; // App password
		
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password); 
				}
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username));
			message.setSubject("Go to GitHub and Enter Your Full Name Into Your Account!");
			message.setText("If you click on the link below it will take you to GitHub, where you can sign into your " + 
					"account and enter your full name on your account." + "\n" +
					"https://github.com/");
			
			Transport.send(message);
			System.out.println("Sent message successfully!");
			
		} catch (MessagingException ex){
			ex.printStackTrace();
		}
	}
	
	private void bufferedWrite(List<String> content, String filePath) throws IOException {
		
		Path path = Paths.get(filePath);
		Charset charset = Charset.forName("utf-8");

		if (!Files.exists(path, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS})) {
			this.createNewFile(path);
		} else {
			System.out.println("File already exists.");
			Files.delete(path);
			System.out.println("File was deleted.");
			this.createNewFile(path);
		}

		try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
			for (String line : content) {
				writer.write(line, 0, line.length());
		        writer.newLine();
		    }
			System.out.println("Finished writing contents to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createNewFile(Path path) {
		try {
			Files.createFile(path);
			System.out.println("New file was created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
