package com.ansysan.project.notification_service.email;

import com.ansysan.project.notification_service.dto.NotificationRequest;
import com.ansysan.project.notification_service.handler.EmailException;
import com.ansysan.project.notification_service.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private NotificationRequest notificationRequest;

    @BeforeEach
    void setUp() {
        notificationRequest = new NotificationRequest(
                "test@example.com",
                "Test Subject",
                "Test Body"
        );
    }

    @Test
    void send_ShouldSendEmail() {
        emailService.send(notificationRequest);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertThat(sentMessage.getTo()).containsExactly("test@example.com");
        assertThat(sentMessage.getSubject()).isEqualTo("Test Subject");
        assertThat(sentMessage.getText()).isEqualTo("Test Body");
    }

    @Test
    void send_ShouldThrowEmailException_WhenSendFails() {
        doThrow(new MailSendException("SMTP failure"))
                .when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        assertThatThrownBy(() -> emailService.send(notificationRequest))
                .isInstanceOf(EmailException.class)
                .hasMessage("Error during email sending");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}