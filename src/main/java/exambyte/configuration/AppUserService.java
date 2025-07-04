package exambyte.configuration;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();

  @Value("${exambyte.rollen.admin}")
  private Set<String> admins;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User originalUser = defaultService.loadUser(userRequest);
    Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    String login = originalUser.<String>getAttribute("login");
    if (admins.contains(login)) {
      System.out.printf("GRANTING ADMIN PRIVILEGES TO USER %s%n", login);
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    } else {
      System.out.printf("DENYING ADMIN PRIVILEGES TO USER %s%n", login);
    }

    return new DefaultOAuth2User(authorities, originalUser.getAttributes(), "login");
  }
}
