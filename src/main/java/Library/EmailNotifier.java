package Library;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Observer implementation that sends reminder messages via email
 * using the JavaMail API and a Gmail SMTP server.
 *
 * <p>In this academic project, credentials are hard-coded and should
 * be replaced with environment variables or a secure configuration
 * in real applications.</p>
 */
public class EmailNotifier implements Observer {

    /** Sender Gmail address used for all notifications. */
    private final String fromEmail = "YOUR_GMAIL@gmail.com";

    /** App password or SMTP password for the sender Gmail account. */
    private final String password = "YOUR_APP_PASSWORD";

    /**
     * Sends an email reminder message to the given user.
     *
     * @param user    target user
     * @param message reminder message text
     */
    @Override
    public void notify(User user, String message) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
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
