package models;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Branch {

  private String id;
  private String name;
  private List<VehicleType> vehicleTypes;

  private RentalStrategy rentalStrategy;

  // Mapping between branch name and branch object for accessibility
  private static Map<String, Branch> branchNameToBranchMapping = new HashMap<String, Branch>();
  // Map to store different types of vehicles in this branch
  private static Map<VehicleType, Integer> vehicleTypeQuantityMap = new HashMap<VehicleType, Integer>();
  // Map to store vehicleType : List of vehicles sorted by start time, endTime
  private static Map<VehicleType, List<Vehicle>> vehicleTypeToVehicleMapping = new HashMap<VehicleType, List<Vehicle>>();
  // Map to store vehicleType: VehicleID of booked vehicles
  private static Map<VehicleType, Set<String>> vehicleTypeToBookedVehiclesMapping = new HashMap<VehicleType, Set<String>>();


  public Branch(String generateUniqueId, String branchName, List<VehicleType> vehicleTypes) {
    this.id = generateUniqueId;
    this.name = branchName;

    //generating a deep copy to maintain immutability
    List<VehicleType> vehicleTypeList = new ArrayList<VehicleType>();
    for(VehicleType vehicleType : vehicleTypes){
      vehicleTypeList.add(vehicleType);
      //initialise all vehicleTypes in the quantity map
      vehicleTypeQuantityMap.put(vehicleType, 0);
      vehicleTypeToVehicleMapping.put(vehicleType, new ArrayList<Vehicle>());
      vehicleTypeToBookedVehiclesMapping.put(vehicleType, new HashSet<String>());
    }

    this.vehicleTypes = vehicleTypeList;
    branchNameToBranchMapping.put(branchName, this);

    //default rental strategy
    this.rentalStrategy = new MinimumPricingStrategy();
  }

  //Constructor with support to change strategy
  public Branch(String generateUniqueId, String branchName, List<VehicleType> vehicleTypes, RentalStrategy strategy) {
    this.id = generateUniqueId;
    this.name = branchName;

    //generating a deep copy to maintain immutability
    List<VehicleType> vehicleTypeList = new ArrayList<VehicleType>();
    for(VehicleType vehicleType : vehicleTypes){
      vehicleTypeList.add(vehicleType);
      //initialise all vehicleTypes in the quantity map
      vehicleTypeQuantityMap.put(vehicleType, 0);
      vehicleTypeToVehicleMapping.put(vehicleType, new ArrayList<Vehicle>());
      vehicleTypeToBookedVehiclesMapping.put(vehicleType, new HashSet<String>());
    }

    this.vehicleTypes = vehicleTypeList;
    branchNameToBranchMapping.put(branchName, this);

    this.rentalStrategy = strategy;
  }

  public void printBranch(){
    System.out.println("ID="+this.id);
    System.out.println("NAME="+this.name);
    System.out.println("SUPPORTED VEHICLE TYPES="+this.vehicleTypes);
    System.out.println("VEHICLE TYPE TO QUANTITY ="+ vehicleTypeQuantityMap);
    System.out.println("VEHICLE TYPE TO VEHICLE LIST MAP ="+vehicleTypeToVehicleMapping);
    System.out.println("VEHICLE TYPE TO BOOKED VEHICLES LIST MAP ="+vehicleTypeToBookedVehiclesMapping);
  }

  public void addVehicleToBranch(Vehicle vehicle){
    List<Vehicle> sortedVehicles = vehicleTypeToVehicleMapping.get(vehicle.getVehicleType());
    sortedVehicles.add(vehicle);
    vehicleTypeToVehicleMapping.put(vehicle.getVehicleType(), sortedVehicles);
    updateQuantityOfVehicleType(vehicle.getVehicleType());
    printBranch();
  }

  public static Branch getBranchForBranchName(String branchName){
    if(!branchNameToBranchMapping.containsKey(branchName)){
      System.err.println("Sorry! Given branch name does not exist");
      return null;
    }
    return branchNameToBranchMapping.get(branchName);
  }

  public static void addBookedVehicleToList(VehicleType vehicleType, String vehicleId){
    if(!vehicleTypeToBookedVehiclesMapping.containsKey(vehicleType)){
      System.err.println("Sorry! This vehicleType = "+vehicleType+ " is not supported in current branch" );
      return;
    }
    vehicleTypeToBookedVehiclesMapping.get(vehicleType).add(vehicleId);
  }

  public static Integer getCountOfBookedVehiclesForVehicleType(VehicleType vehicleType){
    if(!vehicleTypeToBookedVehiclesMapping.containsKey(vehicleType)){
      System.err.println("Sorry! This vehicleType = "+vehicleType+ " is not supported in current branch" );
      return -1;
    }
    return vehicleTypeToBookedVehiclesMapping.get(vehicleType).size();
  }

  public static Integer getQuantityOfVehicleType(VehicleType vehicleType){
    if(!vehicleTypeQuantityMap.containsKey(vehicleType)){
      System.err.println("Sorry! This vehicleType = "+vehicleType+ " is not supported in current branch" );
      return -1;
    }
    return vehicleTypeQuantityMap.get(vehicleType);
  }

  public static boolean updateQuantityOfVehicleType(VehicleType vehicleType){
    if(!vehicleTypeQuantityMap.containsKey(vehicleType)){
      System.err.println("Sorry! This vehicleType = "+vehicleType+ " is not supported in current branch" );
      return false;
    }
    vehicleTypeQuantityMap.put(vehicleType, vehicleTypeQuantityMap.get(vehicleType)+1);
    return true;
  }

  public static List<Vehicle> getSortedVehicleListOfVehicleType(VehicleType vehicleType){
    if(!vehicleTypeToVehicleMapping.containsKey(vehicleType)){
      System.err.println("Sorry! This vehicleType = "+vehicleType+ " is not supported in current branch" );
      return null;
    }
    return vehicleTypeToVehicleMapping.get(vehicleType);
  }

  public static boolean checkVehicleTypeExists(VehicleType vehicleType){
    return vehicleTypeToVehicleMapping.containsKey(vehicleType);
  }

}
