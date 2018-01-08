package zxwl.com.mywxgre.wxapi;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class BBBean  {

    private List<DatasBean> datas;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * Image : 123
         */

        private String Image;

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }
    }
}
