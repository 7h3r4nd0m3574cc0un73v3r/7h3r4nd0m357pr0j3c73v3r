package org.usayi.preta.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.usayi.preta.dao.IPretaDAO;

@Service
public class SchedulerStaticDAOImpl
{
	@Autowired
	private static IPretaDAO dao;

	public static IPretaDAO getDao()
	{
		return dao;
	}

	public static void setDao(IPretaDAO dao)
	{
		SchedulerStaticDAOImpl.dao = dao;
	}
	
	
}
