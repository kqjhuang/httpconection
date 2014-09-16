package fun4RegeX;

import java.util.Scanner;

import fun4RegeX.funcation;

public class Main {

	

    public static void main(String[] args) {

    	//用一个txt来存储
        // 首先用一个字符串 来装载网页链接
    	try{
    	Scanner in=new Scanner(System.in); 
//    	System.out.print("请输入要查找的页码\n");
//    	System.out.print("min:\n");
//    	int min = in.nextInt();
//    	System.out.print("max:\n");
//    	int max = in.nextInt();
//    	if(max<min){
//    		in.close();
//    		throw new Exception("页面数字错误");
//    	}
    	
    	System.out.println("请输入起始日期，请按照年-月-日格式，如2014-2-1");
    	String date1 = in.next();
    	System.out.println("请输入结束日期");
    	String date2 = in.next();
    	funcation A = new funcation();
    	A.getNum(date1,date2);
    	if(!A.pagecheck){
    		//System.out.print(404);
    		in.close();
    		throw new Exception("日期格式错误");
    	}
    	System.out.println("请输入线程数量,太大你电脑受不了");
    	
    	int x = in.nextInt();
    	doThread(x,date1,date2);
    	
//    	long start = System.currentTimeMillis();
//        
//        
//        long end = System.currentTimeMillis();
//        System.out.println("结束，用时"+(end-start));
    	in.close();
    	}catch (Exception  e) {
            // 如果出错 抛出异常
    		System.out.print(e);
            System.out.println("请输入正确格式");
            //return;
    	}
    	
    	}
    //开x个线程 每个线程处理(max-min)/10+1个页面
    public static void doThread(int x,String date1,String date2){
    	int max = 45;
    	int min = 1;
    	int y = (max-min)/x+1;
    	int temp = min;
    	for (;temp<max+1;)
    	{
    		temp = min+y;
    		if(temp > max){
    			temp = max+1;
    		}
    		getMainPage deal = new getMainPage(date1,date2,min,temp); 
    		new Thread(new Threads(deal)).start();
    		min = temp;
    	}    	
    	
    }
    
    
    
}

