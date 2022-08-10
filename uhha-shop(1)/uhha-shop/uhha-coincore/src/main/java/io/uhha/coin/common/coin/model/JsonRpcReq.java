
package io.uhha.coin.common.coin.model;

import java.util.List;

/**
* @auther: Water
* @time: 22 Jan 2018 14:37:57
* 
*/

public class JsonRpcReq {
	
//	'{"jsonrpc":"2.0","method":"eth_getCompilers","params":[],"id":1}'
	private String jsonrpc = "2.0";
	private String method;
	private List params;
	private Integer id=1;
	
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List getParams() {
		return params;
	}
	public void setParams(List params) {
		this.params = params;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
