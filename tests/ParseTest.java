import models.Measurement;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class ParseTest {

    @Test
    void parseFromFileTest() {
        List<Measurement> measurements = Parser.parseFromFile(new File("tests/output.xml"));
        System.out.println(measurements);
    }

}
