package com.collectinfo.dto.query;

import java.util.List;

public class PageResult<T> {

  private List<T> list;
  private long total;
  private PageInfo page;
  private long totalPage;

  public PageResult() {

  }

  public PageResult(List<T> list, long total, PageInfo page) {
    this.list = list;
    this.total = total;
    this.page = page;
    if (total > 0) {
      this.totalPage = total / page.getPageSize();
      if (total % page.getPageSize() > 0) {
        this.totalPage++;
      }
    }
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public PageInfo getPage() {
    return page;
  }

  public void setPage(PageInfo page) {
    this.page = page;
  }

  public long getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(long totalPage) {
    this.totalPage = totalPage;
  }

}
