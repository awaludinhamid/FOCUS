/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.auth;

import com.safasoft.treeweb.service.UsersService;
import com.safasoft.treeweb.util.SessionUtil;
import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

/**
 * Generate role authority on LDAP user
 * @created Jun 24, 2015
 * @author awal
 */

@Component
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

  protected static Logger logger = Logger.getLogger("service");

       /**
        * Attach authority to logged user from database
        * @param userData
        * @param username
        * @return 
        */
       @Override
       public Collection<GrantedAuthority> getGrantedAuthorities(
                       DirContextOperations userData, String username) {
               Collection<GrantedAuthority> gas = new HashSet<GrantedAuthority>();
               int access = 0;
               try {
                 access =
                         new SessionUtil<UsersService>().getAppContext("usersService")
                         .getById(username)
                         .getUserAccess();
               } catch(NullPointerException npe) {
                 logger.error(npe);
               }
               switch (access) {
                 case 1: gas.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
                 case 2: gas.add(new GrantedAuthorityImpl("ROLE_MEMBER"));
                 case 3: gas.add(new GrantedAuthorityImpl("ROLE_USER"));
                 default: gas.add(new GrantedAuthorityImpl("ROLE_GUEST"));
               }
               return gas;
       }

}
