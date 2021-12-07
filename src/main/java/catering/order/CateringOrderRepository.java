package catering.order;

import org.salespointframework.order.OrderIdentifier;

import org.springframework.data.repository.Repository;
import org.springframework.data.util.Streamable;

import java.time.LocalDateTime;


public interface CateringOrderRepository extends Repository<CateringOrder, OrderIdentifier> {

	Streamable<CateringOrder> findByCompletionDate(LocalDateTime completionDate);

	Streamable<CateringOrder> findByAddress(String address);
}
