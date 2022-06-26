package util;

import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor
public class Verify {

  public static <T> void verifyNull(T reference, String errorMessage) throws Exception {
    if(null == reference){
      throw new Exception(errorMessage);
    }
  }

  public static void verify(String input, String errorMessage) throws Exception {
    if(StringUtils.isEmpty(input)){
      throw new Exception(errorMessage);
    }
  }

  public static void verifyCondition(boolean result, String errorMessage) throws Exception{
    if(result){
      throw new Exception(errorMessage);
    }
  }
}
