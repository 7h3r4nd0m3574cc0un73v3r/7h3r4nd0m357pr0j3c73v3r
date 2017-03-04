package org.usayi.preta.config;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.usayi.preta.entities.RevEntity;
import org.usayi.preta.entities.User;

public class UserRevisionListener implements RevisionListener
{
	@Override
	public void newRevision(Object revision)
	{
		RevEntity rev = (RevEntity) revision;
		
		if( SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser")
			rev.setUser( null);
		else {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			rev.setUser( user);
		}
	}

}
