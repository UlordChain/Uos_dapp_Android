package io.eblock.eos4j.api.vo;

import java.util.List;

import io.eblock.eos4j.api.vo.transaction.Transaction;

/**
 * Created by xuhuixiang@ulord.net on 2018/7/4.
 */

public class GetAccountSuccessBean {


    /**
     * account_name : eosio
     * head_block_num : 3883107
     * head_block_time : 2018-07-04T06:37:05.000
     * privileged : true
     * last_code_update : 2018-06-27T22:28:00.500
     * created : 2018-06-09T00:00:00.000
     * core_liquid_balance : 2960472677.7019 EOS
     * ram_quota : 99142050
     * net_weight : 1000000000
     * cpu_weight : 1000000000
     * net_limit : {"used":32121,"available":"20087254025","max":"20087286146"}
     * cpu_limit : {"used":17295538,"available":3814844058,"max":3832139596}
     * ram_usage : 40250211
     * permissions : [{"perm_name":"active","parent":"owner","required_auth":{"threshold":1,"keys":[{"key":"EOS6CttW6XFfeRXaiqbD1Hoc9xPfVJrJ9RVZqA8tNTDLriNEyamFY","weight":1}],"accounts":[],"waits":[]}},{"perm_name":"owner","parent":"","required_auth":{"threshold":1,"keys":[{"key":"EOS6CttW6XFfeRXaiqbD1Hoc9xPfVJrJ9RVZqA8tNTDLriNEyamFY","weight":1}],"accounts":[],"waits":[]}}]
     * total_resources : {"owner":"eosio","net_weight":"100000.0000 EOS","cpu_weight":"100000.0000 EOS","ram_bytes":99142050}
     * self_delegated_bandwidth : {"from":"eosio","to":"eosio","net_weight":"100000.0000 EOS","cpu_weight":"100000.0000 EOS"}
     * refund_request : null
     * voter_info : {"owner":"eosio","proxy":"","producers":[],"staked":"4802001100000","last_vote_weight":"0.00000000000000000","proxied_vote_weight":"0.00000000000000000","is_proxy":0}
     */

    private String account_name;
    private int head_block_num;
    private String head_block_time;
    private boolean privileged;
    private String last_code_update;
    private String created;
    private String core_liquid_balance="0.0000 EOS";
    private long ram_quota;
    private long net_weight;
    private long cpu_weight;
    private NetLimitBean net_limit;
    private CpuLimitBean cpu_limit;
    private long ram_usage;
    private TotalResourcesBean total_resources=new TotalResourcesBean();
    private SelfDelegatedBandwidthBean self_delegated_bandwidth=new SelfDelegatedBandwidthBean();
    private RefundRequestBean refund_request=new RefundRequestBean();
    private VoterInfoBean voter_info;
    private List<PermissionsBean> permissions;


    private int code=200;
    private String message;
    private Transaction.ErrorBean error;



    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getHead_block_num() {
        return head_block_num;
    }

    public void setHead_block_num(int head_block_num) {
        this.head_block_num = head_block_num;
    }

    public String getHead_block_time() {
        return head_block_time;
    }

    public void setHead_block_time(String head_block_time) {
        this.head_block_time = head_block_time;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public String getLast_code_update() {
        return last_code_update;
    }

    public void setLast_code_update(String last_code_update) {
        this.last_code_update = last_code_update;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCore_liquid_balance() {
        return core_liquid_balance;
    }

    public void setCore_liquid_balance(String core_liquid_balance) {
        this.core_liquid_balance = core_liquid_balance;
    }

    public long getRam_quota() {
        return ram_quota;
    }

    public void setRam_quota(long ram_quota) {
        this.ram_quota = ram_quota;
    }

    public long getNet_weight() {
        return net_weight;
    }

    public void setNet_weight(int net_weight) {
        this.net_weight = net_weight;
    }

    public long getCpu_weight() {
        return cpu_weight;
    }

    public void setCpu_weight(long cpu_weight) {
        this.cpu_weight = cpu_weight;
    }

    public NetLimitBean getNet_limit() {
        return net_limit;
    }

    public void setNet_limit(NetLimitBean net_limit) {
        this.net_limit = net_limit;
    }

    public CpuLimitBean getCpu_limit() {
        return cpu_limit;
    }

    public void setCpu_limit(CpuLimitBean cpu_limit) {
        this.cpu_limit = cpu_limit;
    }

    public long getRam_usage() {
        return ram_usage;
    }

    public void setRam_usage(long ram_usage) {
        this.ram_usage = ram_usage;
    }

    public TotalResourcesBean getTotal_resources() {
        return total_resources;
    }

    public void setRefund_request(RefundRequestBean refund_request) {
        this.refund_request = refund_request;
    }

    public void setTotal_resources(TotalResourcesBean total_resources) {
        this.total_resources = total_resources;
    }

    public SelfDelegatedBandwidthBean getSelf_delegated_bandwidth() {
        return self_delegated_bandwidth;
    }

    public void setSelf_delegated_bandwidth(SelfDelegatedBandwidthBean self_delegated_bandwidth) {
        this.self_delegated_bandwidth = self_delegated_bandwidth;
    }

    public RefundRequestBean getRefund_request() {
        return refund_request;
    }


    public VoterInfoBean getVoter_info() {
        return voter_info;
    }

    public void setVoter_info(VoterInfoBean voter_info) {
        this.voter_info = voter_info;
    }

    public List<PermissionsBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionsBean> permissions) {
        this.permissions = permissions;
    }

    public static class NetLimitBean {
        /**
         * used : 32121
         * available : 20087254025
         * max : 20087286146
         */

        private int used;
        private String available;
        private String max;

        public int getUsed() {
            return used;
        }

        public void setUsed(int used) {
            this.used = used;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }
    }

    public static class CpuLimitBean {
        /**
         * used : 17295538
         * available : 3814844058
         * max : 3832139596
         */

        private int used;
        private String available;
        private String max;

        public int getUsed() {
            return used;
        }

        public void setUsed(int used) {
            this.used = used;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }
    }

    public static class TotalResourcesBean {
        /**
         * owner : eosio
         * net_weight : 100000.0000 EOS
         * cpu_weight : 100000.0000 EOS
         * ram_bytes : 99142050
         */

        private String owner;
        private String net_weight="0.0000 EOS";
        private String cpu_weight="0.0000 EOS";
        private long ram_bytes=0;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getNet_weight() {
            return net_weight;
        }

        public void setNet_weight(String net_weight) {
            this.net_weight = net_weight;
        }

        public String getCpu_weight() {
            return cpu_weight;
        }

        public void setCpu_weight(String cpu_weight) {
            this.cpu_weight = cpu_weight;
        }

        public long getRam_bytes() {
            return ram_bytes;
        }

        public void setRam_bytes(long ram_bytes) {
            this.ram_bytes = ram_bytes;
        }
    }

    public static class SelfDelegatedBandwidthBean {
        /**
         * from : eosio
         * to : eosio
         * net_weight : 100000.0000 EOS
         * cpu_weight : 100000.0000 EOS
         */

        private String from;
        private String to;
        private String net_weight="0.0000 EOS";
        private String cpu_weight="0.0000 EOS";

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getNet_weight() {
            return net_weight;
        }

        public void setNet_weight(String net_weight) {
            this.net_weight = net_weight;
        }

        public String getCpu_weight() {
            return cpu_weight;
        }

        public void setCpu_weight(String cpu_weight) {
            this.cpu_weight = cpu_weight;
        }
    }

    public static class RefundRequestBean{

        /**
         * owner : xuhuixiang11
         * request_time : 2019-01-15T03:06:12
         * net_amount : 1.0000 UOS
         * cpu_amount : 1.0000 UOS
         */

        private String owner;
        private String request_time;
        private String net_amount="0.0000 EOS";
        private String cpu_amount="0.0000 EOS";

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getRequest_time() {
            return request_time;
        }

        public void setRequest_time(String request_time) {
            this.request_time = request_time;
        }

        public String getNet_amount() {
            return net_amount;
        }

        public void setNet_amount(String net_amount) {
            this.net_amount = net_amount;
        }

        public String getCpu_amount() {
            return cpu_amount;
        }

        public void setCpu_amount(String cpu_amount) {
            this.cpu_amount = cpu_amount;
        }
    }

    public static class VoterInfoBean {
        /**
         * owner : eosio
         * proxy :
         * producers : []
         * staked : 4802001100000
         * last_vote_weight : 0.00000000000000000
         * proxied_vote_weight : 0.00000000000000000
         * is_proxy : 0
         */

        private String owner;
        private String proxy;
        private String staked;
        private String last_vote_weight;
        private String proxied_vote_weight;
        private int is_proxy;
        private List<?> producers;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public String getStaked() {
            return staked;
        }

        public void setStaked(String staked) {
            this.staked = staked;
        }

        public String getLast_vote_weight() {
            return last_vote_weight;
        }

        public void setLast_vote_weight(String last_vote_weight) {
            this.last_vote_weight = last_vote_weight;
        }

        public String getProxied_vote_weight() {
            return proxied_vote_weight;
        }

        public void setProxied_vote_weight(String proxied_vote_weight) {
            this.proxied_vote_weight = proxied_vote_weight;
        }

        public int getIs_proxy() {
            return is_proxy;
        }

        public void setIs_proxy(int is_proxy) {
            this.is_proxy = is_proxy;
        }

        public List<?> getProducers() {
            return producers;
        }

        public void setProducers(List<?> producers) {
            this.producers = producers;
        }
    }

    public static class PermissionsBean {
        /**
         * perm_name : active
         * parent : owner
         * required_auth : {"threshold":1,"keys":[{"key":"EOS6CttW6XFfeRXaiqbD1Hoc9xPfVJrJ9RVZqA8tNTDLriNEyamFY","weight":1}],"accounts":[],"waits":[]}
         */

        private String perm_name;
        private String parent;
        private RequiredAuthBean required_auth;

        public String getPerm_name() {
            return perm_name;
        }

        public void setPerm_name(String perm_name) {
            this.perm_name = perm_name;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public RequiredAuthBean getRequired_auth() {
            return required_auth;
        }

        public void setRequired_auth(RequiredAuthBean required_auth) {
            this.required_auth = required_auth;
        }

        public static class RequiredAuthBean {
            /**
             * threshold : 1
             * keys : [{"key":"EOS6CttW6XFfeRXaiqbD1Hoc9xPfVJrJ9RVZqA8tNTDLriNEyamFY","weight":1}]
             * accounts : []
             * waits : []
             */

            private int threshold;
            private List<KeysBean> keys;
            private List<?> accounts;
            private List<?> waits;

            public int getThreshold() {
                return threshold;
            }

            public void setThreshold(int threshold) {
                this.threshold = threshold;
            }

            public List<KeysBean> getKeys() {
                return keys;
            }

            public void setKeys(List<KeysBean> keys) {
                this.keys = keys;
            }

            public List<?> getAccounts() {
                return accounts;
            }

            public void setAccounts(List<?> accounts) {
                this.accounts = accounts;
            }

            public List<?> getWaits() {
                return waits;
            }

            public void setWaits(List<?> waits) {
                this.waits = waits;
            }

            public static class KeysBean {
                /**
                 * key : EOS6CttW6XFfeRXaiqbD1Hoc9xPfVJrJ9RVZqA8tNTDLriNEyamFY
                 * weight : 1
                 */

                private String key;
                private int weight;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public int getWeight() {
                    return weight;
                }

                public void setWeight(int weight) {
                    this.weight = weight;
                }
            }
        }
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

    public Transaction.ErrorBean getError() {
        return error;
    }

    public void setError(Transaction.ErrorBean error) {
        this.error = error;
    }

    public static class ErrorBean {
        /**
         * code : 3080004
         * name : tx_cpu_usage_exceeded
         * what : Transaction exceeded the current CPU usage limit imposed on the transaction
         * details : []
         */
        private int code;
        private String name;
        private String what;
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
