package org.usayi.preta.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.usayi.preta.entities.RevEntity;
import org.usayi.preta.entities.User;


public class RevisionListener implements org.hibernate.envers.RevisionListener
{
	@Override
	public void newRevision(Object revisionEntity)
	{
		RevEntity rev = (RevEntity) revisionEntity;
		rev.setUser(null);
		if( SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().equals( User.class))
			rev.setUser( (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()); 
	}
}
