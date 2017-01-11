package com.candao.www.dataserver.service.member;

import com.candao.www.dataserver.mapper.MemberLogMapper;
import com.candao.www.webroom.model.MemberTransModel;
import com.candao.www.webroom.model.PadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MemberHandler {
    private static Logger LOG = LoggerFactory.getLogger(MemberHandler.class);
    @Autowired
    private MemberLogMapper memberLogMapper;
    protected PadConfig padConfig;

    public void init(PadConfig padConfig) {
        this.padConfig = padConfig;
    }

    /**
     * 会员储值
     *
     * @param param
     * @return
     */
    public String storeCardDeposit(String param) {
        Result<MemberTransModel> result = StoreCardDeposit(param);
        if (result.isSuccess()) {
            try {
                MemberTransModel memberTransModel = result.getData();
                memberLogMapper.insert(memberTransModel);
            } catch (Exception e) {
                LOG.error("-->", e);
            }
        }
        return result.getResult().toString();
    }

    protected abstract Result<MemberTransModel> StoreCardDeposit(String param);

    /**
     * 异常信息
     *
     * @param param
     * @param errMsg
     * @return
     */
    public abstract String errorResult(String param, String errMsg);

    protected class Result<T> {
        private boolean success;
        private Object result;
        private T data;

        public Result() {
            super();
        }

        public Result(boolean success) {
            super();
            this.success = success;
        }

        public Result(boolean success, Object result) {
            super();
            this.success = success;
            this.result = result;
        }

        public Result(boolean success, Object result, T data) {
            super();
            this.success = success;
            this.result = result;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

    }
}
