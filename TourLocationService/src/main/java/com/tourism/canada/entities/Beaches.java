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
@Table(name = "beaches")
public class Beaches {

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "beaches_id")
	private int beachesId;

	@Column(name = "beaches_name")
	private String beachesName;

	@Column(name = "beaches_desc", length = 500)
	private String beachesDesc;

	@ManyToOne(fetch = FetchType.EAGER)
	private City city;
	



	
	@Column(name ="image_location")
	@JsonProperty(value = "beachimage")
	private String imageLocation;
	


	public Beaches() {

	}

	/**
	 * @param beachesId
	 */
	public Beaches(Integer beachesId) {
		super();
		this.beachesId = beachesId;
	}

	/**
	 * @param beachesId
	 * @param beachesName
	 * @param beachesDesc
	 * @param city
	 * @param provinces
	 */
	public Beaches(Integer beachesId, String beachesName, String beachesDesc, City city) {
		super();
		this.beachesId = beachesId;
		this.beachesName = beachesName;
		this.beachesDesc = beachesDesc;
		this.city = city;

	}
	
	



	/**
	 * @param beachesId
	 * @param beachesName
	 * @param beachesDesc
	 * @param city
	 * @param imageLocation
	 */
	public Beaches(Integer beachesId, String beachesName, String beachesDesc, City city, String imageLocation) {
		super();
		this.beachesId = beachesId;
		this.beachesName = beachesName;
		this.beachesDesc = beachesDesc;
		this.city = city;
		this.imageLocation = imageLocation;
	}

	/**
	 * @return the beachesId
	 */
	public Integer getBeachesId() {
		return beachesId;
	}

	/**
	 * @param beachesId the beachesId to set
	 */
	public void setBeachesId(Integer beachesId) {
		this.beachesId = beachesId;
	}

	/**
	 * @return the beachesName
	 */
	public String getBeachesName() {
		return beachesName;
	}

	/**
	 * @param beachesName the beachesName to set
	 */
	public void setBeachesName(String beachesName) {
		this.beachesName = beachesName;
	}

	/**
	 * @return the beachesDesc
	 */
	public String getBeachesDesc() {
		return beachesDesc;
	}

	/**
	 * @param beachesDesc the beachesDesc to set
	 */
	public void setBeachesDesc(String beachesDesc) {
		this.beachesDesc = beachesDesc;
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
		return "Beaches [beachesId=" + beachesId + ", beachesName=" + beachesName + ", beachesDesc=" + beachesDesc
				+ ", city=" + city + "]";
	}

}
