package models;

import lombok.Getter;
import lombok.Setter;

/**
 * Type of users that will use the Rental Company System
 */
@Getter
@Setter
public class User {

  private String id;
  private UserType userType;

}
