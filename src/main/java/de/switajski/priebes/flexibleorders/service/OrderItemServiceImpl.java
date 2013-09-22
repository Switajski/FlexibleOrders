package de.switajski.priebes.flexibleorders.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;


@Service
@Transactional
public class OrderItemServiceImpl extends JpaRepositoryToServiceAdapter<OrderItem> implements OrderItemService {

	OrderItemRepository oir;

	@Autowired
	public OrderItemServiceImpl(OrderItemRepository jpaRepository) {
		super(jpaRepository);
		this.oir = jpaRepository;
	}

	/**part of Spring Roo
	 * @deprecated
	 */
	public long countAllOrderItems() {
		return oir.count();
	}
	
	/**part of Spring Roo
	 * @deprecated
	 */
	public void deleteOrderItem(OrderItem orderItem) {
		oir.delete(orderItem);
	}

	/**part of Spring Roo
	 * @deprecated
	 */
	public OrderItem findOrderItem(Long id) {
		return oir.findOne(id);
	}

	/**part of Spring Roo
	 * @deprecated
	 */
	public List<OrderItem> findAllOrderItems() {
		return oir.findAll();
	}

	/**part of Spring Roo
	 * @deprecated
	 */
	public List<OrderItem> findOrderItemEntries(int firstResult, int maxResults) {
		return oir.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
	}

	/**part of Spring Roo
	 * @deprecated
	 */
	public void saveOrderItem(OrderItem orderItem) {
		oir.save(orderItem);
	}

	/**part of Spring Roo
	 * @deprecated
	 */
	public OrderItem updateOrderItem(OrderItem orderItem) {
		return oir.save(orderItem);
	}

	@Override
	public List<OrderItem> findByOrderNumber(Long orderNumber) {
		return oir.findByOrderNumber(orderNumber);
	}

	@Override
	public Page<OrderItem> findByOrderNumber(Long orderNumber, Pageable pageable) {
		return oir.findByOrderNumber(orderNumber, pageable);
	}

	@Override
	public Page<OrderItem> findAll(Pageable pageable) {
		return oir.findAll(pageable);
	}

	@Override
	public Page<OrderItem> findOpen(Pageable pageable){
		return oir.findOpen(pageable);
	}

	@Override
	public List<OrderItem> findOpen() {
		return oir.findOpen();
	}

}
