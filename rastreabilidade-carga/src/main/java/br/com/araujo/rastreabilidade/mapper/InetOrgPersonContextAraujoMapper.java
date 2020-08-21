package br.com.araujo.rastreabilidade.mapper;

import java.util.Collection;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.util.Assert;

public class InetOrgPersonContextAraujoMapper implements UserDetailsContextMapper {

	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		InetOrgPersonAraujo.Essence p = new InetOrgPersonAraujo.Essence(ctx);

		p.setUsername(username);
		p.setAuthorities(authorities);
		
		return p.createUserDetails();

	}

	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		Assert.isInstanceOf(InetOrgPersonAraujo.class, user,
				"UserDetails must be an InetOrgPerson instance");

		InetOrgPersonAraujo p = (InetOrgPersonAraujo) user;
		p.populateContext(ctx);
	}
}

