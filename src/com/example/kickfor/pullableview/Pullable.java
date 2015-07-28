package com.example.kickfor.pullableview;

/**
 * Created by ubuntu on 6/19/15.
 */
public interface Pullable {
    /**
     * 判断是否可以下拉，如果不�?要下拉功能可以直接return false
     *
     * @return true如果可以下拉否则返回false
     */
    boolean canPullDown();

    /**
     * 判断是否可以上拉，如果不�?要上拉功能可以直接return false
     *
     * @return true如果可以上拉否则返回false
     */
    boolean canPullUp();
}
