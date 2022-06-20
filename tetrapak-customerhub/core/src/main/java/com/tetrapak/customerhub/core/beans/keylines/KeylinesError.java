package com.tetrapak.customerhub.core.beans.keylines;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeylinesError {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;

    public int getStatus() {
	return status;
    }

    public void setStatus(int httpBadRequest) {
	this.status = httpBadRequest;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    @Override
    public String toString() {
	return "KeylinesError [status=" + status + ", message=" + message + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((message == null) ? 0 : message.hashCode());
	result = prime * result + status;
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
	KeylinesError other = (KeylinesError) obj;
	if (status != other.status)
	    return false;
	if (message == null) {
	    if (other.message != null) {
		return false;
	    }
	} else if (!message.equals(other.message))
	    return false;

	return true;
    }

}
