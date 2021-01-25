package vn.vnpay.utils;


import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

public class MailUtil {
    static final String FROM = ConfigurationLoader.getInstance().getAsString("mail.smtp.auth.username", "");
    static final String FROM_NAME = "VNPAY Test";

    private static Session getSession() {
        Properties prop = ConfigurationLoader.getInstance().getProperties();
        String username = prop.getProperty("mail.smtp.auth.username");
        String password = prop.getProperty("mail.smtp.auth.password");
        return Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static void send(String subject, String toUsers, String ccUsers, String bccUsers, String content) throws IOException, MessagingException {
        send(subject, toUsers, ccUsers, bccUsers, content, null);
    }

    public static void send(String subject, String toUsers, String ccUsers, String bccUsers, String content, String[] files) throws MessagingException, IOException {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        message.setRecipients(Message.RecipientType.TO, toUsers);
        if (Utils.isNotEmpty(ccUsers)) {
            message.setRecipients(Message.RecipientType.CC, ccUsers);
        }
        if (Utils.isNotEmpty(bccUsers)) {
            message.setRecipients(Message.RecipientType.BCC, bccUsers);
        }

        InternetHeaders headers = new InternetHeaders();
        headers.addHeader("Content-type", "text/html; charset=UTF-8");
        MimeBodyPart body = new MimeBodyPart(headers, content.getBytes("UTF-8"));
        body.setText(content, "UTF-8", "html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);
        message.setContent(multipart);
        if (null != files) {
            for (String file : files) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(file);
                multipart.addBodyPart(attachmentBodyPart);
            }
        }
        message.setContent(multipart);
        message.setFrom(new InternetAddress(FROM, FROM_NAME));

        Transport.send(message);
    }

    public static void send(String subject, String toUsers, String ccUsers, String bccUsers, String content, String content2, String[] files) throws MessagingException, IOException {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        message.setRecipients(Message.RecipientType.TO, toUsers);
        if (Utils.isNotEmpty(ccUsers)) {
            message.setRecipients(Message.RecipientType.CC, ccUsers);
        }
        if (Utils.isNotEmpty(bccUsers)) {
            message.setRecipients(Message.RecipientType.BCC, bccUsers);
        }

        // html
        InternetHeaders headers = new InternetHeaders();
        headers.addHeader("Content-type", "text/html; charset=UTF-8");
        MimeBodyPart body = new MimeBodyPart(headers, content.getBytes("UTF-8"));
        body.setText(content, "UTF-8", "html");


        // inline image part
        byte[] bytesImage = Base64.getDecoder().decode(content2);
        ByteArrayDataSource image = new ByteArrayDataSource(bytesImage, "image/png");
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.setHeader("Content-ID", "<image1>");
        imagePart.setDataHandler(new DataHandler(image));


        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);
        multipart.addBodyPart(imagePart);

        message.setContent(multipart);
        if (null != files) {
            for (String file : files) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(file);
                multipart.addBodyPart(attachmentBodyPart);
            }
        }
        message.setContent(multipart);
        message.setFrom(new InternetAddress(FROM, FROM_NAME));
        Transport.send(message);
    }

    public static void send(String subject, String toUsers, String ccUsers, String content) throws MessagingException, IOException {
        send(subject, toUsers, ccUsers, null, content);
    }

    public static void send(String subject, String toUsers, String content) throws MessagingException, IOException {
        send(subject, toUsers, null, null, content);
    }

    public static void send(String subject, String toUsers, String ccUsers, String content, String[] files) throws MessagingException, IOException {
        send(subject, toUsers, ccUsers, null, content, files);
    }

    public static void send(String subject, String toUsers, String content, String[] files) throws MessagingException, IOException {
        send(subject, toUsers, null, null, content, files);
    }

    public static void send(String subject, String toUsers, String bccUsers, String content, Map<String, String> inlineImages) throws MessagingException, IOException {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        message.setRecipients(Message.RecipientType.TO, toUsers);
        if (Utils.isNotEmpty(bccUsers)) {
            message.setRecipients(Message.RecipientType.BCC, bccUsers);
        }

        InternetHeaders headers = new InternetHeaders();
        headers.addHeader("Content-type", "text/html; charset=UTF-8");
        MimeBodyPart body = new MimeBodyPart(headers, content.getBytes(StandardCharsets.UTF_8));
        body.setText(content, "UTF-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);

        message.setContent(multipart);
        message.setFrom(new InternetAddress(FROM, FROM_NAME));
        Transport.send(message);
    }
}
