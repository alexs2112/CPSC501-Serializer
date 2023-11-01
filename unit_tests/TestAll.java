package unit_tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    TestMap.class,
    TestSerializer.class,
    TestNetwork.class,
    TestDeserializer.class
})
public class TestAll { }
