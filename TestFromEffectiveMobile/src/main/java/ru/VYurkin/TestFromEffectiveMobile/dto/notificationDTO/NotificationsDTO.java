package ru.VYurkin.TestFromEffectiveMobile.dto.notificationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsDTO {
    private List<NotificationDTO> notifications;
}
