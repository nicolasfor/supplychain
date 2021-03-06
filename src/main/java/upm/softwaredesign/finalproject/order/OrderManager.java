package upm.softwaredesign.finalproject.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


import upm.softwaredesign.finalproject.blockchain.BlockChainFactory;
import upm.softwaredesign.finalproject.enums.TransactionStatus;
import upm.softwaredesign.finalproject.model.Actor;
import upm.softwaredesign.finalproject.model.Factory;
import upm.softwaredesign.finalproject.model.Producer;
import upm.softwaredesign.finalproject.model.Product;
import upm.softwaredesign.finalproject.model.Retailer;

public class OrderManager {

    private final static OrderManager instance = new OrderManager();

    public static OrderManager getInstance(){
        return instance;
    }

    /*
    @return arraylist of orders that are in the BlockChain
     */
    public ArrayList<Order> consultChain(){
        return BlockChainFactory.build().listOrders();
    }

    /* Saves a request order in the BlockChain
    @param sender
    @param product
    @param time
    @param transactionGroupId: id  that links a maximum of 4 orders
     */
    public void saveRequest(Actor sender, Product product, Date time, UUID transactionGroupId){
    	Request request = new Request(sender, product, time);
		request.setTransactionGroupId(transactionGroupId);
		BlockChainFactory.build().addOrder(request);
    }

	/* Saves a delivery order in the BlockChain
   @param sender
   @param product
   @param time
   @param transactionGroupId: id  that links a maximum of 4 orders
    */
	public void saveDelivery(Actor sender, Product product, Date time, UUID transactionGroupId){
		Delivery delivery = new Delivery(sender, product, time);
		delivery.setTransactionGroupId(transactionGroupId);
		BlockChainFactory.build().addOrder(delivery);
	}

    /* checks the status of a transaction:
        - Retailer Request
        - Factory Request/Delivery
        - Producer Delivery
    @param transactionGroupId: id  that links a maximum of 4 orders
    @return the status of the transaction
     */
    public TransactionStatus status(UUID transactionGroupId){
    	ArrayList<Order> linkedOrder = new ArrayList<Order>();
    	TransactionStatus status = null;
    	for(Order order:BlockChainFactory.build().listOrders()){
    		if(order.getTransactionGroupId() == transactionGroupId){
    			linkedOrder.add(order);
    		}
    	}
    	
    	if(!linkedOrder.isEmpty()){
    		// Initial status if there are linked orders
    		status = TransactionStatus.RETAILER_REQUEST; 
    		for(Order order:linkedOrder){
				// This sequence of if statements are important
				// Because we check from the reverse order so that we get the latest status
				// FACTORY_DELIVERY <- PRODUCER_DELIVERY <- FACTORY_REQUEST <- RETAILER_REQUEST
				if(order instanceof Delivery 
						&& order.getSender() instanceof Factory  // Hence Factory Delivery
						&& status.getSequenceIndex() < TransactionStatus.FACTORY_DELIVERY.getSequenceIndex()){ 
					
					status = TransactionStatus.FACTORY_DELIVERY;
					
				} else if(order instanceof Delivery 
						&& order.getSender() instanceof Producer 
						&& status.getSequenceIndex() < TransactionStatus.PRODUCER_DELIVERY.getSequenceIndex()){
					
					status = TransactionStatus.PRODUCER_DELIVERY;
					break;
					
				} else if(order instanceof Request 
						&& order.getSender() instanceof Factory
						&& status.getSequenceIndex() < TransactionStatus.FACTORY_REQUEST.getSequenceIndex()){
					
					status = TransactionStatus.FACTORY_REQUEST;
					
					
				} else if(order instanceof Request 
						&& order.getSender() instanceof Retailer
						&& status.getSequenceIndex() < TransactionStatus.RETAILER_REQUEST.getSequenceIndex()){
					
					status = TransactionStatus.RETAILER_REQUEST;
					
				}
    		}
    	}
    	return status;
    }
}
