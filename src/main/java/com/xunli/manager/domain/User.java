package com.xunli.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunli.manager.annotation.ColumnComment;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "users")
public class User extends AbstractAuditingEntity implements UserDetails
{

	private static final long serialVersionUID = 2568228987558323960L;

	@NotNull
	@Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
	@Size(min = 0, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	@ColumnComment("用户名")
	private String username;

	@JsonIgnore
	@Size(min = 0, max = 100)
	@Column(length = 100)
	@ColumnComment("密码")
	private String password;

	@Size(min = 0, max = 50)
	@Column(length = 50)
	@ColumnComment("昵称")
	private String nickname;

	@Size(min = 0, max = 50)
	@Column(length = 50)
	@ColumnComment("姓名")
	private String name;

	@Email
	@Size(min = 0, max = 255)
	@Column(length = 255)
	@ColumnComment("邮箱")
	private String email;

	@Size(min = 0, max = 255)
	@Column(length = 255)
	@ColumnComment("电话")
	private String phone;

	@Size(min = 0, max = 255)
	@Column(length = 255)
	@ColumnComment("头像")
	private String avatar;

	@Column(nullable = false)
	@ColumnComment("是否激活")
	private boolean activated = false;

	@Size(min = 0, max = 20)
	@Column(name = "activation_key", length = 20)
	@ColumnComment("激活码")
	private String activationKey;

	@Size(min = 0, max = 20)
	@Column(name = "reset_key", length = 20)
	@ColumnComment("重置码")
	private String resetKey;

	@Column(name = "reset_date")
	@ColumnComment("重置日期")
	private LocalDateTime resetDate;

	@Column(nullable = false)
	@ColumnComment("是否可用")
	private boolean enabled = true;

	@ManyToMany(fetch = FetchType.EAGER)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<Role>(0);

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private Set<PersistentLogin> persistentLogins;

	//返回给前端的权限
	@JsonProperty("authorities")
  public Set<String> getAuthoritiesStr() {
    Set<String> authorities = new HashSet<>();
    for(Role role : getRoles()){
      Set<String> as = role.getAuthorities().stream()
              .map(authority -> new String("ROLE_" + authority.getAuthorityCode()))
              .collect(Collectors.toSet());
      authorities.addAll(as);
    }
    return authorities;
  }

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	public String getActivationKey()
	{
		return activationKey;
	}

	public void setActivationKey(String activationKey)
	{
		this.activationKey = activationKey;
	}

	public String getResetKey()
	{
		return resetKey;
	}

	public void setResetKey(String resetKey)
	{
		this.resetKey = resetKey;
	}

	public LocalDateTime getResetDate()
	{
		return resetDate;
	}

	public void setResetDate(LocalDateTime resetDate)
	{
		this.resetDate = resetDate;
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public Set<PersistentLogin> getPersistentLogins()
	{
		return persistentLogins;
	}

	public void setPersistentLogins(Set<PersistentLogin> persistentLogins)
	{
		this.persistentLogins = persistentLogins;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	@JsonIgnore
  @Override
  public Set<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new HashSet<>();
    for(Role role : getRoles()){
      Set<GrantedAuthority> as = role.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getAuthorityCode()))
              .collect(Collectors.toSet());
      authorities.addAll(as);
    }
    //.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
    return authorities;
  }

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired()
	{
		return true;

	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj == this)
		{
			return true;
		}
		if (obj.getClass() != getClass())
		{
			return false;
		}
		User rhs = (User) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(this.getId(), rhs.getId()).isEquals();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", getId()).append("username", username).append("name", name).append("activated", activated).toString();
	}

}
