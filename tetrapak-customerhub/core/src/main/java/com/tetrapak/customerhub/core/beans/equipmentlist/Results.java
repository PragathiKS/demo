package com.tetrapak.customerhub.core.beans.equipmentlist;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {

	@SerializedName("data")
    @Expose
	private List<Equipments> data;

	public List<Equipments> getData() {
		return data;
	}

	public void setData(List<Equipments> data) {
		this.data = data;
	}
	
	
}
