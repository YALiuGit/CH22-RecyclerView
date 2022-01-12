package tw.tcnr03.m2206;

public class Post {
    public String Name;
    public String posterThumbnailUrl;
    public String Add;
    public String Content;
    public String Zipcode;
    public String Website;
    public Post( String Name,
                 String posterThumbnailUrl, String Add, String content,String Zipcode,String Website) {

        this.Name = Name;  //名稱
        this.posterThumbnailUrl = posterThumbnailUrl; //圖片
        this.Add = Add;  //住址
        this.Content = content; //描述
        this.Zipcode = Zipcode; //郵遞區號
        this.Website = Website; ////商家網頁
    }
}
