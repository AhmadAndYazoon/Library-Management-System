package Library;

public interface MailSender {
    void send(String to, String subject, String body) throws Exception;
}
