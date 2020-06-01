package com.tourism.canada.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "national_park")
public class NationalPark {

	@Id
	// @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "national_park_id")
	private int nationalParkId;

	@ManyToOne(fetch = FetchType.EAGER)
	private City city;

	@Column(name = "national_park_name")
	private String nationalParkName;

	@Column(name = "national_park_desc", length = 500)
	private String nationalParkDesc;
	
	
	
	@Column(name ="image_location")
	@JsonProperty(value = "parkimage")
	private String imageLocation;
	

	/**
	 * 
	 */
	public NationalPark() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nationalParkId
	 * @param provinces
	 * @param nationalParkName
	 * @param nationalParkDesc
	 */
	public NationalPark(Integer nationalParkId, City cities, String nationalParkName, String nationalParkDesc) {
		super();
		this.nationalParkId = nationalParkId;
		this.city = cities;
		this.nationalParkName = nationalParkName;
		this.nationalParkDesc = nationalParkDesc;
	}
	
	

	

	/**
	 * @param nationalParkId
	 * @param city
	 * @param nationalParkName
	 * @param nationalParkDesc
	 * @param imageLocation
	 */
	public NationalPark(Integer nationalParkId, City city, String nationalParkName, String nationalParkDesc,
			String imageLocation) {
		super();
		this.nationalParkId = nationalParkId;
		this.city = city;
		this.nationalParkName = nationalParkName;
		this.nationalParkDesc = nationalParkDesc;
		this.imageLocation = imageLocation;
	}

	/**
	 * @return the nationalParkId
	 */
	public Integer getNationalParkId() {
		return nationalParkId;
	}

	/**
	 * @param nationalParkId the nationalParkId to set
	 */
	public void setNationalParkId(Integer nationalParkId) {
		this.nationalParkId = nationalParkId;
	}


	/**
	 * @return the nationalParkName
	 */
	public String getNationalParkName() {
		return nationalParkName;
	}

	/**
	 * @param nationalParkName the nationalParkName to set
	 */
	public void setNationalParkName(String nationalParkName) {
		this.nationalParkName = nationalParkName;
	}

	/**
	 * @return the nationalParkDesc
	 */
	public String getNationalParkDesc() {
		return nationalParkDesc;
	}

	/**
	 * @param nationalParkDesc the nationalParkDesc to set
	 */
	public void setNationalParkDesc(String nationalParkDesc) {
		this.nationalParkDesc = nationalParkDesc;
	}
	
	

	/**
	 * @return the city
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(City city) {
		this.city = city;
	}

	

	
	

	/**
	 * @return the imageLocation
	 */
	public String getImageLocation() {
		return imageLocation;
	}

	/**
	 * @param imageLocation the imageLocation to set
	 */
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	@Override
	public String toString() {
		return "NationalPark [nationalParkId=" + nationalParkId + ", cities=" + city + ", nationalParkName="
				+ nationalParkName + ", nationalParkDesc=" + nationalParkDesc + "]";
	}

}