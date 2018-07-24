package tools;


public class GetRoute {

    public String realPath;

    public GetRoute () {
        realPath = GetRoute.class.getClassLoader().getResource("").getFile();
        java.io.File file = new java.io.File(realPath);
        realPath = file.getAbsolutePath();
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}