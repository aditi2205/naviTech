package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VehicleBooking {

  //For simplicity, using start and end time as integers
  private int start;
  private int end;
}
