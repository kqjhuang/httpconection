package fun4RegeX;

public class Threads implements Runnable{

	getMainPage thds;
	
	public	Threads(getMainPage A){
    	//让线程慢一点
    	
		this.thds = A;
	}


	@Override
	public void run() {
		
	   try{
	       
		   Thread.sleep(100);
	    
	   }
	   catch(InterruptedException e){
		   
	   }
	   try{
		thds.beforeFuncation();
		// TODO Auto-generated method stub
	   }catch(Exception e){
		   System.out.println(e.getMessage()+thds.pageNumMin+thds.pageNumMax);
	   }
	}
	
	

}
