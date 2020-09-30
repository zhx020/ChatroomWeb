package com.su.base.po;

import java.util.List;

public class Page<T>{
	 /**
	     *  其中currentPage,perPageRows这两个参数是做分页查询必须具备的参数
	     *  原因是：hibernate中的Criteria或则是Query这两个接口：都有setFirstResult(int firstResult)
	     *  和setMaxResult(int maxResult),
	     *  这里的firstResult就是每页的开始的索引数：
	     *  每页开始的索引数的计算公式是：（currentPage-1）*perPageRows+1,(这是相对索引从1开始的)
	     *  但是Hibernate中的firstResult的索引是从0开始的，所以在hibernate中每页开始的索引数的计算公式是：
     *  (currentPage-1)*perPageRows+1-1=(currentPge-1)*perPageRows.
     *
     *  maxResult就是每页能查询的最大记录数：也就是perPageRows.
     *
     *  Math.ceil(totalRows/perPageRows)==totalPages;//这是根据总记录数和每页的记录数算出总页数的计算公式。
     */
    private int currentPage = 1;//当前页
    private int perPageRows = 20;//每页的记录数
    private int totalRows;//总记录数：
    private int totalPages;//总页数：
    private List<T> datas;
    private boolean hasNext;
    private boolean hasPre;
    public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPerPageRows() {
        return perPageRows;
    }

    public void setPerPageRows( int perPageRows) {
        this.perPageRows = perPageRows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows( int totalRows) {
        this.totalRows = totalRows;
        caculat();
    }

    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
    
    public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasPre() {
		return hasPre;
	}

	public void setHasPre(boolean hasPre) {
		this.hasPre = hasPre;
	}

	public void caculat() {
    	totalPages = (totalRows / perPageRows) + (totalRows % perPageRows > 0 ? 1 : 0);
    	hasNext = totalPages > currentPage;
    	hasPre = currentPage > 1; 
    }
}
