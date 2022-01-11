package tw.tcnr03.m2205;

public class Post {
    public String teacherName;
    public String posterThumbnailUrl;
    public String content;

    public Post(String teacherName,
                String posterThumbnailUrl, String content) {
        this.teacherName = teacherName;
        this.posterThumbnailUrl = posterThumbnailUrl;
        this.content = content;
    }
}