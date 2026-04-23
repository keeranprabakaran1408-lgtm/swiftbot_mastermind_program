# swiftbot_mastermind_program
A Mastermind code-breaking game I built in Java for my first year at Brunel University. It uses a SwiftBot robot as the game opponent — the robot's camera scans physical colour cards as your guesses.

## What it does

- Two game modes — Default (4 colours, 6 attempts) and Customised (you pick the code length and number of attempts)
- The SwiftBot camera captures an image of each colour card, extracts the centre pixels, computes the average RGB value and classifies it as one of 6 colours (R, G, B, Y, O, P)
- Gives feedback after each guess with + (right colour, right position) and - (right colour, wrong position)
- If a card can't be recognised it flashes red and asks you to rescan — green flash means it worked
- Hint system — press B to reveal one colour from the secret code
- Win celebration using SwiftBot underlights
- Calibration mode to test colour scanning before playing
- Logs every round to a text file including the secret code, each guess, feedback and the score

## Built with

- Java
- SwiftBot API
- Java AWT (image processing)

## How to run

This project requires a SwiftBot robot and the SwiftBot Java environment from Brunel University — it can't be run without the hardware.

To run it on a SwiftBot:
1. Clone the repo into your SwiftBot Java project
2. Make sure the SwiftBot API is on your classpath
3. Run `task1.java`

## Files

- `task1.java` — the full game including all logic, image processing, scanning, logging and SwiftBot control
## Videos will be attached after examination
