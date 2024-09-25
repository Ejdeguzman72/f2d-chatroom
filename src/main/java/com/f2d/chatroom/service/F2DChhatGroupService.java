package com.f2d.chatroom.service;

import com.f2d.chatroom.repository.F2DChatGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class F2DChhatGroupService {

    @Autowired
    private F2DChatGroupRepository chatGroupRepository;


}
