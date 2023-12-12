package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.AddEventExit;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.HelperTool;
import globalwaves.player.entities.utilities.DateMapper;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Getter @Setter
public class AddEventInterrogator extends CommandObject {
    private String name;
    private String description;
    private String date;

    @JsonIgnore
    private AddEventExit.Status exitStatus;

    @Override
    public void execute() {

    }

    @Override
    public JsonNode formatOutput() {
        return null;
    }
}
