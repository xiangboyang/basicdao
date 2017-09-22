package org.fkjava.callback;


/**
 * 接口中有一个方法，方法的作用是处理数组对象
 * */
interface Callback {
	void doInCallback(int[] array) ;	
}

/**
 * Template Method，模板方法：
 * 定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，
 * TemplateMethod使得子类可以不改变一个算法的结构即可以重定义该算法得某些特定步骤。
 * */
public class ArrayUtil {
	/**
	 * 处理数组的方法
	 * @parama int array 
	 * @param Callback callback 接口,用来处理整形数组的
	 * process 布置作业(要作什么,怎么做没有确定)
 	 * */
	public void process(int[] array,Callback callback){
		callback.doInCallback(array);
	}
	
	public static void main(String[] args) {
		int[] array = {1,2,3,4,5};
		ArrayUtil util = new ArrayUtil();
		// A
		util.process(array, new Callback() {
			
			@Override
			public void doInCallback(int[] array) {
				for(int t : array){
					System.out.print(t + "\t");
				}
				
			}
		});
		System.out.println();
		// B
		util.process(array, new Callback() {
			
			@Override
			public void doInCallback(int[] array) {
				int sum = 0;
				for(int t : array){
					sum += t;
				}
				System.out.println(sum);
			}
		});
	}


}
