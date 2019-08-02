package io.eblock.eos4j;

import java.util.List;

/**
 * Created by xuhuixiang@ulord.net on 2019/1/14.
 */

public class UosPushBean {

    /**
     * transaction_id : f25eb4366fd53e444e31097d05b12184b1196190ee70ba59bf68e7063091a66f
     */

    private String transaction_id="";
    /**
     * code : 500
     * message : Internal Service Error
     * error : {"code":3050003,"name":"uosio_assert_message_exception","what":"uosio_assert_message assertion failure","details":[]}
     */

    private int code=200;
    private String message;
    private ErrorBean error;

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
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
         * code : 3050003
         * name : uosio_assert_message_exception
         * what : uosio_assert_message assertion failure
         * details : []
         */

        private int code=0;
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
