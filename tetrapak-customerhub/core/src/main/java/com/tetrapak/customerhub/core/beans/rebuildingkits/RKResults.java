package com.tetrapak.customerhub.core.beans.rebuildingkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RKResults {

	@SerializedName("data")
	@Expose
	private List<RebuildingKits> data;

	@SerializedName("meta")
	@Expose
	private RKMeta meta;

	public List<RebuildingKits> getData() {
		return data;
	}

	public void setData(List<RebuildingKits> data) {
		this.data = data;
	}

	public RKMeta getMeta() {
		return meta;
	}

	public void setMeta(RKMeta meta) {
		this.meta = meta;
	}

}
