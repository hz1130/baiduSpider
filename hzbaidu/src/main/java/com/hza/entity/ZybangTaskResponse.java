package com.hza.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ZybangTaskResponse {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataDTO> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("imageId")
        private String imageId;
        @SerializedName("solarId")
        private String solarId;
        @SerializedName("gradeId")
        private String gradeId;
        @SerializedName("courseId")
        private String courseId;
        @SerializedName("dbctime")
        private String dbctime;
        @SerializedName("dbutime")
        private String dbutime;
        @SerializedName("url")
        private String url;
        @SerializedName("test")
        private Boolean test;
        @SerializedName("muti")
        private Boolean muti;

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getSolarId() {
            return solarId;
        }

        public void setSolarId(String solarId) {
            this.solarId = solarId;
        }

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getDbctime() {
            return dbctime;
        }

        public void setDbctime(String dbctime) {
            this.dbctime = dbctime;
        }

        public String getDbutime() {
            return dbutime;
        }

        public void setDbutime(String dbutime) {
            this.dbutime = dbutime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Boolean getTest() {
            return test;
        }

        public void setTest(Boolean test) {
            this.test = test;
        }

        public Boolean getMuti() {
            return muti;
        }

        public void setMuti(Boolean muti) {
            this.muti = muti;
        }
    }
}
