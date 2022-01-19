package tw.tcnr03.m220t;

public class Post {
    public String Name;
    public String posterThumbnailUrl;
    public String Add;
    public String Content;
//    public String Zipcode;
    public String Website;
    public  String Start;
    public  String End;
    public String Px;
    public String Py;

    public Post(String Name,
                String posterThumbnailUrl, String Add, String content,String Website, String Start, String End, String Px,String Py) {

        this.Name = Name;  //名稱
        this.posterThumbnailUrl = posterThumbnailUrl; //圖片
        this.Add = Add;  //住址
        this.Content = content; //描述
//        this.Zipcode = Zipcode; //郵遞區號
        this.Website = Website; ////商家網頁
        this.Start = Start;
        this.End = End;
        this.Px = Px;//經度
        this.Py = Py;//緯度
    }
}
