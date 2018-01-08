package zxwl.com.mywxgre.wxapi;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class Bbean {


    private List<DatasBean> datas;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * bphone : 18210909941
         * time : 2018-01-04 18:44:48
         */

        private String bphone;
        private String time;

        public String getBphone() {
            return bphone;
        }

        public void setBphone(String bphone) {
            this.bphone = bphone;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
