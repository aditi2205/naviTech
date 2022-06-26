package service;

import lombok.AllArgsConstructor;
import models.Branch;
import models.Vehicle;
import models.VehicleType;
import util.Verify;
import util.generator.VehicleIdGenerator;

@AllArgsConstructor
public class VehicleService {

  private VehicleIdGenerator vehicleIdGenerator;

  public boolean addVehicle(String branchName, VehicleType vehicleType, double price){
    //Create a vehicle (start and end time)
    //Add the vehicle to branch
    //update the quantity map
    System.out.println("==========================================================================================");
    System.out.println("Add a new vehicle in the branch "+ branchName);
    try {
      Branch branch = Branch.getBranchForBranchName(branchName);
      Verify.verifyNull(branch, "No branch found with the given name "+ branchName);
      if(!branch.getVehicleTypes().contains(vehicleType)){
        System.err.println("Given vehicle type "+ vehicleType+" is not supported in current branch "+branchName);
        return false;
      }
      Vehicle vehicle = new Vehicle(vehicleIdGenerator.generateUniqueId(), vehicleType, price);
      branch.addVehicleToBranch(vehicle);
      System.out.println("Added vehicle "+ vehicle.getId()+" to Branch "+branch.getId()+" "+branchName);
      return true;
    }catch(Exception e){
      System.err.println("Error while trying to add a new Vehicle "+ e.getStackTrace());
      return false;
    }
  }


}
