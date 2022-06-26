package util.generator;

import java.util.concurrent.atomic.AtomicInteger;

public class BranchIdGenerator implements IdGenerator{

  private static AtomicInteger atomicInteger = new AtomicInteger(0);
  private static final String BRANCH_PREFIX = "B";

  public String generateUniqueId() {
    return BRANCH_PREFIX + String.valueOf(atomicInteger.incrementAndGet());
  }
}
