
// PasswordEncoderConfig.java
// Path: hrms-authentication/src/main/java/com/hrms/auth/config/PasswordEncoderConfig.java

package com.hrms.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



/*package com.hrms.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//** Separate @Configuration to break the SecurityConfig ⇄ AuthServiceImpl loop 
@Configuration
public class PasswordEncoderConfig {

    @Bean               // static bean no longer tied to SecurityConfig's lifecycle
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
*/