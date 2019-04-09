package controllers.search;

public class FinalResult {
		
	private String name;
	private String photoUrl;
	private String pageId;
	
	
	public FinalResult(String name, String photoUrl, String pageId) {
		super();
		this.name = name;
		this.photoUrl = photoUrl;
		this.pageId = pageId;
		
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
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}


}
