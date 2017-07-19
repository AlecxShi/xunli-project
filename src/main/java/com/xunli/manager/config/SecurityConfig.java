package com.xunli.manager.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.xunli.manager.security.AjaxAuthenticationFailureHandler;
import com.xunli.manager.security.AjaxAuthenticationSuccessHandler;
import com.xunli.manager.security.AjaxLogoutSuccessHandler;
import com.xunli.manager.security.Http401UnauthorizedEntryPoint;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

  @Autowired
  private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

  @Autowired
  private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

  @Autowired
  private Http401UnauthorizedEntryPoint authenticationEntryPoint;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private RememberMeServices rememberMeServices;

  @Autowired
  private AppProperties appProperties;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AccessDecisionManager accessDecisionManager() {
      RoleVoter roleVoter = new RoleVoter();
      roleVoter.setRolePrefix("");
      final AffirmativeBased acd = new AffirmativeBased(Arrays.<AccessDecisionVoter<?>>
          asList(roleVoter));
      return acd;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
	// 设置不拦截规则
    web.ignoring()
    .antMatchers("/bower_components/**")
    .antMatchers("/fonts/**")
    .antMatchers("/images/**")
    .antMatchers("/scripts/**")
    .antMatchers("/styles/**")
    .antMatchers("/views/**")
    .antMatchers("/assets/**")
    .antMatchers("/resources/**")
    .antMatchers("/swagger-ui/**")
    .antMatchers("/h2-console/**")
    .antMatchers("/test/**");
  }

  public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOrigin("*");
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
    	.addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
//    .requestMatcher(CorsUtils::isPreFlightRequest)
//  .csrf()
//    .ignoringAntMatchers("/websocket/**")
//  .and()
//    .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
  .csrf()
    .disable()
  .exceptionHandling()
  .authenticationEntryPoint(authenticationEntryPoint)
  .and()
    .rememberMe()
    .rememberMeServices(rememberMeServices)
    .rememberMeParameter("remember-me")
    .key(appProperties.getSecurity().getKey())
  .and()
    .formLogin()
    .loginProcessingUrl("/api/authentication")
    .successHandler(ajaxAuthenticationSuccessHandler)
    .failureHandler(ajaxAuthenticationFailureHandler)
    .usernameParameter("j_username")
    .passwordParameter("j_password")
    .permitAll()
  .and()
    .logout()
    .logoutUrl("/api/logout")
    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
    .deleteCookies("JSESSIONID")
    .permitAll()
  .and()
    .headers()
    .frameOptions()
    .disable()
  .and()
    .authorizeRequests()
    .antMatchers("/test/**").permitAll()
    .antMatchers("/v2/api-docs/**").permitAll()
    .antMatchers("/config/**").permitAll()
    .antMatchers("/auth/**").permitAll()
    .antMatchers("/api/validate/**").permitAll()
    .antMatchers("/api/register").permitAll()
    .antMatchers("/api/activate").permitAll()
    .antMatchers("/api/authenticate").permitAll()
    .antMatchers("/api/reset").permitAll()
    .antMatchers("/api/NextGroupInfo/**").permitAll()



    .antMatchers("/api/hotSearch").permitAll()
    .antMatchers("/api/GroupInfo/AllPublic").permitAll()
    .antMatchers("/api/GroupInfo/IfPublic").permitAll()
    .antMatchers("/api/ApiInfo/PageQuery/manyConditions").permitAll()
    .antMatchers("/api/ApiInfo/getAllByGroupIdAndIfPublic").permitAll()
    .antMatchers("/api/ApiInfo/getApiInfoByApiId").permitAll()
    .antMatchers("/api/getUpgradeInfo").permitAll()
    .antMatchers("/api/ApiInfo/associate").permitAll()
    .antMatchers("/api/ApiInfo/exportAsExcel").permitAll()

    //.antMatchers("/api/**").authenticated()
    .anyRequest().authenticated();
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }
}
