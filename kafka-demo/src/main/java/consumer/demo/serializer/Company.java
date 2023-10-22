package consumer.demo.serializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LR
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private String address;
}
