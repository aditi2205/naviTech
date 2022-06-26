package models;

/**
 * ADMIN will be able to use all the APIs
 * CUSTOMER can only book and display (cannot add vehicle, add branch)
 */
public enum UserType {
  ADMIN,
  CUSTOMER
}
