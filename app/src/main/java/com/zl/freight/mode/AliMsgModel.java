package com.zl.freight.mode;

/**
 * Created by Administrator on 2018/2/5.
 */

public class AliMsgModel {

    /**
     * alipay_fund_trans_toaccount_transfer_response : {"code":"40004","msg":"Business Failed","sub_code":"PAYEE_USER_INFO_ERROR","sub_msg":"支付宝账号和姓名不匹配，请确认姓名是否正确","out_biz_no":"dc3f14f0-ebc9-4db4-95a1-748be9ec09a3"}
     * sign : f6KkWLxAi4duWc2nWme0afyn4OvVqIJ+yqFTYQjenpc7/97/xkNZLLaB0icC64o9TIw6oodnxcyqtzCbbLUCrsHQg9XWXiCdqScN0J+m5qGUmAYoKctwRgNMV7hj5iuxdbpOqZpxgG5JAGIrP/LzbdpRek5guWmPxl4fLwToJeDGLsziBuaM+Vnka8NwqccZpelCCXTPk8uFugEVfkmfT0r7pMIBLcOl4912LfBiNHVVqIs4IkMKY2VKbpic4tEE0sjCh1t9Lkff6tB78fXC3iWafgd/ccaGuS1dyzOqHzqVH1U/mQ04/x/jT/6dH78znOPh/whDMlVVnKjIlnklEw==
     */

    private AlipayBean alipay_fund_trans_toaccount_transfer_response;

    public AlipayBean getAlipay_fund_trans_toaccount_transfer_response() {
        return alipay_fund_trans_toaccount_transfer_response;
    }

    public void setAlipay_fund_trans_toaccount_transfer_response(AlipayBean alipay_fund_trans_toaccount_transfer_response) {
        this.alipay_fund_trans_toaccount_transfer_response = alipay_fund_trans_toaccount_transfer_response;
    }

    public static class AlipayBean {
        /**
         * code : 40004
         * msg : Business Failed
         * sub_code : PAYEE_USER_INFO_ERROR
         * sub_msg : 支付宝账号和姓名不匹配，请确认姓名是否正确
         * out_biz_no : dc3f14f0-ebc9-4db4-95a1-748be9ec09a3
         */

        private String code;
        private String msg;
        private String sub_code;
        private String sub_msg;
        private String out_biz_no;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSub_code() {
            return sub_code;
        }

        public void setSub_code(String sub_code) {
            this.sub_code = sub_code;
        }

        public String getSub_msg() {
            return sub_msg;
        }

        public void setSub_msg(String sub_msg) {
            this.sub_msg = sub_msg;
        }

        public String getOut_biz_no() {
            return out_biz_no;
        }

        public void setOut_biz_no(String out_biz_no) {
            this.out_biz_no = out_biz_no;
        }
    }
}
