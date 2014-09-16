package fun4RegeX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcation {
	
	
	protected Format format = new SimpleDateFormat("yyyy-MM-dd");
	
	
    public String HaveAnoPage = "<span title=\"(.*?)</span></label><a";
    public String getNum = "\\d+";
     
    public String rxz1 = "(\\[<font color=\")(.*?)(]</li>|]</span>)";
    public String rxz3 = "<title>(.*?)</title>";
    public String rxz2 = "<table cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"t_f\" id=\"(.*?)(</td></tr></table>)";
   
    public String deleteHtml1 = "<[^>]+>|]|\\[";
    public String deleteHtml2 = "<h3><strong>(.*?)</a> </p>|(<font class=\"jammer\">(.*?)/font>)|(<span(.*?)/span>)|(<[^>]+>)";
    public String deleteHtml3 = "(?s)(<h3><strong>(.*?)</a> </p>)|(<blockquote>(.*?)</blockquote>)";
    
    public String replaceSpace = "&nbsp;";
    
    //这个表达式是不全面的  因为日期实在是太繁琐 变化太多，这个能匹配大多数
    public String checkDate = "\\d{4}-(1[012]|0?\\d)-(0?[1-9]|[12][0-9]|3[01])";     //日期格式XXXX-X-XX
    public String checkNum = "\\d+";
    public String getDate = "(?s)<em(.*?)</em>";
    
    private int min;
    private int max;
    private Date dateFir;
    private Date dateSec;
    public boolean pagecheck = true;
//    private int threadNum;
//    private int pageNum; 
    //起始日期
//    private int year1;
//    private int month1;
//    private int day1;
//    //最终日期
//    private int year2;
//    private int month2;
//    private int day2;
    
//    private String START;
//    private String END;
    funcation(){
    }
    
    funcation(int x,int y,String date1,String date2){
    	this.min = x;
    	this.max = y;
//    	this.START = date1;
//    	this.END = date2;
    	//检验日期
    	getNum(date1,date2);

    }
    



    
    //处理函数
    
    public void process(){
    	
        String strUrl;
        String temp;
        //System.out.print(1111);
        
        
    	try{
    		int z =min;
    	//for(int z = min;z<max;z++ ){
    		//System.out.print(z);
    	strUrl = "http://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid="+z
    			+"&extra=page%3D1%26filter%3Dsortid%26sortid%3D165%26sortid%3D165";
    	//链接页面
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
        
        //判定日期 判定成功跳过这一条
        if(check(text)){
        	System.out.println("不合格页面"+z);
        	return;
        }
        

    	String ans1 = "";  
    	String ans2 = "";
    	String ans3 = "";
    	
    	//获得名称MIS@CMU-ebiz之类
    	ans1 += regularGroup2(rxz1,text);
    	//System.out.print(ans1);
    	ans1 = ans1.replaceAll(deleteHtml1,"");
    	ans1 += "下面放主贴\n\n\n";
    	//获取标题
    	ans3 += regularGroup2(rxz3,text);
    	ans3 = ans3.replaceAll(deleteHtml1,"");
    	ans3 += "\n";
    	
    	//获得正文	
    	ans2 += regularGroup1(rxz2,text);
    	ans2 = ans2.replaceAll(deleteHtml3, ""); 
    	ans2 = ans2.replaceAll(deleteHtml2, "");  
    	ans2 = ans2.replaceAll(replaceSpace," ");
    	
        //存储
    	File myFilePath = new File("E:/网页抓取/"+z+".txt");  
    	if (!myFilePath.exists()) {  
    	    myFilePath.createNewFile();  
    	}  
    	FileWriter resultFile = new FileWriter(myFilePath);  
    	PrintWriter myFile = new PrintWriter(resultFile);	
        myFile.println(ans3+ans1+ans2);
        
        resultFile.close();
        
        //多个页面处理
        int k = 0;    
        
        temp = regularGroup2(HaveAnoPage,text);
        
        k = GetPage(getNum,temp);
        
        //System.out.print(k);
        
        if(k!=0){
        	for(int i =1;i<k;i++)
            DoGetAns(z,i+1);
        }
    	//}
        } catch (IOException e) {
            // 如果出错 抛出异常
            e.printStackTrace();
        }
    }
    
    
  //多页面处理表达式函数
    public void DoGetAns(int z,int k){
        
    	try {
        	    	
            // 这里是指向第二种情况的页面
            String strUrl = "http://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid="+z
        			+"&extra=page%3D1%26filter%3Dsortid%26sortid%3D165&page="+k;
        	
            URL url = new URL(strUrl);
            // InputStreamReader 是一个输入流读取器 用于将读取的字节转换成字符
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();    
            con.setRequestProperty("User-Agent", "" ); 
         
            InputStream is = con.getInputStream(); 
            
            InputStreamReader isr = new InputStreamReader(is,"gbk"); // 统一使用utf-8 编码模式
           
            // 使用 BufferedReader 来读取 InputStreamReader 转换成的字符
            BufferedReader br = new BufferedReader(isr);
            String strRead = ""; // 新增一个空字符串strRead来装载 BufferedReader 读取到的内容

            StringBuffer content=new StringBuffer();  
            while((strRead = br.readLine())!=null){  
                content.append(strRead+"\n");
               }  
            String text=content.toString();  
            br.close();
            

//        	String ans1 = "";  
        	String ans2 = "";
        	
//        	//获得名称MIS@CMU-ebiz之类
//        	ans1 += regularGroup2(rxz1,text);
//        	ans1 = ans1.replaceAll(deleteHtml1,"");
        	
        	//获得正文 	
        	ans2 += regularGroup2(rxz2,text);
        	ans2 = ans2.replaceAll(deleteHtml3, ""); 
        	ans2 = ans2.replaceAll(deleteHtml2, "");  
        	ans2 = ans2.replaceAll(replaceSpace," ");
        	
            //存储
            
        	File myFilePath = new File("E:/网页抓取/"+z+"-"+k+".txt");  
        	if (!myFilePath.exists()) {  
        	    myFilePath.createNewFile();  
        	}  
        	FileWriter resultFile = new FileWriter(myFilePath);  
        	PrintWriter myFile = new PrintWriter(resultFile); 	
            myFile.println(ans2);
            resultFile.close();
            
        	
        } catch (Exception e) {
            // 如果出错 抛出异常
            e.printStackTrace();
        }
    }
    
    //为了将主贴和跟帖分开
    public String regularGroup1(String pattern, String matcher) {
        Pattern p = Pattern.compile(pattern,Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(matcher);
        String a = "";
        //第一条处理
        if (m.find()) { // 如果读到
        	a = a + m.group() + "\n"+"\n"+"\n"+"\n";
        	a += "下面是跟帖\n";
        } 
        while (m.find()) { // 如果读到 	
            a = a + m.group() + "\n";// 返回捕获的数据
        }       
        return a;
    }
    
    //常规处理正则表达式
    public String regularGroup2(String pattern, String matcher) {
        Pattern p = Pattern.compile(pattern,Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(matcher);
        String a = "";
        while (m.find()) { // 如果读到 	
            a = a + m.group() + "\n";// 返回捕获的数据
        }       
        return a;
    }
    
    //获取第一个数字的正则表达式处理;
    public int GetPage(String pattern, String matcher) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(matcher);
        int ans = 0;
        if (m.find()) { // 如果读到
        	ans = Integer.parseInt(m.group(0));
        } 
        
        return ans;
    }
    
    //获得数字 检验日期
    public void getNum(String date1, String date2){
    	//检验格式第一步
		Pattern pa = Pattern.compile(checkDate, Pattern.CASE_INSENSITIVE);
        Matcher ma = pa.matcher(date1);
        ma.find();
        ma.group();
        ma = pa.matcher(date2);
        ma.find();
        ma.group();
        
        //存储年月日
		try {
			this.dateFir = (Date)format.parseObject(date1);
			this.dateSec = (Date)format.parseObject(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(dateFir.after(dateSec))
		{
			setFalse();
		}

//    	int a[]={0,0,0};
    	
//        Pattern p = Pattern.compile(checkNum, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(date1);
//        
//        int i = 0;
//        while(m.find()){
//        	a[i] = Integer.parseInt(m.group());
//        	//System.out.print(a[i]);
//        	i++;	
//        } 
//        this.year1 = a[0];
//        this.month1 = a[1];
//        this.day1 = a[2];
        
//        m = p.matcher(date2);  
//        i = 0;
//        while(m.find()){
//        	a[i] = Integer.parseInt(m.group());
//        	i++;	
//        }
//        
//        this.year2 = a[0];
//        this.month2 = a[1];
//        this.day2 = a[2];
        
    }
    
    public void setFalse(){
    	this.pagecheck = false;
    }
    
    //检查日期函数
    public boolean check(String matcher){
    	try{
    	//这里获得源码中的日期 首先用<em>标记缩小范围
    	Pattern pattern = Pattern.compile(getDate, Pattern.CASE_INSENSITIVE);
        Matcher ma = pattern.matcher(matcher);	
        String ans = "";
        while(ma.find()){
        	ans += ma.group();
        }
    	Pattern pa = Pattern.compile(checkDate, Pattern.CASE_INSENSITIVE);
        ma = pa.matcher(ans); 
        //只要第一个
        if(ma.find()){
        	ans = ma.group();
        }
        Date dateThi = (Date)format.parseObject(ans);
        if(dateThi.after(dateSec)|dateThi.before(dateFir)){
        	return true;
        }
        //System.out.print(ans+"\n");
//        int a[]={0,0,0};
        
//        Pattern p = Pattern.compile(checkNum, Pattern.CASE_INSENSITIVE);
//        ma = p.matcher(ans);  
//        int i = 0;
//        while(ma.find()){
//        	a[i] = Integer.parseInt(ma.group());
//        	i++;	
//        }
//        
//        int year = a[0];
//        int month = a[1];
//        int day = a[2];
//        //这里因为只有年月日 好比较 就重复写 清楚些 不然我感觉有更好的逻辑，其实没差
//        if(year<this.year1 | year>this.year2){
//        	return true;
//        }
//        if(year == this.year1){
//        	if(month<this.month1){
//        		return true;
//        	}else if(month == this.month1){
//        		if(day<this.day1){
//        			return true;
//        		}
//        	}
//        }
//        	
//        if(year == this.year2){
//        	if(month>this.month2){
//        		return true;
//        	}else if(month == this.month2){
//        		if(day>this.day2){
//        			return true;
//        		}
//        	}
//        }	
        }catch (Exception e){
        	//System.out.println("error");
        	return true;
        }
    	
		return false;
	
    }

}
