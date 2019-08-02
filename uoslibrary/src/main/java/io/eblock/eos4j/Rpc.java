package io.eblock.eos4j;

import java.text.SimpleDateFormat;

import java.util.TimeZone;

import io.eblock.eos4j.api.service.RpcService;
import io.eblock.eos4j.api.utils.Generator;


/**
 * @author xuhuixiang@ulord.net
 */
public class Rpc {


	public static final String  DEFAULT_ACTION="newaccount";

	public static final String  DEFAULT_ACTION_NEW="buyrambytes";

	private final RpcService rpcService;


	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public Rpc(String baseUrl) {
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		rpcService = Generator.createService(RpcService.class, baseUrl);
	}

}
