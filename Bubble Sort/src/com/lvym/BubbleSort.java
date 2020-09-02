package com.lvym;

public class BubbleSort {
	public static void main(String[] args) {
	
		int [] sort= {1,2,6,4,8,3,2};
		
		for(int i=0;i<sort.length;i++) {
			for(int c=0;c<sort.length-i-1;c++) {
				if(sort[c]>sort[c+1]) {
					int temp=sort[c];
					sort[c]=sort[c+1];
					sort[c+1]=temp;
				}
				
			}
			
		}
		System.out.println();
		System.out.println("最终得出的数组为：");
      for (int k =0 ; k < sort.length;k++){
          System.out.print(sort[k]+" ");
     }
	}

}
