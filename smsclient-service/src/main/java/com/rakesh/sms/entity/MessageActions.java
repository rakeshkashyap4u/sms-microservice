package com.rakesh.sms.entity;


import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "messageactions")
public class MessageActions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid")
    private int aid;

    @Column(name = "moId", nullable = false)
    private int moId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "details")
    private String details;

    public MessageActions() {
        // Default constructor
    }

    // Getters and setters
    public int getAid() { return aid; }
    public void setAid(int aid) { this.aid = aid; }
    public int getMoId() { return moId; }
    public void setMoId(int moId) { this.moId = moId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    @Override
    public String toString() {
        try {
            return CoreUtils.GSON.toJson(this);
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
        }
        return "{}";
    }
}
