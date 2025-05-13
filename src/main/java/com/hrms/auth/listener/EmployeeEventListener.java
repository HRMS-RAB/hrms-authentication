package com.hrms.auth.listener;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.listener.EmployeeEvent;          // adjust if your event class lives elsewhere
import com.hrms.auth.service.RoleAssignmentService;
import com.hrms.auth.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmployeeEventListener {

    private final UserService userService;
    private final RoleAssignmentService roleAssignmentService;

    public EmployeeEventListener(UserService userService,
                                 RoleAssignmentService roleAssignmentService) {
        this.userService = userService;
        this.roleAssignmentService = roleAssignmentService;
    }

    /** Handles “employee created” events from hrms-backend. */
 
    
    /*//@RabbitListener(queues = "#{rabbitConfig.employeeCreatedQueue}")
    @RabbitListener(
            queues = "#{rabbitConfig.employeeCreatedQueue}",
            containerFactory = "rabbitListenerContainerFactory"   // keep if you have this factory bean
        )*/
    
    @RabbitListener(
            queues = "${hrms.rabbitmq.employee.queue}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    
    public void handleEmployeeCreated(EmployeeEvent event) {

        /* --- Build a User DTO from the event --- */
        UserDto dto = new UserDto();
        dto.setEmployeeId(event.getEmployeeId());     // <-- NEW METHOD
        dto.setWorkEmail(event.getWorkEmail());
        dto.setFullName(event.getFullName());
        dto.setRoles(Set.of("EMPLOYEE"));             // or whatever comes from the event

        /* --- Persist user & assign role --- */
        userService.createOrUpdateUser(dto);
        roleAssignmentService.assignRole(event);      // retain existing logic
    }
}





/*package com.hrms.auth.listener;

import com.hrms.auth.entity.User;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.RoleAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeEventListener {

    private final UserRepository        users;
    private final PasswordEncoder       encoder;
    private final RoleAssignmentService roleService;

    @RabbitListener(
            queues = "${hrms.rabbitmq.employee.queue}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    @Transactional
    public void handleEmployeeEvent(EmployeeEvent evt) {
        log.info("Received EmployeeEvent [{}] for {}", evt.getEventType(), evt.getWorkEmail());

        User user = users.findByWorkEmail(evt.getWorkEmail())
                         .orElseGet(User::new);

        user.setEmpId(evt.getEmployeeId());
        user.setDeptId(evt.getDepartmentId());
        user.setDeptName(evt.getDepartmentName());
        user.setFullName(evt.getFullName());
        user.setWorkEmail(evt.getWorkEmail());

        if (user.getPassword() == null) {
            user.setPassword(encoder.encode("Password@1"));
        }
        users.save(user);

        roleService.assignRole(evt);     // delegate
    }
}
*/



