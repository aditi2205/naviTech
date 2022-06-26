package models;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Getter
@RequiredArgsConstructor
public class Vehicle implements Comparable<Vehicle>{
  private String id;
  private VehicleType vehicleType;
  private double price;
  private VehicleBooking bookingDetails;


  public Vehicle(String generateUniqueId, VehicleType vehicleType, double price) {
    this.id = generateUniqueId;
    this.vehicleType = vehicleType;
    this.price = price;
    //initialising start and end time by 0 and 24 (24 hour clock format)
    this.bookingDetails = new VehicleBooking(0,24);
  }

  //copy constructor
  public Vehicle(Vehicle vehicle,int start, int end){
    this.id = vehicle.id;
    this.vehicleType = vehicle.vehicleType;
    this.price = vehicle.price;
    this.bookingDetails = new VehicleBooking(start, end);
  }

  /**
   * Sorting strategy to sort the vehicles by startTime, EndTime
   * Used to do binary search while finding vehicles
  // if current object is greater,then return 1
  // if current object is lesser,then return -1
  // if current object is equal to given vehicle,then return 0
   **/
  public int compareTo(Vehicle vehicle) {
    if(this.bookingDetails.getStart() > vehicle.bookingDetails.getStart()){
      return 1;
    }
    if(this.bookingDetails.getStart() < vehicle.bookingDetails.getStart()){
      return -1;
    }
    if(this.bookingDetails.getStart() == vehicle.bookingDetails.getStart()){
      if(this.bookingDetails.getEnd() > vehicle.bookingDetails.getEnd()){
        return 1;
      }
      if(this.bookingDetails.getEnd() < vehicle.bookingDetails.getEnd()){
        return -1;
      }
      return 0;
    }
    return 0;
  }

}
