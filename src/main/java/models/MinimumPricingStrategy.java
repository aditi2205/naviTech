package models;

public class MinimumPricingStrategy implements RentalStrategy{


  public int doOperation(Vehicle vehicle1, Vehicle vehicle2) {
    return (int)(vehicle1.getPrice() - vehicle2.getPrice());
  }
}
