package controllers.search;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FinalResult {
		
	private String name;
	private String photoUrl;
	private String pageId;
	private String desc;
	private String nationality; // P27
	private String gender; // P21
	private String dateOfBirth; // P569
	private String occupation; // P106
	private String style; // P136
	private Map<String, String> identity;
	private Map<String, String> relatedPagesIds;
	private Map<String, String> relatedServices; //MusicBrainz P434, Twitter P2002, Spotify P1902
	
	

	public FinalResult(String name, String photoUrl, String pageId, String desc, String nationality, String gender,
			String dateOfBirth, String occupation, String style) {
		this.name = name;
		this.photoUrl = photoUrl;
		this.pageId = pageId;
		this.desc = desc;
		this.nationality = nationality;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.occupation = occupation;
		this.style = style;
		this.relatedPagesIds = new HashMap();
		this.relatedServices = new HashMap();
		this.identity = new HashMap();
	}
	

	@Override
	public String toString() {
		return "FinalResult [name=" + name + ", photoUrl=" + photoUrl + ", pageId=" + pageId + ", desc=" + desc
				+ ", nationality=" + nationality + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth
				+ ", occupation=" + occupation + ", style=" + style + ", identity=" + getIdentity() + ", relatedPagesIds="
				+ relatedPagesIds + ", relatedServices=" + relatedServices + "]";
	}

	public Map<String, String> getIdentity() {
		identity.put("Date de naissance",Objects.requireNonNull(dateOfBirth));
		identity.put("Nationalité",Objects.requireNonNull(nationality));
		identity.put("Genre",Objects.requireNonNull(gender));
		identity.put("Occupation",Objects.requireNonNull(occupation));
		identity.put("Style",Objects.requireNonNull(style));
		return identity;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
	public String getPageId() {
		return pageId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public Map<String, String> getRelatedPagesIds() {
		return relatedPagesIds;
	}

	public Map<String, String> getRelatedServices() {
		return relatedServices;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}


}
