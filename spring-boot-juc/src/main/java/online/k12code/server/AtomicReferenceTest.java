package online.k12code.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子引用
 * @author Carl
 * @date 2023/9/22
 **/
public class AtomicReferenceTest {
    public static void main(String[] args) {
        AtomicReference<User> userAtomicReference = new AtomicReference<>();
        User user1 = new User("zhangsan", 22);
        User user2 = new User("lisi", 23);
        userAtomicReference.set(user1);
        System.out.println(userAtomicReference.compareAndSet(user1, user2) + "\t" + userAtomicReference.get().toString());
        System.out.println(userAtomicReference.compareAndSet(user1, user2) + "\t" + userAtomicReference.get().toString());
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class User{
    String userName;
    int age;
}
