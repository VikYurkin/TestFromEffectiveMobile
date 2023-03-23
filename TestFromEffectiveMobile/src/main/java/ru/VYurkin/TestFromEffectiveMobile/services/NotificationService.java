package ru.VYurkin.TestFromEffectiveMobile.services;

import org.springframework.stereotype.Service;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Notification;
import ru.VYurkin.TestFromEffectiveMobile.repositories.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification save(Notification notification){
        return notificationRepository.save(notification);
    }
}
