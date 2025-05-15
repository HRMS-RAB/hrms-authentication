package com.hrms.auth.listener;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.listener.EmployeeEvent;
import com.hrms.auth.service.RoleAssignmentService;
import com.hrms.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Consumes EmployeeEvent messages and provisions / updates users. */
@Component
public class EmployeeEventListener {

    private static final Logger log = LoggerFactory.getLogger(EmployeeEventListener.class);

    private final UserService           userService;
    private final RoleAssignmentService roleService;

    public EmployeeEventListener(UserService userService,
                                 RoleAssignmentService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RabbitListener(queues = "${hrms.rabbitmq.employee.queue}")
    @Transactional
    public void handleEmployeeEvent(EmployeeEvent evt) {
        log.info("↪️  Received EmployeeEvent [{}] for {}", evt.getEventType(), evt.getWorkEmail());

        UserDto user = userService.upsertFromEmployeeEvent(evt);   // create / update user
        roleService.assignRole(evt);                               // keep existing role logic

        log.info("✅ user {} ({}) processed & role assigned", user.getUserId(), user.getWorkEmail());
    }
}
