package com.hrms.auth.service.impl;

import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;
import com.hrms.auth.listener.EmployeeEvent;
import com.hrms.auth.repository.RoleRepository;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.RoleAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleAssignmentServiceImpl implements RoleAssignmentService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;

    private final WebClient backend = WebClient.builder()
            .baseUrl("http://localhost:8080")     // hrms-backend
            .build();

    private static final long NONE = -1L;

    @Override
    public void assignRole(EmployeeEvent evt) {

        Mono<Long> parentDeptMono = backend.get()
                .uri("/api/departments/{id}", evt.getDepartmentId())
                .retrieve()
                .bodyToMono(Dept.class)
                .map(Dept::getParentDepartmentId)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty())
                .onErrorResume(ex -> {
                    log.warn("Dept service unreachable: {}", ex.getMessage());
                    return Mono.empty();
                })
                .defaultIfEmpty(NONE);

        parentDeptMono.subscribe(parentIdOrNone -> {
            User user = userRepo.findByWorkEmail(evt.getWorkEmail())
                                 .orElseThrow(() -> new IllegalStateException(
                                         "User not found for " + evt.getWorkEmail()));

            String roleName = resolve(evt.getDepartmentName(),
                                      parentIdOrNone.equals(NONE) ? null : parentIdOrNone);

            Role role = roleRepo.findByName(roleName)
                    .orElseGet(() -> roleRepo.findByName("EMPLOYEE")
                            .orElseThrow(() ->
                                    new IllegalStateException("Default role EMPLOYEE not found")));

            user.getRoles().clear();
            user.getRoles().add(role);
            userRepo.save(user);

            log.info("Role {} assigned to {}", role.getName(), user.getWorkEmail());
        });
    }

    /* ───────────── helpers ───────────── */

    private static String resolve(String deptName, Long parentDeptId) {
        if ("PAYROLL".equalsIgnoreCase(deptName)) return "PAYROLL_SPECIALIST";
        if ("HR".equalsIgnoreCase(deptName)) {
            return parentDeptId == null ? "HR_MANAGER" : "PAYROLL_SPECIALIST";
        }
        return "EMPLOYEE";
    }

    /* minimal DTO from backend */
    private static class Dept {
        private Long parentDepartmentId;
        public Long getParentDepartmentId() { return parentDepartmentId; }
        public void setParentDepartmentId(Long id) { this.parentDepartmentId = id; }
    }
}







/*package com.hrms.auth.service.impl;

import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;
import com.hrms.auth.listener.EmployeeEvent;
import com.hrms.auth.repository.RoleRepository;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.RoleAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleAssignmentServiceImpl implements RoleAssignmentService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

   //** Non-blocking HTTP client to hrms-backend 
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    //** sentinel value because `defaultIfEmpty(null)` is forbidden 
    private static final long NONE = -1L;

    @Override
    public void assignRole(User user, EmployeeEvent event) {

        Mono<Long> parentDeptIdMono = webClient.get()
                .uri("/api/departments/{id}", event.getDepartmentId())
                .retrieve()
                .bodyToMono(DepartmentResponse.class)
                .map(DepartmentResponse::getParentDepartmentId)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty())
                .onErrorResume(ex -> {
                    log.warn("Dept service unreachable: {}", ex.toString());
                    return Mono.empty();
                })
                .defaultIfEmpty(NONE);                      // <── ✔ no NPE

        parentDeptIdMono.subscribe(parentDeptIdOrNone -> {

            Long parentDeptId = parentDeptIdOrNone.equals(NONE) ? null : parentDeptIdOrNone;
            String roleName   = resolveRole(event.getDepartmentName(), parentDeptId);

            // absolute safety-net: EMPLOYEE always exists
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.findByName("EMPLOYEE")
                            .orElseThrow(() ->
                                    new IllegalStateException("Default role EMPLOYEE missing in DB")));

            log.info("Assigning role {} to {}", role.getName(), user.getWorkEmail());

            user.getRoles().clear();
            user.getRoles().add(role);
            userRepository.save(user);
        });
    }

    //* ───────────────────────── helpers ───────────────────────── 

    private static String resolveRole(String deptName, Long parentDeptId) {
        if ("PAYROLL".equalsIgnoreCase(deptName)) return "PAYROLL_SPECIALIST";
        if ("HR".equalsIgnoreCase(deptName)) {
            return parentDeptId == null ? "HR_MANAGER" : "PAYROLL_SPECIALIST";
        }
        return "EMPLOYEE";
    }

    //** minimal DTO projection from hrms-backend 
    private static class DepartmentResponse {
        private Long parentDepartmentId;
        public Long getParentDepartmentId() { return parentDepartmentId; }
        public void setParentDepartmentId(Long id) { this.parentDepartmentId = id; }
    }
}*/
