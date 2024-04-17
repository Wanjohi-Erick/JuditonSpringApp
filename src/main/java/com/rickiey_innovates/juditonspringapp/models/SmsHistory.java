package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "sms_history", schema = "messaging")
public class SmsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "message", nullable = false, length = 10000)
    private String message;

    @Column(name = "recipients", nullable = false, length = 500)
    private String recipients;

    @Column(name = "date_sent", length = 200)
    private String dateSent;

    @Lob
    @Column(name = "phones")
    private String phones;

    @Column(name = "sent")
    private Integer sent;

    @Column(name = "delivered")
    private Integer delivered;

    @Column(name = "failed")
    private Integer failed;

    @JoinColumn(name = "sms_group")
    private Integer smsGroup;

    @Column(name = "sending_params", length = 200)
    private String sendingParams;

    @Column(name = "type", length = 400)
    private String type;

    @Column(name = "sms_id", length = 600)
    private String smsId;

    @Column(name = "uuid", length = 600)
    private String uuid;

    @Column(name = "sent_by")
    private Integer sentBy;

    @Column(name = "status")
    private String status;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm")
    private Farm farm;

}