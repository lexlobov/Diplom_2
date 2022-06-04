package model;

import model.OrderModel;

import java.util.List;

public class OrdersApiResponseModel {
    private boolean success;
    private List<OrderModel> orders;
    private long total;
    private long totalToday;

    public OrdersApiResponseModel(boolean success, List<OrderModel> orders, long total, long totalToday) {
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
        this.success = success;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public long getTotal() {
        return total;
    }

    public long getTotalToday() {
        return totalToday;
    }

    public boolean isSuccess(){
        return success;
    }
}
