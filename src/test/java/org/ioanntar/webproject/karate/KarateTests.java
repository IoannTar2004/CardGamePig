package org.ioanntar.webproject.karate;

import com.intuit.karate.junit5.Karate;

public class KarateTests {

    @Karate.Test
    Karate runKarateTests() {
        return Karate.run(
//                "classpath:features/SignIn.feature",
//                "classpath:features/TearDown.feature"
                "classpath:features/Game.feature"
                );
    }
}
