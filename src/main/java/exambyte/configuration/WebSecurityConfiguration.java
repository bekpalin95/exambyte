package exambyte.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

  @Bean
  public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
    chainBuilder.authorizeHttpRequests(
            c -> c.requestMatchers("/css/*", "/img/**", "/main").permitAll()
                .anyRequest().authenticated()
        ).logout(l -> l.logoutSuccessUrl("/").permitAll())
        .oauth2Login(Customizer.withDefaults());

    return chainBuilder.build();
  }

}
