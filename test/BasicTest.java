import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new User("bob@gmail.com", "secret", "Bob").save();

        // Retrieve the user with email address bob@gmail.com
        User bob = User.find("byEmail", "bob@gmail.com").one();

        // Test
        assertNotNull(bob);
        assertEquals("Bob", bob.fullname);
    }

}
