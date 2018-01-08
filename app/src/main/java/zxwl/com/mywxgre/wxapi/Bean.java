package zxwl.com.mywxgre.wxapi;

import java.util.List;

/**
 * Created by Administrator on 2017/12/29.
 */

public class Bean {

    /**
     * login_result : true
     */

    private boolean login_result;
    /**
     * register_result : true
     */

    private boolean register_result;
    /**
     * login : 0
     */

    private String login;
    /**
     * vip : 1514968312
     */

    private String vip;
    /**
     * time : 2018-01-06 14:48:32
     */

    private String time;
    /**
     * modify_result : true
     */

    private boolean modify_result;
    private List<DatasBean> datas;
    /**
     * id : 1
     */

    private String id;

    public boolean isLogin_result() {
        return login_result;
    }

    public void setLogin_result(boolean login_result) {
        this.login_result = login_result;
    }

    public boolean isRegister_result() {
        return register_result;
    }

    public void setRegister_result(boolean register_result) {
        this.register_result = register_result;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public boolean isModify_result() {
        return modify_result;
    }

    public void setModify_result(boolean modify_result) {
        this.modify_result = modify_result;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static class DatasBean {
        /**
         * edition : 1
         * address : http://www.zxwlwh.com/pn/1702BTK.apk
         */

        private String edition;
        private String address;
        private String img;

        public String getEdition() {
            return edition;
        }

        public void setEdition(String edition) {
            this.edition = edition;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }



}
