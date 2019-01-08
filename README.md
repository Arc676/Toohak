# Toohak
Project Toohak: a free, open-source, socket-based LAN version of [Kahoot! (the online quiz game)](https://kahoot.com/). This project is in no way affiliated or endorsed by Kahoot!. Toohak differs from Kahoot! in the following major ways (note that this may not be an exhaustive list):

- Kahoot! works over the internet and is accessed via a website. Toohak is a socket-based Java application that is designed to primarily work over local area networks (LAN) such as a home or school network. Virtual LANs used as peer-to-peer networks can also be used.
- Toohak is a standalone application and must be downloaded on the machines to be used. It also requires Java to be installed. Kahoot! does not require users to download anything.
- Kahoot! is available on mobile devices. Toohak, currently, is not.
- Quizzes created with Toohak are saved locally as files. Kahoot! saves user quizzes on their servers.
- The features of the two platforms differ.

### Scoring (subject to change)

Players earn more points by answering faster than their opponents. To be exact, every time a player answers _correctly_, the points obtained by the next player to answer _correctly_ will be 90% of those points. Higher amounts increase the stakes, but are also more tolerant because slower players get more points. The first person to correctly answer a 1000 point question will receive 1000 points. The second player to do so receives 900. The third receives 810. The fourth receives 729, and so on.

### Quiz editing

Quizzes can be created using the quiz editor. Here, you can specify the quiz name and enter question details. Enter the question, the time limit, and the maximum number of points obtainable from answering that question. The recommended score for a question is 1000 points.

You may optionally select an image to embed in the question. The image data is embedded into the quiz file. The image will appear in the quiz even if modified or removed from your computer. This also means that you may send quiz files to other people without sending the relevant images. Note that this will increase the size of the quiz file. It's also worth noting that this means that removing an image from a quiz requires editing that quiz in the quiz editor.

You must provide at least 2 answers to each question, at least one of which must be correct. The answers may be placed in any of the boxes, even if the alphabetically preceding box is empty. Only non-empty answers can be selected in-game. The correct answer(s) must be among the non-empty answers.

Click "Add Question" to add the question to the end of the quiz. Select a question in the table and press "Edit Question" to edit it. This will **remove** that question from the quiz and load its contents into the quiz editor interface. Click "Add Question" to add it back after making any necessary modifications. Note that this will move the question to the end of the quiz.

### Server mode

When you click "Host game" you will be asked to select a quiz file. The IP address shown should be your address on the local network. If your device has more than one network card or interface, this might not be correct. If people are having trouble connecting, please check your network settings in case the shown address is incorrect.

You may kick users from the game before the quiz begins, but not during (might change in the future).

The button on the right adapts to match the current game state.
- While waiting for players, clicking it begins the game. No new players may join.
- While waiting for answers, it skips the remaining time and ends the question. Players who have already answered still get points if they answered correctly. The leaderboard is updated to show the new rankings. The center view switches to show information about what answers were chosen. The length of each bar is determined by the relative popularity of the correponding answer. The most popular answer is always maximum length.
- After the end of a question, it loads the next question. The center view switches back to the leaderboard.
- If there are no more questions, the button shuts down the server.

### Client mode

Enter the IP address of the game host and a username/nickname. Click "Connect" to join the game. When a question is received from the game host, the question, relevant image, and potential answers are presented. Click an answer to submit.

When time runs out or all players have answered, you will be given the following information:
- Whether your answer was correct
- All acceptable answers
- Your ranking
- If you aren't in first place, the difference between your score and that of the person ahead of you

When the game ends, click "Back to Main" to exit the client and return to the main menu.

### Legal

Toohak source code available under GPLv3. See LICENSE for full GPL text. See the comments at the beginning of each source file for details regarding the license of that particular file.

Commons CSV library available under the Apache 2.0 license. See 'APACHE LICENSE.txt' for full license text.

Based on work by Matthew Chen and Arc676/Alessandro Vinciguerra available under the MIT License.

Music available under CC0.

### Screenshots

![Main Menu](https://arc676.github.io/img/toohak/main.jpg)

![Connected Client](https://arc676.github.io/img/toohak/client.jpg)

![Server View](https://arc676.github.io/img/toohak/server.jpg)

![Quiz Editor](https://arc676.github.io/img/toohak/qEdit.jpg)
