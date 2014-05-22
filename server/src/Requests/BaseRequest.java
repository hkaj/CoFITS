package Requests;

import Constants.RequestConstants.requestTypes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "type")
	@JsonSubTypes({
	    @Type(value = SelectRequest.class, name = "SelectRequest"),
	    @Type(value = AddRequest.class, name = "AddRequest"),
	    @Type(value = UploadRequest.class, name = "UploadRequest"),
	    @Type(value = DownloadRequest.class, name = "DownloadRequest"),
	    @Type(value = LinkRequest.class, name = "LinkRequest")})
public interface BaseRequest
{
	public abstract requestTypes getRequestType();
	public abstract String toJSON();
	public abstract String toSQL();
}
