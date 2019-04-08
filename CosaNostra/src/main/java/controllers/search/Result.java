package controllers.search;

public class Result {

		String name;
		String firstName;
		String photoUrl;
		String desc;
		String pageId;
		String type;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getFirstName() {
			return firstName;
		}
		
		public String getType() {
			return type;
		}
		
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getPhotoUrl() {
			return photoUrl;
		}
		public void setPhotoUrl(String photoUrl) {
			this.photoUrl = photoUrl;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getPageId() {
			return pageId;
		}
		public void setPageId(String pageId) {
			this.pageId = pageId;
		}
		public void setType(String type) {
			this.type = type;
		}
		
		public Result(String name, String firstName, String photoUrl, String desc, String pageId, String type) {
			this.name = name;
			this.firstName = firstName;
			this.photoUrl = photoUrl;
			this.desc = desc;
			this.pageId = pageId;
			this.type=type;
		}
		@Override
		public String toString() {
			return "Result [name=" + name + ", firstName=" + firstName + ", photoUrl=" + photoUrl + ", desc=" + desc
					+ ", pageId=" + pageId + ", type=" + type + "]";
		}
		
		
		
			
}
