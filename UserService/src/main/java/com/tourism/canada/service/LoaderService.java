package com.tourism.canada.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.tourism.canada.dao.RoleDao;
import com.tourism.canada.entities.Role;


@Component
public class LoaderService implements ApplicationListener<ApplicationReadyEvent>{


	
	@Autowired
	private RoleDao roledao;
	

	public void loadData() {
		
		loadRole();
	
	}

		
	public void loadRole()
	{
		Role role = new Role(1, "ADMIN");
		roledao.save(role);
		Role role1 = new Role(2, "USER");
		roledao.save(role1);
	}
	
	


	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		// TODO Auto-generated method stub
		loadData();
	}
}
