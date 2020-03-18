import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.shippy.app.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import io.netty.buffer.ByteBuf;



public class ProductShippingEligibility {

  private static final String MININUM_PRICE_KEY = "MINIMUM_PRICE";

  @Autowired
  private RedisTemplate<String, String> redis;


  public Boolean isElisibleForFreeShiping(Product product) {
    return true;
  }

  public void updateValidCategories() {
    // TODO:
  }

  public Double updateMinimumPriceThreshold (Double price) {
    ByteBuffer buffer = ByteBuffer.allocate(price.BYTES);
    buffer.putDouble(price);
    redis.restore(MININUM_PRICE_KEY, buffer.array(), 0, TimeUnit.NANOSECONDS, true);
    return price;
  }

}