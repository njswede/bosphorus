package com.vmware.samples.bosphorus.live;

public class LiveSubscription {
	private final String type;
	
	private final String subtype;

	public LiveSubscription(String type, String subType) {
		super();
		this.type = type;
		this.subtype = subType;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}
	
	public boolean matches(LiveEvent e) {
		return (type.equals("*") || e.getType().equals(type)) &&
				(subtype.equals("*") || e.getSubtype().equals(subtype));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
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
		LiveSubscription other = (LiveSubscription) obj;
		if (subtype == null) {
			if (other.subtype != null)
				return false;
		} else if (!subtype.equals(other.subtype))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
