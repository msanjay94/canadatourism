package com.tourism.canada.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Scanner;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import com.tourism.canada.dao.BeachesDao;
import com.tourism.canada.dao.CityDao;
import com.tourism.canada.dao.NationalParkDao;
import com.tourism.canada.entities.Beaches;
import com.tourism.canada.entities.City;
import com.tourism.canada.entities.NationalPark;

@Component
public class LoaderService implements ApplicationListener<ApplicationReadyEvent>{



	@Autowired
	CityDao cityDao;

	@Autowired
	private BeachesDao beachdao;
	

	@Autowired
	NationalParkDao nationalParkDao;
	
	
	

	public void loadData() {
		
		try {
			loadParks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			loadBeaches();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void loadBeaches() throws IOException
	{
		URL url = new URL("https://canadatourism.s3.amazonaws.com/files/BeachDetails.txt");
		
		Scanner sc = new Scanner(url.openStream());
		sc.useDelimiter("\r\n");
		

		while (sc.hasNext()) {

			//System.out.println(sc.next());
			String line = sc.next();
			line = line.replaceAll("\\uFEFF", "");
			String[] beachDetails = line.split("~");
			
			Beaches beach = new Beaches(Integer.parseInt(beachDetails[0].toString().trim()), beachDetails[1], beachDetails[3], new City(Integer.parseInt(beachDetails[2])),"https://static.travelsca.com/images/beaches/"+beachDetails[0]+".jpg");
			System.out.println(beach.toString());
			beachdao.save(beach);
		}
		sc.close();
		
	}

	public void loadParks() throws IOException
	{
		URL url = new URL("https://canadatourism.s3.amazonaws.com/files/ParkDetails.txt");
		Scanner sc = new Scanner(url.openStream());
		sc.useDelimiter("\r\n");
		

		while (sc.hasNext()) {

			//System.out.println(sc.next());
			String line = sc.next();
			line = line.replaceAll("\\uFEFF", "");
			String[] parksDetails = line.split("~");
			
			NationalPark park = new NationalPark(Integer.parseInt(parksDetails[0].toString().trim()), new City(Integer.parseInt(parksDetails[2])), parksDetails[1], parksDetails[3], "https://static.travelsca.com/images/parks/"+parksDetails[0]+".jfif");
			System.out.println(park.toString());
			nationalParkDao.save(park);
		}
		sc.close();
		
	}
	

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		// TODO Auto-generated method stub
		loadData();
	}
}
