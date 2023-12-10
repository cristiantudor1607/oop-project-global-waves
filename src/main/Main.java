package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.input.LibraryInput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.parser.file.JsonLoader;
import globalwaves.player.entities.library.ActionManager;
import globalwaves.player.entities.library.Library;
import globalwaves.player.entities.utilities.FiltersMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);


        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePathInput for input file
     * @param filePathOutput for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePathInput,
                              final String filePathOutput) throws IOException {
        System.out.println("Test: " + filePathInput);
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class);
        ArrayNode outputs = objectMapper.createArrayNode();

        Library database = Library.getInstance();
        database.loadLibraryData(library);

        ActionManager manager = ActionManager.getInstance();
        List<CommandObject> commandList = new ArrayList<>();

        JsonLoader jsonInput = new JsonLoader(filePathInput);
        for (JsonNode node : jsonInput.getInputContent()) {
            CommandObject newCommand = objectMapper.treeToValue(node, CommandObject.class);
            if (newCommand.hasFiltersField()) {
                JsonNode filtersNode = node.get("filters");
                newCommand.setFiltersField(FiltersMapper.convertToMap(filtersNode));
            }

            commandList.add(newCommand);
        }

        for (CommandObject c : commandList) {
            manager.updatePlayersData(c);
            c.execute();
            JsonNode output = c.formatOutput();
            outputs.add(output);
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePathOutput), outputs);

        ActionManager.deleteInstance();
        Library.deleteInstance();
    }
}
