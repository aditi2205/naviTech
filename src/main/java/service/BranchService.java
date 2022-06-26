package service;

import lombok.AllArgsConstructor;
import models.Branch;
import models.Vehicle;
import models.VehicleType;
import util.RentalServiceConstants;
import util.VehicleBookingUtil;
import util.Verify;
import util.generator.BranchIdGenerator;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BranchService {

  private RentalCompanyService rentalCompanyService;
  private BranchIdGenerator idGenerator;
  private VehicleBookingUtil vehicleBookingUtil;

  /**
  Adds a new branch into the system
   **/
  public boolean addBranch(String branchName , List<VehicleType> vehicleTypes){
    System.out.println("==========================================================================================");
    System.out.println("Add a new branch in the rental company "+ branchName);
    //Create a new Branch and add it to the rental company
    try {
      Branch branch = new Branch(idGenerator.generateUniqueId(), branchName, vehicleTypes);
      System.out.println("Adding Branch "+branch+" to the rental Company...");
      rentalCompanyService.addBranchToCompany(branch);
      return true;
    }catch(Exception e){
      System.err.println("Error occurred while adding branch");
      return false;
    }
  }


  /**
  book a vehicle - (Branch id, vehicle type, start time, end time)
  Return Booking Price, if booking succeeds else -1
   **/
  public double bookVehicle(String branchName, VehicleType vehicleType, int start, int end){

    //Booking criteria:
    // 1) Given branchName should exist
    // 2) Given vehicleType should exist in the given branch
    // 3) The quantity of this vehicleType should be greater than 0
    // 4) The returned vehicle should be available between the given start and end
    // 5) Price of the returned vehicle should be minimum

    // Other things to do:
    // 1) Update the quantity of vehicleType in case of successful booking
    // 2) Update the start and end time of the returned vehicle
    // 3) Update the list sorted with start and endTime

    //Finding vehicle criteria:
    // 1) Find the vehicles by doing a binary search
    // 2) Choose the one with minimum price from that list (Sort using the strategy)
    // 3) Break the interval of the vehicle selected:
    // V1 (0-24) is booked from 1-3 then Make two entries V1(0-1) and V1(3-24) and add them to the list

    System.out.println("==========================================================================================");
    System.out.println("Book a vehicle for branchname "+branchName+" vehicleType "+vehicleType+" between interval "+ start+","+end);

    try{
      Branch branch = Branch.getBranchForBranchName(branchName);
      Verify.verifyNull(branch, "No branch found with such name "+ branchName);

      Verify.verifyCondition(!Branch.checkVehicleTypeExists(vehicleType), "No vehicle type="+vehicleType+" exists in the given branch");

      List<Vehicle> sortedVehiclesForVehicleType = Branch.getSortedVehicleListOfVehicleType(vehicleType);
      Verify.verifyCondition((null == sortedVehiclesForVehicleType) || sortedVehiclesForVehicleType.size()==0, "No vehicles available in the given branch for given vehicle type "+ vehicleType);

      List<Vehicle> vehiclesInGivenInterval = vehicleBookingUtil.binarySearchVehiclesBasedOnInterval(sortedVehiclesForVehicleType, start, end);

      Verify.verifyCondition(vehiclesInGivenInterval.size()==0, "No vehicles found in the given interval in this branch and vehicleType");

      Vehicle bookedVehicle = vehicleBookingUtil.getVehicleBasedOnRentalStrategy(vehiclesInGivenInterval, branch);

      System.out.println("BOOKED VEHICLE "+bookedVehicle);

      vehicleBookingUtil.updateTreeSetForBookedVehicle(sortedVehiclesForVehicleType, bookedVehicle, start, end);

      double totalBookingPrice = getBookingPrice(bookedVehicle, vehicleType, start, end);
      System.out.println("FINAL BOOKING PRICE "+ totalBookingPrice+" SELECTED VEHICLE ID "+ bookedVehicle.getId());

      Branch.addBookedVehicleToList(vehicleType, bookedVehicle.getId());

      branch.printBranch();

      return totalBookingPrice;

    } catch (Exception e) {
      System.err.println("Error occurred while trying to book a vehicle");
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * Implements dynamic pricing (BONUS QUESTION)
   *  Return (price)*(end-start) if the number of booked vehicles is less than 80%
   *  Return (price)*(end-start) + 10% if the number of booked vehicles is more than or equal to 80%
   */
  private double getBookingPrice(Vehicle bookedVehicle, VehicleType vehicleType, int start, int end){
    double totalBookingPrice = bookedVehicle.getPrice() * (end-start);
    int totalVehiclesOfVehicleType = Branch.getQuantityOfVehicleType(vehicleType);
    int totalBookedVehiclesForVehicleType = Branch.getCountOfBookedVehiclesForVehicleType(vehicleType);

    if((double)(totalBookedVehiclesForVehicleType/totalVehiclesOfVehicleType) >= RentalServiceConstants.DYNAMIC_PRICING_THRESHOLD){
      //increase price by 10%
      System.out.println("Dynamic pricing is applicable, prices increased by 10%");
      totalBookingPrice += (totalBookingPrice*0.1);
    }

    return totalBookingPrice;
  }


  /**
   *
   * Display all the vehicles for given branch available between the given interval
   */
  public List<Vehicle> displayAllVehiclesAvailableInAnInterval(String branchName, int start, int end){
    System.out.println("==========================================================================================");
    System.out.println("Display all vehicles for Branch "+ branchName+" Between interval "+ start+","+end);
    List<Vehicle> resultSet = new ArrayList<Vehicle>();
    try {
      Branch branch = Branch.getBranchForBranchName(branchName);
      Verify.verifyNull(branch, "No branch found with such name " + branchName);

      for(VehicleType vehicleType : branch.getVehicleTypes()){
        List<Vehicle> sortedVehiclesForVehicleType = Branch.getSortedVehicleListOfVehicleType(vehicleType);
        if(sortedVehiclesForVehicleType == null || sortedVehiclesForVehicleType.size() == 0)
          continue;
        List<Vehicle> vehiclesInGivenInterval = vehicleBookingUtil.binarySearchVehiclesBasedOnInterval(sortedVehiclesForVehicleType, start, end);
        resultSet.addAll(vehiclesInGivenInterval);
      }
      System.out.println("Available vehicles in the provided interval "+ resultSet);
      return resultSet;
    }catch(Exception e){
      System.err.println("Exception while trying to display vehicles");
      e.printStackTrace();
      return null;
    }
  }

}
