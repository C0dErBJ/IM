package com.AlphaZ.entity;

/**
 * 封装分页时的页面信息的类
 *
 * @author qian_qi
 *         2012-10-15
 */
public class PageDTO {


    //单页显示的数据数
    private int pageSize = 10;
    //当前页码数
    private int pageNo = 1;
    //总条数
    private int rowCount = 0;
    //结果集
    protected Object resultList;


    public PageDTO() {
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public Object getResultList() {
        return resultList;
    }

    public void setResultList(Object resultList) {
        this.resultList = resultList;
    }
}
