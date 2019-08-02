package io.eblock.eos4j.api.vo.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;

import java.util.List;

/**
 * 
 * @author xuhuixiang@ulord.net
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction extends Base {

	@JsonProperty("transaction_id")
	private String transaction_id;

	@JsonProperty("processed")
	private Processed processed;
	/**
	 * code : 500
	 * message : Internal Service Error
	 * error : {"code":3080004,"name":"tx_cpu_usage_exceeded","what":"Transaction exceeded the current CPU usage limit imposed on the transaction","details":[]}
	 */
	@JsonProperty("code")
	private int code;
	@JsonProperty("message")
	private String message;
	@JsonProperty("error")
	private ErrorBean error;

	public String getTransactionId() {
		return transaction_id;
	}

	public void setTransactionId(String transactionId) {
		this.transaction_id = transactionId;
	}

	public Processed getProcessed() {
		return processed;
	}

	public void setProcessed(Processed processed) {
		this.processed = processed;
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"transactionId='" + transaction_id + '\'' +
				", processed=" + processed +
				'}';
	}



	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorBean getError() {
		return error;
	}

	public void setError(ErrorBean error) {
		this.error = error;
	}

	public static class ErrorBean {
		/**
		 * code : 3080004
		 * name : tx_cpu_usage_exceeded
		 * what : Transaction exceeded the current CPU usage limit imposed on the transaction
		 * details : []
		 */
		@JsonProperty("code")
		private int code;
		@JsonProperty("name")
		private String name;
		@JsonProperty("what")
		private String what;
		@JsonProperty("details")
		private List<?> details;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getWhat() {
			return what;
		}

		public void setWhat(String what) {
			this.what = what;
		}

		public List<?> getDetails() {
			return details;
		}

		public void setDetails(List<?> details) {
			this.details = details;
		}
	}
}
