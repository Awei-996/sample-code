package online.k12code.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解决ABA问题
 * @author Carl
 * @date 2023/9/22
 **/
public class AtomicStampedReferenceTest {

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Student {
    String userName;
    int age;
}
