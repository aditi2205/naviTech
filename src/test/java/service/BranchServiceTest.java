package service;

import models.Branch;
import models.RentalCompany;
import models.Vehicle;
import models.VehicleType;
import org.junit.Assert;
import org.junit.Test;
import util.VehicleBookingUtil;
import util.generator.BranchIdGenerator;
import util.generator.VehicleIdGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BranchServiceTest {

  RentalCompany rentalCompany = new RentalCompany(new ArrayList<Branch>());
  RentalCompanyService rentalCompanyService = new RentalCompanyService(rentalCompany);
  BranchIdGenerator branchIdGenerator = new BranchIdGenerator();
  VehicleIdGenerator vehicleIdGenerator = new VehicleIdGenerator();
  VehicleService vehicleService = new VehicleService(vehicleIdGenerator);
  VehicleBookingUtil vehicleBookingUtil = new VehicleBookingUtil();
  BranchService branchService = new BranchService(rentalCompanyService, branchIdGenerator, vehicleBookingUtil);

  /*
  TEST CASES::
  ADD_BRANCH B1 CAR,BIKE,VAN	TRUE
  ADD_VEHICLE B1 CAR V1 500	TRUE
  ADD_VEHICLE B1 CAR V2 1000	TRUE
  ADD_VEHICLE B1 BIKE V3 250	TRUE
  ADD_VEHICLE B1 BIKE V4 300	TRUE
  ADD_VEHICLE B1 BUS V5 2500	FALSE
  BOOK B1 VAN 1 5	-1
  BOOK B1 CAR 1 3	1000
  BOOK B1 BIKE 2 3	250
  BOOK B1 BIKE 2 5	900
  DISPLAY_VEHICLES B1 1 5	V2
   */
  @Test
  public void testAddBranchToCompany(){

    //Add a new branch to company
    boolean result = branchService.addBranch("Bellandur-Branch", Arrays.asList(VehicleType.BIKE, VehicleType.VAN, VehicleType.CAR));
    Assert.assertTrue(result);
    rentalCompanyService.showAllBranches();

    // Add a new vehicle to branch
    boolean result2 = vehicleService.addVehicle("Bellandur-Branch", VehicleType.CAR, 500D);
    Assert.assertTrue(result2);


    // Add a new vehicle to branch with unsupported vehicle type
    boolean result3 = vehicleService.addVehicle("Bellandur-Branch", VehicleType.BUS, 2500D);
    Assert.assertFalse(result3);

    //Add a new vehicle to non existing branch
    // Add a new vehicle to branch with unsupported vehicle type
    boolean result4 = vehicleService.addVehicle("fake-Branch", VehicleType.VAN, 100D);
    Assert.assertFalse(result4);


    //booking test
    boolean result5 = vehicleService.addVehicle("Bellandur-Branch", VehicleType.CAR, 1000D);
    Assert.assertTrue(result5);
    boolean result6 = vehicleService.addVehicle("Bellandur-Branch", VehicleType.BIKE, 250D);
    Assert.assertTrue(result6);
    boolean result7 = vehicleService.addVehicle("Bellandur-Branch", VehicleType.BIKE, 300D);
    Assert.assertTrue(result7);

    //BOOK B1 VAN 1 5	-1
    double result8 = branchService.bookVehicle("Bellandur-Branch", VehicleType.VAN, 1, 5);
    Assert.assertEquals(-1, result8, 0);

    //BOOK B1 CAR 1 3	1000
    double result9 = branchService.bookVehicle("Bellandur-Branch", VehicleType.CAR, 1, 3);
    Assert.assertEquals(1000D, result9, 0);

    //BOOK B1 BIKE 2 3	250
    double result10 = branchService.bookVehicle("Bellandur-Branch", VehicleType.BIKE, 2, 3);
    Assert.assertEquals(250D, result10, 0);

    //BOOK B1 BIKE 2 5	900
    double result11 = branchService.bookVehicle("Bellandur-Branch", VehicleType.BIKE, 2, 5);
    Assert.assertEquals(900D, result11, 0);

    //DISPLAY_VEHICLES B1 1 5	V2
    List<Vehicle> result12 = branchService.displayAllVehiclesAvailableInAnInterval("Bellandur-Branch", 1, 5);

    Assert.assertTrue(result12.size()==1);
    Assert.assertEquals("V2", result12.get(0).getId());

    //BOOK B1 BIKE 6 7  V3 at 275 (DEMO DYNAMIC PRICING)
    double result13 = branchService.bookVehicle("Bellandur-Branch", VehicleType.BIKE, 6, 7);
    Assert.assertEquals(275D, result13, 0);


  }


}
