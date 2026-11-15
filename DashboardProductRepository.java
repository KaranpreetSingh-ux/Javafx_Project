package Starter_App.Starter_Ap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** JDBC queries used before calling user_Dashboard.setDashboardData(...). */
public final class DashboardProductRepository {
    private DashboardProductRepository() { }

    public static List<user_Dashboard.ProductRank> findMarketTopFive(Connection connection)
            throws SQLException {
        String sql = """
            SELECT p.product_id, p.product_name, p.image_data,
                   COALESCE(SUM(oi.quantity), 0) AS sales
            FROM products p
            LEFT JOIN order_items oi ON oi.product_id = p.product_id
            GROUP BY p.product_id, p.product_name, p.image_data
            ORDER BY sales DESC, p.product_name ASC
            LIMIT 5
            """;
        return queryProducts(connection, sql, null);
    }

    public static List<user_Dashboard.ProductRank> findRecommendations(Connection connection,
            long userId) throws SQLException {
        // Uses categories the customer bought before, and does not recommend bought products again.
        String sql = """
            SELECT p.product_id, p.product_name, p.image_data,
                   COALESCE(SUM(all_items.quantity), 0) AS sales
            FROM products p
            LEFT JOIN order_items all_items ON all_items.product_id = p.product_id
            WHERE EXISTS (
                SELECT 1
                FROM products bought_product
                JOIN order_items bought_item ON bought_item.product_id = bought_product.product_id
                JOIN orders bought_order ON bought_order.order_id = bought_item.order_id
                WHERE bought_order.user_id = ?
                  AND bought_product.category = p.category
            )
              AND NOT EXISTS (
                SELECT 1 FROM order_items previous_item
                JOIN orders previous_order ON previous_order.order_id = previous_item.order_id
                WHERE previous_order.user_id = ?
                  AND previous_item.product_id = p.product_id
              )
            GROUP BY p.product_id, p.product_name, p.image_data
            ORDER BY sales DESC, p.product_name ASC
            LIMIT 5
            """;
        return queryProducts(connection, sql, userId);
    }

    private static List<user_Dashboard.ProductRank> queryProducts(Connection connection,
            String sql, Long userId) throws SQLException {
        List<user_Dashboard.ProductRank> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (userId != null) {
                statement.setLong(1, userId);
                statement.setLong(2, userId);
            }
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    products.add(new user_Dashboard.ProductRank(
                            results.getLong("product_id"),
                            results.getString("product_name"),
                            results.getInt("sales"),
                            results.getBytes("image_data")));
                }
            }
        }
        return products;
    }
}
