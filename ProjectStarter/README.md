# My Personal Project

## Proposal
- This project is a local tool for managing COC (Call of Cthulhu) character sheets. 

- The application will allows users to **creat, viewing, edit, delete** their character sheets. And auto-calculate derived data *(HP, MP, SAN )*.

-  The main users of this application should be players of the COC (Call of Cthulhu) game. 

- Managing character sheets after a player has participated in multiple games can be quite difficult. This application should help them manage their character sheets.

## User stories
- As a user, I want to add/delete a character to my library by name, occupation, age, attributes (include 9 types data), skills.

- As a user, I want to view a list of all characters with key info.

- As a user, I want to edit a character’s attributes and have derived stats auto-recalculated.

- As a user, I want to add/edit/delete skills for one character.

- As a user, I want to be able to save my current character List to file (if I so choose)

- As a user, I want to be able to be able to load my previous character List from file (if I so choose)

## Instructions for End User

- You can view the panel that displays the Characters that have already been added to the Library by choose it in the main tble and click the view button.

- You can generate the first required action related to the user story "adding multiple Characters to a Library" by click the add button.

- You can generate the second required action related to the user story "adding multiple Skills to a Character" by click the edit button.

- You can locate my visual component by click the view button.

- You can save the state of my application by click the save button. 

- You can reload the state of my application by click the load button on the beginning of program.

## Phase 4: Task 2
- Add EventLog, example: 

    Sun Nov 23 15:07:30 PST 2025
    Change character name: Alex->Alex

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex occupation: officer->officer

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex gender: other->other

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex gender: other->other

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex age: 28->28

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute STR: 38->38

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute DEX: 40->40

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute CON: 35->35

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute APP: 45->45

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute POW: 68->68

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute SIZ: 55->55

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute INT: 44->44

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute EDU: 58->58

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute LUC: 66->66

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute HP: 9->9

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute MP: 13->13

    Sun Nov 23 15:07:30 PST 2025
    Change character Alex Attribute SAN: 99->99

## Phase 4: Task 3

- I think my UI is overly cluttered; given more time, I would separate the GUI and console UI in different foldor. Furthermore, my UI components frequently call object within the model. I might establish an intermediary class to reduce cohesion throughout the project.


