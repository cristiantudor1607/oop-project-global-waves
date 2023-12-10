package globalwaves.parser.file;

import checker.CheckerConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

@Getter @Setter
public class JsonLoader {
    private final JsonNode inputContent;
    private final int inputsCount;

    public JsonLoader(final String filename) throws IOException {
        ObjectMapper objMapper = new ObjectMapper();
        inputContent = objMapper.readTree(new File(CheckerConstants.TESTS_PATH + filename));
        inputsCount = inputContent.size();
    }
}
