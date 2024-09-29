package com.f2d.chatroom.domain;

import com.f2d.chatroom.domain.F2DGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "chat_group")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatGroup {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "chat_group_id", updatable = false)
    private UUID chatGroupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_update_time")
    private LocalDate lastUpdateTime;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private F2DGroup f2dGroup;

    // Getters and Setters
    public UUID getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(UUID chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDate lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public F2DGroup getF2dGroup() {
        return f2dGroup;
    }

    public void setF2dGroup(F2DGroup f2dGroup) {
        this.f2dGroup = f2dGroup;
    }
}
