package css.pizzzaorder;


import android.app.Application;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PizzaRepository {
    public static final Double DELIVERY_PRICE = 2.50;
    private PizzaDao heartrateDao;
    private List<Pizza> pizzasInOrder;          // list of pizzas ordered so far
    private boolean delivery=false;             // true if customer wants order delivered

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    PizzaRepository(Application application) {
        PizzaDatabase db = PizzaDatabase.getDatabase(application);
        Log.d("CIS 3334", "PizzaRepository Setting up the Dao and  database");
        heartrateDao = db.pizzaDao();
        Log.d("CIS 3334", "PizzaRepository Dao created");
        pizzasInOrder = new ArrayList<Pizza>();
        //pizzasInOrder = heartrateDao.getAll();
        //Log.d("CIS 3334", "PizzaRepository pizzas retreived from Dao, size = " + pizzasInOrder.size());

    }


    /***
     *  Constructor initializes the Arraylist holding the pizzas in the order

    public PizzaRepository( ) {
        pizzasInOrder = new ArrayList<Pizza>();
    }
     */

    /***
     * Add an pizza to the order
     * @param topping - A string listing all the toppings on the pizza
     * @param size - The size of the pizza as an integer -- see Pizza class for details
     * @return the description of the pizza is returned if needed
     */
    public String OrderPizza(Pizza newPizza){
        Log.d("CIS 3334", "PizzaRepository OrderPizza creating pizzza");
        //Pizza newPizza = new Pizza(topping, size);
        Log.d ("CIS 3334", newPizza.toString());
        // ROOM -- Add insert into database
        PizzaDatabase.databaseWriteExecutor.execute(() -> {
            Log.d("CIS 3334", "PizzaRepository - OrderPizza:databaseWriteExecutor - starting up heartrateDao");
            heartrateDao.insert(newPizza);
            Log.d("CIS 3334", "PizzaRepository - OrderPizza:databaseWriteExecutor -Inserting pizza into database");
            //Log.d ("CIS 3334", newPizza.toString());
        });
        pizzasInOrder.add(newPizza);
        return newPizza.toString();             // return a description of what was ordered
    }


    /***
     * Retrieve the full order
     * @return a list of pizzas, each one describe by a single string
     */
    public List<String> getOrder() {
        PizzaDatabase.databaseWriteExecutor.execute(() -> {
            Log.d("CIS 3334", "PizzaRepository - getOrder:databaseWriteExecutor - starting up");
            pizzasInOrder = heartrateDao.getAll();              // ROOM - add this
            Log.d("CIS 3334", "PizzaRepository - getOrder:databaseWriteExecutor - got order");
            //Log.d ("CIS 3334", newPizza.toString());
        });
        Log.d("CIS 3334", "PizzaRepository - getOrder: pizzaOrder size = "+pizzasInOrder.size());

        ArrayList<String> strOrder = new ArrayList<String>();
        for (Pizza p:pizzasInOrder ){
            strOrder.add(p.toString());
        }
        return strOrder;
    }

    /***
     * Calculate the total bill for this order
     * @return total bill as Double
     */
    public Double getTotalBill() {
        Double total = 0.0;
        for (Pizza p:pizzasInOrder ){
            total += p.getPrice();
        }
        if (delivery) {
            total += DELIVERY_PRICE;
        }
        return total;
    }

    public void setDelivery(boolean deliver) {
        this.delivery = deliver;
    }

    public boolean getDelivery() {
        return delivery;
    }
}
