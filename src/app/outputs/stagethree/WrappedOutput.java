package app.outputs.stagethree;

import app.parser.commands.templates.CommandOutputFormatter;

import java.util.List;
import java.util.Map;

public class WrappedOutput extends CommandOutputFormatter {
    private List<Map.Entry<String, List<Map.Entry<String, Integer>>>> result;
}
