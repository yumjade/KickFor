package com.example.kickfor.pullableview;

/**
 * Created by ubuntu on 6/19/15.
 */
public interface Pullable {
	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ���ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullDown();

	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ���ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullUp();
}
