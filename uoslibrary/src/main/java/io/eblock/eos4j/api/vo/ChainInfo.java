package io.eblock.eos4j.api.vo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author xuhuixiang@ulord.net
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChainInfo {

	public ChainInfo() {

	}

	public ChainInfo(String server_version, String chain_id, String head_block_num, Long last_irreversible_block_num, String last_irreversible_block_id, String head_block_id, Date head_block_time, String head_block_producer, String virtual_block_cpu_limit, String virtual_block_net_limit, String block_cpu_limit, String block_net_limit) {
		this.server_version = server_version;
		this.chain_id = chain_id;
		this.head_block_num = head_block_num;
		this.last_irreversible_block_num = last_irreversible_block_num;
		this.last_irreversible_block_id = last_irreversible_block_id;
		this.head_block_id = head_block_id;
		this.head_block_time = head_block_time;
		this.head_block_producer = head_block_producer;
		this.virtual_block_cpu_limit = virtual_block_cpu_limit;
		this.virtual_block_net_limit = virtual_block_net_limit;
		this.block_cpu_limit = block_cpu_limit;
		this.block_net_limit = block_net_limit;
	}

	@JsonProperty("server_version")
	private String server_version;

	@JsonProperty("chain_id")
	private String chain_id;

	@JsonProperty("head_block_num")
	private String head_block_num;

	@JsonProperty("last_irreversible_block_num")
	private Long last_irreversible_block_num;

	@JsonProperty("last_irreversible_block_id")
	private String last_irreversible_block_id;

	@JsonProperty("head_block_id")
	private String head_block_id;

	@JsonProperty("head_block_time")
	private Date head_block_time;

	@JsonProperty("head_block_producer")
	private String head_block_producer;

	@JsonProperty("virtual_block_cpu_limit")
	private String virtual_block_cpu_limit;

	@JsonProperty("virtual_block_net_limit")
	private String virtual_block_net_limit;

	@JsonProperty("block_cpu_limit")
	private String block_cpu_limit;

	@JsonProperty("block_net_limit")
	private String block_net_limit;

	public String getServerVersion() {
		return server_version;
	}

	public void setServerVersion(String serverVersion) {
		this.server_version = serverVersion;
	}

	public String getChainId() {
		return chain_id;
	}

	public void setChainId(String chainId) {
		this.chain_id = chainId;
	}

	public String getHeadBlockNum() {
		return head_block_num;
	}

	public void setHeadBlockNum(String headBlockNum) {
		this.head_block_num = headBlockNum;
	}

	public Long getLastIrreversibleBlockNum() {
		return last_irreversible_block_num;
	}

	public void setLastIrreversibleBlockNum(Long lastIrreversibleBlockNum) {
		this.last_irreversible_block_num = lastIrreversibleBlockNum;
	}

	public String getLastIrreversibleBlockId() {
		return last_irreversible_block_id;
	}

	public void setLastIrreversibleBlockId(String lastIrreversibleBlockId) {
		this.last_irreversible_block_id = lastIrreversibleBlockId;
	}

	public Date getHeadBlockTime() {
//		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区

		return head_block_time;
	}

	public void setHeadBlockTime(Date headBlockTime)
	{
		       //将string转换成固定
		this.head_block_time = headBlockTime;

	}
	public String getTimeAfterHeadBlockTime(int diffInMilSec) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = this.head_block_time;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add( Calendar.MILLISECOND, diffInMilSec);
		date = c.getTime();
		return sdf.format(date);

	}
//public String getTimeAfterHeadBlockTime(int diffInMilSec) {
//	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//	try {
//		Date date = sdf.parse( this.head_block_time);
//
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.add( Calendar.MILLISECOND, diffInMilSec);
//		date = c.getTime();
//
//		return sdf.format(date);
//
//	} catch (ParseException e) {
//		e.printStackTrace();
//		return this.head_block_time;
//	}
//}
	public String getHeadBlockProducer() {
		return head_block_producer;
	}

	public void setHeadBlockProducer(String headBlockProducer) {
		this.head_block_producer = headBlockProducer;
	}

	public String getVirtualBlockCpuLimit() {
		return virtual_block_cpu_limit;
	}

	public void setVirtualBlockCpuLimit(String virtualBlockCpuLimit) {
		this.virtual_block_cpu_limit = virtualBlockCpuLimit;
	}

	public String getVirtualBlockNetLimit() {
		return virtual_block_net_limit;
	}

	public void setVirtualBlockNetLimit(String virtualBlockNetLimit) {
		this.virtual_block_net_limit = virtualBlockNetLimit;
	}

	public String getBlockCpuLimit() {
		return block_cpu_limit;
	}

	public void setBlockCpuLimit(String blockCpuLimit) {
		this.block_cpu_limit = blockCpuLimit;
	}

	public String getBlockNetLimit() {
		return block_net_limit;
	}

	public void setBlockNetLimit(String blockNetLimit) {
		this.block_net_limit = blockNetLimit;
	}

	public String getHeadBlockId() {
		return head_block_id;
	}

	public void setHeadBlockId(String headBlockId) {
		this.head_block_id = headBlockId;
	}

	@Override
	public String toString() {
		return "ChainInfo{" +
				"serverVersion='" + server_version + '\'' +
				", chainId='" + chain_id + '\'' +
				", headBlockNum='" + head_block_num + '\'' +
				", lastIrreversibleBlockNum=" + last_irreversible_block_num +
				", lastIrreversibleBlockId='" + last_irreversible_block_id + '\'' +
				", headBlockId='" + head_block_id + '\'' +
				", headBlockTime=" + head_block_time +
				", headBlockProducer='" + head_block_producer + '\'' +
				", virtualBlockCpuLimit='" + virtual_block_cpu_limit + '\'' +
				", virtualBlockNetLimit='" + virtual_block_net_limit + '\'' +
				", blockCpuLimit='" + block_cpu_limit + '\'' +
				", blockNetLimit='" + block_net_limit + '\'' +
				'}';
	}
}
