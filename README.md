# CS 2212 Final Software Project: Navigating the Fiscal Frontier
*Authors:* Earl Castillo (ecastil3), Karmen Minhas (kminhas7), Joelene Hales (jhales5), Franck Limtung (flimtung), 
Kevin Chen (kchen546)

Welcome to Fiscal Frontier, a financial education game developed by Group 73 as our final project for CS 2212.

Demo Video: https://youtu.be/cwU87hQf9q0

### Overview 🌍
The Fiscal Frontier is designed to take players on a journey to explore and enhance their personal finance skills, practice 
financial decision-making, while also exposing them to a simulated stock market. The gameplay is reminiscent of Mario 
Party, where players take turns navigating the game board, earning coins, and striving to earn money to purchase Stars. 
The game features two modes: normal and hard. In hard mode, players face more severe challenges, including higher costs 
for Stars and harder mini-games.


### Player Information 👤
Players have profiles that store key information necessary for navigating the game. This includes the player's current 
position on the game board, dice roll results, paths reachable from the player's current position, financial metrics 
such as money and investment accounts, stocks owned, score, item inventory, frozen state, and more. The player profile 
also includes information that remains constant regardless of the game state, such as knowledge level, lifetime score, 
maximum score achieved, and knowledge base.


### Instructor Dashboard 📝
Instructors can monitor their students' progress using the Instructor Dashboard. This dashboard displays information 
about students' levels, high scores, lifetime scores, and the number of educational tips unlocked. Instructors can 
manage student profiles, including adding new profiles, editing existing ones, and removing profiles.
The Instructor dashboard password is "CS2212".

### High Score Table 🏆
The High Score Table automatically updates after each game, tracking the top five scores in two categories: highest 
individual game score and highest cumulative lifetime score.


### Setting Up and Continuing a Game 🚦
Players can set up a new game by selecting players from a dropdown menu. The game supports up to five players and 
offers a checkbox to enable hard mode. The "Continue" option loads the most recent save based on modification time.


### Gameplay and Nodes 🎲
The game board consists of various tiles, including star tiles, penalty tiles, global penalty tiles, and agility test 
game tiles. Players navigate the board by rolling dice and landing on tiles, where they can purchase stars, face 
penalties, or engage in mini-games. Players can use items such as shields, bikes, multi-dice, and books to protect 
themselves or gain advantages. The game also features permanent nodes and randomly generated stars.


### Shop and Stocks 🛒
The shop allows players to purchase items and stocks using their cash and investment accounts. Stocks range from safe to 
volatile, offering different levels of risk and potential reward. Dividend stocks pay players either every round or 
every turn, depending on their volatility.


### Pause Menu and Debug Mode 🔍
The pause menu enables players to save or load games, view their knowledge catalog, or access the shop. The debug mode, 
activated with a hidden button and password, allows for modifying player stats, changing tile types, skipping rounds, 
and accessing debug options during gameplay.
The debug mode can be activated by pressing a partially transparent button in the lower right corner of the screen, and entering the password "noclip".

### End Screen and High Score Update 
After completing a game, players are shown an end screen listing their scores and achievements. All saves linked to that 
game are automatically deleted upon returning to the main menu. The high score screen updates automatically based on 
game outcomes.


### Agility Test 🏃
The agility test mini-game challenges players to answer math questions quickly, with increasing difficulty based on 
correct answers and speed bonuses for quick responses.


### Conclusion
Navigating the Fiscal Frontier aims to provide an engaging and educational experience for players to improve their 
financial literacy  skills, aiming to bridge the gap in personal finance education for players. We hope that our game 
equips you with the knowledge and skills to make smart financial decisions. :)


# How to run:
- After downloading the self extracting archive, extract the primary game folder to anywhere you choose.
- Ensure the `external` and `saves` directory have been extracted properly.
- Then click on "Edit Configurations" on the top right in Intellij. 
- Add a new Gradle configuration from the + in the top left. 
- under the Run field type "run" and click okay. 
- Click on the green Play Icon in the top right corner to run the game.
- To run tests, add a new Gradle configuration and type "test".
### Source code information:
- The Javadoc is located in the `JavaDocOutput` folder
- Source files are located in `core/src/`
- Test source files are located in `core/test`

### Libraries used:
- LibGDX Version 1.12.1 https://libgdx.com/
- Raeleus Stripe 1.4.5 https://github.com/raeleus/stripe
- Mockito 4.2.0 https://site.mockito.org/
