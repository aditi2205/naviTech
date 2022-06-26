package util;

import models.Branch;
import models.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VehicleBookingUtil {


  /**
   *  Returns the list of all the vehicles (type: vehicleType) that are available in the interval (start, end)
   *  Logic:
   *  1) Get the sorted list of vehicles (sorted on startTime, endTime in increasing order)
   *  2) Do a binary search on this list to find the vehicle index that overlaps the given interval
   *  3) Traverse left from this index and find any other vehicle that overlaps with this interval
   *  4) Traverse right from this index and find any other vehicle that overlaps with this interval
   *  5) Add all these vehicles in a resultSet and return
   */
  public List<Vehicle> binarySearchVehiclesBasedOnInterval(List<Vehicle> vehicles, int start, int end){
    Collections.sort(vehicles);
    Vehicle[] sortedVehicles = vehicles.toArray(new Vehicle[0]);
    List<Vehicle> resultSet = new ArrayList<Vehicle>();
    int low = 0;
    int high = vehicles.size();
    int index = -1;

    while(low < high){
      int mid = (low+high)/2;
      Vehicle midVehicle = sortedVehicles[mid];

      if(midVehicle.getBookingDetails().getStart() <= start){
        if(midVehicle.getBookingDetails().getEnd() >= end){
          index = mid;
          resultSet.add(midVehicle);
          break;
        }
        else if(midVehicle.getBookingDetails().getEnd() < end){
          low = mid+1;
        }

      }
      else if(start < midVehicle.getBookingDetails().getStart()){
        high = mid-1;
      }
    }

    // No vehicle found within the given interval
    if(index == -1){
      System.err.println("Sorry! No vehicle found within the given time interval "+ start+","+end+" And vehicleType "+ vehicles.get(0).getVehicleType());
      return resultSet;
    }

    //If vehicle is found, traverse left and right
    int leftIndex = index-1;
    while(leftIndex >= 0){
      if(sortedVehicles[leftIndex].getBookingDetails().getStart()<=start && sortedVehicles[leftIndex].getBookingDetails().getEnd()>=end){
        resultSet.add(sortedVehicles[leftIndex]);
      }
      leftIndex--;
    }

    int rightIndex = index+1;
    while(rightIndex < sortedVehicles.length){
      if(sortedVehicles[rightIndex].getBookingDetails().getStart()<=start && sortedVehicles[rightIndex].getBookingDetails().getEnd()>=end){
        resultSet.add(sortedVehicles[rightIndex]);
      }
      rightIndex++;
    }

    return resultSet;
  }


  /**
   * Sorts the vehicles selected based on rental strategy (default : minimum pricing)
   */
  public Vehicle getVehicleBasedOnRentalStrategy(List<Vehicle> vehicles, final Branch branch){
    Collections.sort(vehicles, new Comparator<Vehicle>() {
      public int compare(Vehicle v1, Vehicle v2) {
        return branch.getRentalStrategy().doOperation(v1, v2);
      }
    });
    return vehicles.get(0);
  }

  /**
   * Divides the interval based on booking provided
   * Consider the interval in which vehicle is available is (S1, E1) and the booking interval is (S2, E2)
   * There can be 4 scenarios:
   * 1) S1=S2 and E1=E2 (Equal interval) : Just remove the interval from the list
   * 2) S1<S2 and E1>E2 (Complete overlap): Update the interval list from (S1,S2) and (E2, E1)
   * 3) S1<S2 and E1=E2 (Partial overlap): Update the interval list to have (S1,S2)
   * 4) S1=S2 and E1>E2 (Partial overlap): Update the interval list to have (E2,E1)
   */
  public void updateTreeSetForBookedVehicle(List<Vehicle> sortedVehicles, Vehicle chosenVehicle, int start, int end){
    int currentStart = chosenVehicle.getBookingDetails().getStart();
    int currentEnd = chosenVehicle.getBookingDetails().getEnd();

    sortedVehicles.remove(chosenVehicle);

    if(currentStart == start && currentEnd == end){
      return;
    }

    if(currentStart < start && currentEnd > end){
      //break the interval and add it back to treeSet
      Vehicle v1 = new Vehicle(chosenVehicle, currentStart, start);
      Vehicle v2 = new Vehicle(chosenVehicle, end, currentEnd);
      sortedVehicles.add(v1);
      sortedVehicles.add(v2);
      return;
    }

    if(currentStart < start && currentEnd == end){
      Vehicle v1 = new Vehicle(chosenVehicle, currentStart, start);
      sortedVehicles.add(v1);
      return;
    }

    if(currentStart == start && currentEnd > end){
      Vehicle v2 = new Vehicle(chosenVehicle, end, currentEnd);
      sortedVehicles.add(v2);
      return;
    }
  }
}
