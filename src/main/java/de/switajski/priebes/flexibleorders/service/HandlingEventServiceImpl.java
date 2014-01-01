package de.switajski.priebes.flexibleorders.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.PayedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.repository.HandlingEventRepository;

@Service
public class HandlingEventServiceImpl implements HandlingEventService{

	private HandlingEventRepository heRepo;

	@Autowired
	public HandlingEventServiceImpl(HandlingEventRepository heRepo) {
		this.heRepo = heRepo;
	}
	
	@Override
	public HandlingEvent confirm(Item orderItemToConfirm, int quantity, 
			ConfirmedSpecification confirmedSpec) {
		if (confirmedSpec.isSatisfiedBy(orderItemToConfirm)){
			HandlingEvent he = new HandlingEvent(HandlingEventType.ORDERCONFIRM, orderItemToConfirm.getDeliveryHistory(), quantity, new Date());
			he.setConfirmedSpec(confirmedSpec);
			return heRepo.save(he);
		}
		return null;
	}

	@Override
	public HandlingEvent cancel(Item item) {
		HandlingEvent he = new HandlingEvent(HandlingEventType.CANCEL, item.getDeliveryHistory(), item.getQuantity(), new Date());
		return heRepo.save(he);
	}

	@Override
	public HandlingEvent deconfirm(Item shippingItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public HandlingEvent deliver(Item shippingItemToDeliver, int quantity,
			ShippedSpecification shippingSpec) {
		if (shippingSpec.isSatisfiedBy(shippingItemToDeliver)){
			HandlingEvent he = new HandlingEvent(HandlingEventType.SHIP, shippingItemToDeliver.getDeliveryHistory(), quantity, new Date());
			return heRepo.save(he);
		}
		return null;
	}

	@Override
	public HandlingEvent withdraw(Item invoiceItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public HandlingEvent complete(Item invoiceItem, int quantity,
			PayedSpecification accountSpec) {
		if (accountSpec.isSatisfiedBy(invoiceItem)){
			HandlingEvent he = new HandlingEvent(HandlingEventType.PAY, invoiceItem.getDeliveryHistory(), quantity, new Date());
			return heRepo.save(he);
		}
		return null;
	}

	@Override
	public HandlingEvent decomplete(Item archiveItem) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

}
