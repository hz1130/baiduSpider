package com.hza.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class SignJson {

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SignJson{" +
                "data=" + data +
                '}';
    }

    @JsonProperty("errno")
    private Integer errno;

    @JsonProperty("data")
    private DataDTO data;
    

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("imgsearch")
        private ImgsearchDTO imgsearch;

        public ImgsearchDTO getImgsearch() {
            return imgsearch;
        }

        public void setImgsearch(ImgsearchDTO imgsearch) {
            this.imgsearch = imgsearch;
        }


        @NoArgsConstructor
        @Data
        public static class ImgsearchDTO {
            @JsonProperty("qpsearch")
            private QpsearchDTO qpsearch;

            public QpsearchDTO getQpsearch() {
                return qpsearch;
            }

            public void setQpsearch(QpsearchDTO qpsearch) {
                this.qpsearch = qpsearch;
            }

            @NoArgsConstructor
            @Data
            public static class QpsearchDTO {
                @JsonProperty("status")
                private Integer status;
                @JsonProperty("version")
                private String version;
                @JsonProperty("dataset")
                private DatasetDTO dataset;

                public DatasetDTO getDataset() {
                    return dataset;
                }

                public void setDataset(DatasetDTO dataset) {
                    this.dataset = dataset;
                }

                @NoArgsConstructor
                @Data
                public static class DatasetDTO {
                    @JsonProperty("rectSet")
                    private List<RectSetDTO> rectSet;
                    @JsonProperty("tag")
                    private String tag;
                    @JsonProperty("isTranslate")
                    private String isTranslate;
                    @JsonProperty("retkey")
                    private Object retkey;
                    @JsonProperty("imgkey")
                    private String imgkey;
                    @JsonProperty("command")
                    private CommandDTO command;

                    public CommandDTO getCommand() {
                        return command;
                    }

                    public void setCommand(CommandDTO command) {
                        this.command = command;
                    }

                    @NoArgsConstructor
                    @Data
                    public static class CommandDTO {
                        @Override
                        public String toString() {
                            return "CommandDTO{" +
                                    "thumbUrl='" + thumbUrl + '\'' +
                                    '}';
                        }

                        public String getImgUrl() {
                            return imgUrl;
                        }

                        public void setImgUrl(String imgUrl) {
                            this.imgUrl = imgUrl;
                        }

                        public String getThumbUrl() {
                            return thumbUrl;
                        }

                        public void setThumbUrl(String thumbUrl) {
                            this.thumbUrl = thumbUrl;
                        }

                        public String getThumbUrlSmall() {
                            return thumbUrlSmall;
                        }

                        public void setThumbUrlSmall(String thumbUrlSmall) {
                            this.thumbUrlSmall = thumbUrlSmall;
                        }

                        @JsonProperty("mode")
                        private String mode;
                        @JsonProperty("append")
                        private String append;
                        @JsonProperty("imgUrl")
                        private String imgUrl;
                        @JsonProperty("thumbUrl")
                        private String thumbUrl;
                        @JsonProperty("thumbUrlSmall")
                        private String thumbUrlSmall;
                        @JsonProperty("url")
                        private String url;
                    }

                    @NoArgsConstructor
                    @Data
                    public static class RectSetDTO {
                        @JsonProperty("rect")
                        private RectDTO rect;

                        @NoArgsConstructor
                        @Data
                        public static class RectDTO {
                            @JsonProperty("x1")
                            private Double x1;
                            @JsonProperty("x2")
                            private Double x2;
                            @JsonProperty("y1")
                            private Double y1;
                            @JsonProperty("y2")
                            private Double y2;
                        }
                    }
                }
            }
        }
    }
}
