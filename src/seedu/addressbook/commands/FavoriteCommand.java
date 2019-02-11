package seedu.addressbook.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;


/**
 * Finds and adds all persons in address book whose name contains any of the argument keywords to a favorites list.
 * Keyword matching is case sensitive.
 */
public class FavoriteCommand extends Command {

    public static final String COMMAND_WORD = "favorite";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain\n"
            + "any of the specified \"\n"
            + "keywords (case-sensitive) and adds them to a list of favorite contacts.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD
            + " John Alice Bob";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the favorite list";
    public static final String MESSAGE_COMPLETE = "Adding to favorite list completed.";


    private final Set<String> keywords;

    public FavoriteCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns a copy of keywords in this command.
     */
    public Set<String> getKeywords() {
        return new HashSet<>(keywords);
    }

    @Override
    public CommandResult execute() {
        final List<ReadOnlyPerson> personsFound = getPersonsWithNameContainingAnyKeyword(keywords);
        for(ReadOnlyPerson person : personsFound) {
            try {
                addressBook.addFavorite(person);
                return new CommandResult(String.format(MESSAGE_SUCCESS, person));
            } catch (UniquePersonList.DuplicatePersonException dpe) {
                return new CommandResult(MESSAGE_DUPLICATE_PERSON);
            }
        }
        return new CommandResult(MESSAGE_COMPLETE);
    }

    /**
     * Retrieves all persons in the address book whose names contain some of the specified keywords.
     *
     * @param keywords for searching
     * @return list of persons found
     */
    private List<ReadOnlyPerson> getPersonsWithNameContainingAnyKeyword(Set<String> keywords) {
        final List<ReadOnlyPerson> matchedPersons = new ArrayList<>();
        for (ReadOnlyPerson person : addressBook.getAllPersons()) {
            final Set<String> wordsInName = new HashSet<>(person.getName().getWordsInName());
            if (!Collections.disjoint(wordsInName, keywords)) {
                matchedPersons.add(person);
            }
        }
        return matchedPersons;
    }

}
