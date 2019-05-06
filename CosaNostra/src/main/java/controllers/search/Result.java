package controllers.search;

import java.io.Serializable;

public class Result implements Serializable {

		private String name;
		private String firstName;
		private String photoUrl;
		private String desc;
		private String pageId;
		private String type;
		private String instanceOf;
		
		public String getInstanceOf() {
			return instanceOf;
		}
		public void setInstanceOf(String instanceOf) {
			this.instanceOf = instanceOf;
		}
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
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((desc == null) ? 0 : desc.hashCode());
			result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
			result = prime * result + ((instanceOf == null) ? 0 : instanceOf.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((pageId == null) ? 0 : pageId.hashCode());
			result = prime * result + ((photoUrl == null) ? 0 : photoUrl.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Result other = (Result) obj;
			if (desc == null) {
				if (other.desc != null)
					return false;
			} else if (!desc.equals(other.desc))
				return false;
			if (firstName == null) {
				if (other.firstName != null)
					return false;
			} else if (!firstName.equals(other.firstName))
				return false;
			if (instanceOf == null) {
				if (other.instanceOf != null)
					return false;
			} else if (!instanceOf.equals(other.instanceOf))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (pageId == null) {
				if (other.pageId != null)
					return false;
			} else if (!pageId.equals(other.pageId))
				return false;
			if (photoUrl == null) {
				if (other.photoUrl != null)
					return false;
			} else if (!photoUrl.equals(other.photoUrl))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
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
		
		public Result(String name, String firstName, String photoUrl, String desc, String pageId, String type,String instanceOf) {
			this.name = name;
			this.firstName = firstName;
			this.photoUrl = photoUrl;
			this.desc = desc;
			this.pageId = pageId;
			this.type=type;
			this.instanceOf=instanceOf;
		}
		@Override
		public String toString() {
			return "Result [name=" + name + ", firstName=" + firstName + ", photoUrl=" + photoUrl + ", desc=" + desc
					+ ", pageId=" + pageId + ", type=" + type + ", instanceOf=" + instanceOf + "]";
		}

		
		
			
}
