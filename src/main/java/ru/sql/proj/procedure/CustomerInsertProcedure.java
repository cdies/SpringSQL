package ru.sql.proj.procedure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.SqlReturnUpdateCount;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;
import ru.sql.proj.model.Order;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;

@Component
public class CustomerInsertProcedure extends StoredProcedure {


    @Autowired
    public CustomerInsertProcedure(DataSource ds) {
        super(ds, "dbo.testProcedureInsert");
        declareParameter(new SqlParameter("@name", Types.VARCHAR));
        declareParameter(new SqlParameter("@date", Types.DATE));
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
    public Collection<Order> execute(String customerName, Date date) {
        return (Collection<Order>) super.execute(customerName, date).get("Result");
    }
}
