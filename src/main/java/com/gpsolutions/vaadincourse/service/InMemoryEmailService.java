package com.gpsolutions.vaadincourse.service;

import com.gpsolutions.vaadincourse.dto.Email;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class InMemoryEmailService implements EmailService {

    private List<Email> emails;

    @Override
    public List<Email> getAllEmails() {
        if (emails == null) {
            emails = new ArrayList<>();
            final Email first = createEmail("Demian", "Hello, have you done your homework",
                                            Arrays.asList("Wladimir", "Pavel", "Iosif"), LocalDate.now().minusDays(2));
            final Email second =
                    createEmail("Wladimir", "Hello, I'm not going to do any homework. Don't write me anymore.",
                                Collections.singletonList("Demian"), LocalDate.now().minusDays(1));
            final Email third = createEmail("Demian", "Ok",
                                            Collections.singletonList("Wladimir"), LocalDate.now());
            emails.add(first);
            emails.add(second);
            emails.add(third);
        }
        return emails;
    }

    private Email createEmail(final String name,
                              final String message,
                              final List<String> recipients,
                              final LocalDate date) {
        final Email email = new Email();
        email.setName(name);
        email.setMessage(message);
        email.setRecipients(new ArrayList<>(recipients));
        email.setDate(date);
        return email;
    }
}
