package ru.VYurkin.TestFromEffectiveMobile.dto.notificationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private String header;

    private Date date;

    private String text;
}
