package defi_backend_api.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
