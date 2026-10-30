package com.resolveit.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
@Configuration
public class SecurityConfig {
 @Bean public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean public UserDetailsService userDetailsService(PasswordEncoder encoder,
  @Value("${resolveit.demo.student.username}") String su,@Value("${resolveit.demo.student.password}") String sp,
  @Value("${resolveit.demo.admin.username}") String au,@Value("${resolveit.demo.admin.password}") String ap){
  var student=User.withUsername(su).password(encoder.encode(sp)).roles("USER").build();
  var admin=User.withUsername(au).password(encoder.encode(ap)).roles("ADMIN").build();
  return new InMemoryUserDetailsManager(student,admin);
 }
 @Bean public AuthenticationSuccessHandler roleBasedSuccessHandler(){
  return (request,response,authentication)->{
   String selected=request.getParameter("selectedRole");
   boolean admin=hasRole(authentication,"ROLE_ADMIN"), student=hasRole(authentication,"ROLE_USER");
   if(("ADMIN".equals(selected)&&admin)||("STUDENT".equals(selected)&&student)){
    response.sendRedirect(admin?"/admin":"/user"); return;
   }
   if(request.getSession(false)!=null)request.getSession(false).invalidate();
   response.sendRedirect("/login?error=role");
  };
 }
 private static boolean hasRole(Authentication a,String role){
  return a.getAuthorities().stream().anyMatch(x->role.equals(x.getAuthority()));
 }
 @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationSuccessHandler handler)throws Exception{
  http.authorizeHttpRequests(auth->auth.requestMatchers("/","/login","/css/**","/error").permitAll()
   .requestMatchers("/admin/**").hasRole("ADMIN")
   .requestMatchers("/user/**","/complaints/**","/track/**").hasAnyRole("USER","ADMIN")
   .anyRequest().authenticated())
   .formLogin(form->form.loginPage("/login").loginProcessingUrl("/login").successHandler(handler).failureUrl("/login?error=true").permitAll())
   .logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").invalidateHttpSession(true)
    .clearAuthentication(true).deleteCookies("JSESSIONID").permitAll());
  return http.build();
 }
}
