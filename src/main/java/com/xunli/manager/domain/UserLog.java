package com.xunli.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "log_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLog extends AbstractAuditingEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;//

	@Column(name = "auth_id")
	private String authId;// 用户认证ID

	@Column(name = "ip")
	private String ip;

	@Column(name = "request_path")
	private String requestPath;// 请求路径

	@Column(name = "request_params")
	private String requestParams;// 请求参数

	@Column(name = "return_count")
	private Integer returnCount;// 返回记录数

	@Column(name = "return_result")
	private Boolean returnResult;// 返回结果

	@Column(name = "request_time")
	private Long requestTime;// 请求日期

	@Column(name = "return_time")
	private Long returnTime;

	@Column(name = "return_size")
	private Integer returnSize;

	public UserLog(){}

	/*public UserLog(com.gildaas.API_core.model.gemfire.UserLog log){
		this.authId = log.getAuthId();
		this.ip = log.getIp();
		this.requestPath = log.getRequestPath();
		this.requestParams = log.getRequestParams();
		this.returnCount = log.getReturnCount();
		this.returnResult = log.getReturnResult();
		this.requestTime = log.getRequestTime();
		this.returnTime = log.getReturnTime();
		this.returnSize = log.getReturnSize();
	}*/

	public Long getID() {
		return ID;
	}

	@JsonIgnore
	public void setID(Long iD) {
		ID = iD;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public Integer getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}

	public Boolean getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(Boolean returnResult) {
		this.returnResult = returnResult;
	}

	public Long getRequestTime() {
		return requestTime;
	}
	

	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}

	public Long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Long returnTime) {
		this.returnTime = returnTime;
	}

	public Integer getRequestSize() {
		return returnSize;
	}

	public void setRequestSize(Integer requestSize) {
		this.returnSize = requestSize;
	}
}
