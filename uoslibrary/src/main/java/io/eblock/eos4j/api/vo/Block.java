package io.eblock.eos4j.api.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author xuhuixiang@ulord.net
 *  *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {

	@JsonProperty("timestamp")
	private Date timestamp;

	@JsonProperty("producer")
	private String producer;

	@JsonProperty("confirmed")
	private Long confirmed;

	@JsonProperty("previous")
	private String previous;

	@JsonProperty("transaction_mroot")
	private String transactionMroot;

	@JsonProperty("action_mroot")
	private String actionMroot;

	@JsonProperty("schedule_version")
	private String scheduleVersion;

	@JsonProperty("id")
	private String id;

	@JsonProperty("block_num")
	private Long blockNum;

	@JsonProperty("ref_block_prefix")
	private Long refBlockPrefix;

	public Block() {

	}

	public Block(Date timestamp, String producer, Long confirmed, String previous, String transactionMroot, String actionMroot, String scheduleVersion, String id, Long blockNum, Long refBlockPrefix) {
		this.timestamp = timestamp;
		this.producer = producer;
		this.confirmed = confirmed;
		this.previous = previous;
		this.transactionMroot = transactionMroot;
		this.actionMroot = actionMroot;
		this.scheduleVersion = scheduleVersion;
		this.id = id;
		this.blockNum = blockNum;
		this.refBlockPrefix = refBlockPrefix;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public Long getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Long confirmed) {
		this.confirmed = confirmed;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getTransactionMroot() {
		return transactionMroot;
	}

	public void setTransactionMroot(String transactionMroot) {
		this.transactionMroot = transactionMroot;
	}

	public String getActionMroot() {
		return actionMroot;
	}

	public void setActionMroot(String actionMroot) {
		this.actionMroot = actionMroot;
	}

	public String getScheduleVersion() {
		return scheduleVersion;
	}

	public void setScheduleVersion(String scheduleVersion) {
		this.scheduleVersion = scheduleVersion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(Long blockNum) {
		this.blockNum = blockNum;
	}

	public Long getRefBlockPrefix() {
		return refBlockPrefix;
	}

	public void setRefBlockPrefix(Long refBlockPrefix) {
		this.refBlockPrefix = refBlockPrefix;
	}

	@Override
	public String toString() {
		return "Block{" +
				"timestamp=" + timestamp +
				", producer='" + producer + '\'' +
				", confirmed=" + confirmed +
				", previous='" + previous + '\'' +
				", transactionMroot='" + transactionMroot + '\'' +
				", actionMroot='" + actionMroot + '\'' +
				", scheduleVersion='" + scheduleVersion + '\'' +
				", id='" + id + '\'' +
				", blockNum=" + blockNum +
				", refBlockPrefix=" + refBlockPrefix +
				'}';
	}
}
