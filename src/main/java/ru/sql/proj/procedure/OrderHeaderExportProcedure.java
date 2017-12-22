package ru.sql.proj.procedure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;
import ru.sql.proj.model.Order;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Collection;

@Component
public class OrderHeaderExportProcedure extends StoredProcedure {

    @Autowired
    public OrderHeaderExportProcedure(DataSource ds) {
        super(ds, "dbo.testProcedure");
        declareParameter(new SqlParameter("@name", Types.VARCHAR));
        declareParameter(new SqlReturnResultSet("Result", (rs, row) -> {
            try {
                Order.Row order = new Order.Row(rs.getInt("id"));
                order.setId(rs.getLong("id"));
                order.setName(rs.getString("name"));
                order.setDate(rs.getDate("date"));

                return order;
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }));
    }

    @SuppressWarnings("unchecked")
    public Collection<Order.Row> execute(String author) {
        return (Collection<Order.Row>) super.execute(author).get("Result");
    }
}
