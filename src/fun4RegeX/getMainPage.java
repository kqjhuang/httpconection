package fun4RegeX;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getMainPage {

	private String RxzTable = "<table summary=\"forum_144\"(.*?)</table>";
	private String RxzTbody = "<tbody id(.*?)>";
	private String RxzNum = "\\d+";
    private String dateFir;
    private String dateSec;
    public int pageNumMin;
    public int pageNumMax;
    getMainPage(String date1,String date2,int x,int y){

    	pageNumMin = x;
    	pageNumMax = y;
    	dateFir = date1;
    	dateSec = date2;
    }
	public void beforeFuncation(){
		
		try{
			for(int pageNum = pageNumMin;pageNum<pageNumMax;pageNum++){
		String strUrl = "http://www.1point3acres.com/bbs/forum-144-"+pageNum+".html";
    	URL url = new URL(strUrl);
    	
        HttpURLConnection con = (HttpURLConnection) url.openConnection();    
        con.setRequestProperty("User-Agent", "" ); 
        InputStream is = con.getInputStream(); 
        // InputStreamReader 是一个输入流读取器 用于将读取的字节转换成字符,该网页使用的是gbk编码
       InputStreamReader isr = new InputStreamReader(is,"gbk"); 
      
       // 使用 BufferedReader 来读取 InputStreamReader 转换成的字符
       BufferedReader br = new BufferedReader(isr);
       // 新增一个空字符串strRead来装载 BufferedReader 读取到的内容
       String strRead = ""; 
       //存储所有内容
       StringBuffer content=new StringBuffer(); 
       
       while((strRead = br.readLine())!=null){  
           content.append(strRead+"\n");
          }  
       //用一个String来存储最终的页面源码
       String text=content.toString();  
       
       br.close();
       
       String table = "";
       String tbody = "";
       table = getContext(RxzTable,text);
       tbody = getContext(RxzTbody,table);
       ArrayList<Integer> A = new ArrayList<Integer>();
       A = getNum(RxzNum,tbody);
       for(int i = 0;i<A.size();i++){
       funcation deal = new funcation(A.get(i),A.get(i),dateFir,dateSec); 
       deal.process();
       } 
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

  //取得表单内容的正则表达式处理
	private String getContext(String pattern, String matcher) {
		// TODO Auto-generated method stub
        Pattern p = Pattern.compile(pattern,Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(matcher);
        String a = "";
        while (m.find()) { // 如果读到 	
            a = a + m.group() + "\n";// 返回捕获的数据
        }       
        return a;
	
	}
	
	//取得帖子num的正则表达式处理
	private ArrayList<Integer> getNum(String pattern, String matcher){
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(matcher);
        ArrayList<Integer> a = new ArrayList<Integer>();
        while (m.find()) { // 如果读到
        	a.add(Integer.parseInt(m.group()));
        } 
        
        return a;
	}
	
	
	
}
