package com.f2d.chatroom.repository;

import com.f2d.chatroom.domain.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface F2DChatMessageRepository extends JpaRepository<ChatGroup, UUID> {
}
