package models;

/**
 * Implement this interface to create your own rental strategy
 * Default strategy: Minimum price
 */
public interface RentalStrategy {

  //This method will return
  // +ve if vehicle1 is better than vehicle2 based on strategy
  // -ve if vehicle2 is better than vehicle1
  // 0 if vehicle1 and vehicle2 are equal

  //example: For minimum price strategy, the doOperation will compare price of both vehicles and return appropriate result

  public int doOperation(Vehicle vehicle1, Vehicle vehicle2);

}
