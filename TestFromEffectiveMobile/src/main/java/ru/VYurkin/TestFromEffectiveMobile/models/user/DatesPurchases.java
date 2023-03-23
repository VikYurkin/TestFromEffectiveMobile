package ru.VYurkin.TestFromEffectiveMobile.models.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Table(name = "dates")
@Data
@NoArgsConstructor
public class DatesPurchases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private long dataId;

    @ManyToOne
    @JoinColumn(name = "purchases_id", referencedColumnName = "purchases_id")
    private Purchases purchases;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public DatesPurchases(Purchases purchases, Date date) {
        this.purchases = purchases;
        this.date = date;
    }
}
