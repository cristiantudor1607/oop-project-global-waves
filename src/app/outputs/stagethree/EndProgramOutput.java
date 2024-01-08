package app.outputs.stagethree;

import app.parser.commands.templates.CommandOutputFormatter;
import app.monetization.MonetizationStat;
import app.monetization.MonetizationStatisticsFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.Map;

@Getter
public class EndProgramOutput extends CommandOutputFormatter {
    private final Map<String, MonetizationStat> result;

    public EndProgramOutput() {
        command = "endProgram";
        MonetizationStatisticsFormatter formatter = new MonetizationStatisticsFormatter();
        result = formatter.getMonetizationStatistics();
    }

    /**
     * Method that generates the output JsonNode, based on the current instance of the
     * class.
     * @return The output formatted as JsonNode
     */
    @Override
    public JsonNode generateOutputNode() {
        ObjectMapper formatter = new ObjectMapper();
        ObjectNode node =  formatter.valueToTree(this);
        node.remove("user");
        node.remove("timestamp");

        return node;
    }
}
