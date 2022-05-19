import java.util.List;

public class OrdersApiResponseModel {
    private List<OrderModel> orders;
    private long total;
    private long totalToday;

    public OrdersApiResponseModel(List<OrderModel> orders, long total, long totalToday) {
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
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
}
