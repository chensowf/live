package cn.mz.live.bean;

import java.util.List;

public class MPLiverBean {
    private List<Programs> programs;

    public MPLiverBean(List<Programs> programs){
        this.programs = programs;
    }

    public List<Programs> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Programs> programs) {
        this.programs = programs;
    }

    public class Programs{
        private String recommend_caption,recommend_cover_pic;
        private Live live;

        public Live getLive() {
            return live;
        }

        public void setLive(Live live) {
            this.live = live;
        }

        public String getRecommend_caption() {
            return recommend_caption;
        }

        public void setRecommend_caption(String recommend_caption) {
            this.recommend_caption = recommend_caption;
        }

        public String getRecommend_cover_pic() {
            return recommend_cover_pic;
        }

        public void setRecommend_cover_pic(String recommend_cover_pic) {
            this.recommend_cover_pic = recommend_cover_pic;
        }


        public class Live{
            private Video_stream video_stream;

            public Video_stream getVideo_stream() {
                return video_stream;
            }

            public void setVideo_stream(Video_stream video_stream) {
                this.video_stream = video_stream;
            }

            public class Video_stream{
                private String http_flv_url;

                public String getHttp_flv_url() {
                    return http_flv_url;
                }

                public void setHttp_flv_url(String http_flv_url) {
                    this.http_flv_url = http_flv_url;
                }
            }
        }
    }
}
