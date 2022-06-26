package util.generator;

import java.util.concurrent.atomic.AtomicInteger;

public class VehicleIdGenerator implements IdGenerator{

  private static AtomicInteger atomicInteger = new AtomicInteger(0);
  private static final String VEHICLE_PREFIX = "V";

  public String generateUniqueId() {
    return VEHICLE_PREFIX + String.valueOf(atomicInteger.incrementAndGet());
  }
}
