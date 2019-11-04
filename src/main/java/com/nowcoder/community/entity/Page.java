package com.nowcoder.community.entity;

/**
 * @author xuming
 * @Date 2019/11/4 11:14
 * 封装分页相关信息
 */
public class Page {
    /** 当前页码,默认为1 */
    private int current = 1;
    /** 每页显示上限,默认为10 */
    private int limit = 10;
    /** 数据总数(用于计算总页数) */
    private int rows;
    /** 查询路径(用于复用分页链接) */
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset() {
        return current * limit - limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取底栏起始页码(5页)
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /** 底栏显示5页.若总页数为100,当前页是3,则起始页为1,结束页为5. */

    /**
     * 获取底栏结束页码(5页)
     * @return
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
