package com.bb.automation.testUtils; 

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*; // Required for attachments
import java.util.Properties;
import java.io.File;

public class EmailUtils{

    /**
     * Sends an email with multiple attachments.
     * @param to Recipient email
     * @param subject Email Subject
     * @param body Email Body Text
     * @param attachmentPaths Comma-separated list of file paths to attach
     */
    public static void sendEmail(String to, String subject, String body, String... attachmentPaths) {
        
        // 1. GMAIL CONFIGURATION
        final String from = "sphpatil01@gmail.com"; 
        final String password = "boyy nxbe hgym denn"; // ⚠️ REPLACE WITH YOUR APP PASSWORD

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Important for Gmail

        // 2. AUTHENTICATOR
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // 3. CREATE MESSAGE
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // 4. CREATE MULTIPART (Container for Body + Attachments)
            Multipart multipart = new MimeMultipart();

            // -- Part A: Body Text --
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);

            // -- Part B: Attachments (Loop through all files) --
            if (attachmentPaths != null && attachmentPaths.length > 0) {
                for (String filePath : attachmentPaths) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        MimeBodyPart attachPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(filePath);
                        attachPart.setDataHandler(new DataHandler(source));
                        attachPart.setFileName(file.getName()); // Auto-names the file in email
                        multipart.addBodyPart(attachPart);
                        System.out.println("📎 Attached: " + file.getName());
                    } else {
                        System.err.println("⚠️ Warning: Attachment not found at " + filePath);
                    }
                }
            }

            // 5. SEND
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("✅ Email Sent Successfully to " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Error Sending Email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}