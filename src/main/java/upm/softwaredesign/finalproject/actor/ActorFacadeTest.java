package upm.softwaredesign.finalproject.actor;

import upm.softwaredesign.finalproject.model.Product;
import upm.softwaredesign.finalproject.order.OrderManager;

public class ActorFacadeTest {

    public static void main(String[] args) {

        // simple example of usage of actor Facade
        ActorFacade actorFacade = new ActorFacade(new OrderManager());
        actorFacade.createNewRequest(new Product());

    }
}
