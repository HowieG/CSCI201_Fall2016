The Welcome Screen calls GameData gameData = new GameData("data/" + file.getName()) so any test files should be placed in the data folder

The Welcome Screen suddenly started giving error for the file for working files.

Haven't adapted GamePlay's answerQuestion(...) yet, right now every time you hit Submit Answer it's calling answerQuestion(...) as if it's the first time, thereby making you stuck in the game

I commented out the answer checking in the submit button to test the final jeopardy window. Please uncomment to test!