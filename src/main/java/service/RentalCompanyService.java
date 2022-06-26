package service;

import lombok.AllArgsConstructor;
import models.Branch;
import models.RentalCompany;


@AllArgsConstructor
public class RentalCompanyService {

  private RentalCompany rentalCompany;

  public void addBranchToCompany(Branch newBranch){
    try{
      rentalCompany.getBranches().add(newBranch);
    }catch(Exception e){
      System.err.println("Exception while to trying add branch to the company "+ e.getStackTrace());
    }
  }

  public void showAllBranches(){
    System.out.println(rentalCompany.getBranches());
  }
}
