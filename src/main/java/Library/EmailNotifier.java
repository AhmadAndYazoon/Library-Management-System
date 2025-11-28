package Library;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotifier implements Observer {

    private final String fromEmail = "YOUR_GMAIL@gmail.com"; 
    private final String password = "YOUR_APP_PASSWORD";

    @Override
    public void notify(User user, String message) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.email));
            msg.setSubject("Library Reminder");
            msg.setText(message);

            Transport.send(msg);

            System.out.println("Email sent to: " + user.email);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
