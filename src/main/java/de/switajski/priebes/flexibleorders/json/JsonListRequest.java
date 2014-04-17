package de.switajski.priebes.flexibleorders.json;

import java.util.List;

public class JsonListRequest<T> {
	private List<T> data;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
