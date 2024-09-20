<template>
  <div id="app">
    <h1>找到HuaFu饼</h1>
    <h3>"It's like Wordle, but not fun." -Julius Caesar</h3>
    
    <div id="game-board">
      <div v-for="(row, rowIndex) in board" :key="rowIndex" class="letter-row">
        <div v-for="(letter, letterIndex) in row" :key="letterIndex" 
             class="letter-box" :class="{ 'filled-box': letter }">
          {{ letter }}
        </div>
      </div>
    </div>

    <div id="keyboard-cont">
      <div v-for="(row, rowIndex) in keyboard" :key="rowIndex" :class="getRowClass(rowIndex)">
        <button v-for="key in row" :key="key" 
                @click="handleKeyPress(key)" 
                class="keyboard-button" 
                :style="{ backgroundColor: getKeyColor(key) }">
          {{ key }}
        </button>
      </div>
    </div>

    <div id='download-button'>
      <a href="../js/app.js" download>Download</a>
    </div>

  </div>
</template>

<script>
import { WORDS } from '@/assets/waffle/words.js'

export default {
  name: 'App',
  data() {
    return {
      NUMBER_OF_GUESSES: 6,
      WORD_LENGTH: 21,
      CORRECT_GUESS: 155,
      guessesRemaining: 6,
      currentGuess: [],
      nextLetter: 0,
      rightGuessString: WORDS[155],
      board: [],
      keyboard: [
        ['q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'],
        ['a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'],
        ['Del', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'Enter']
      ],
      keyColors: {}
    }
  },
  created() {
    this.initBoard()
    window.addEventListener('keyup', this.handleKeyPress)
  },
  methods: {
    initBoard() {
      for (let i = 0; i < this.NUMBER_OF_GUESSES; i++) {
        this.board.push(new Array(this.WORD_LENGTH).fill(''))
      }
    },
    getRowClass(index) {
      return index === 1 ? 'second-row' : index === 2 ? 'third-row' : 'first-row'
    },
    getKeyColor(key) {
      return this.keyColors[key] || ''
    },
    handleKeyPress(key) {
      if (typeof key === 'object') {
        key = key.key
      }
      
      if (this.guessesRemaining === 0) {
        return
      }

      key = key.toLowerCase()

      if (key === 'backspace' || key === 'del') {
        this.deleteLetter()
      } else if (key === 'enter') {
        this.checkGuess()
      } else if (key.match(/^[a-z]$/)) {
        this.insertLetter(key)
      }
    },
    insertLetter(key) {
      if (this.nextLetter === this.WORD_LENGTH) {
        return
      }
      this.board[this.NUMBER_OF_GUESSES - this.guessesRemaining][this.nextLetter] = key
      this.currentGuess.push(key)
      this.nextLetter++
    },
    deleteLetter() {
      if (this.nextLetter === 0) {
        return
      }
      this.nextLetter--
      this.board[this.NUMBER_OF_GUESSES - this.guessesRemaining][this.nextLetter] = ''
      this.currentGuess.pop()
    },
    checkGuess() {
      if (this.currentGuess.length !== this.WORD_LENGTH) {
        alert("Not enough letters!")
        return
      }

      let guessString = this.currentGuess.join('')
      
      if (!WORDS.includes(guessString)) {
        alert("Word not in list!")
        return
      }

      for (let i = 0; i < this.WORD_LENGTH; i++) {
        let letterColor = ''
        let letter = this.currentGuess[i]
        
        if (letter === this.rightGuessString[i]) {
          letterColor = 'green'
        } else if (this.rightGuessString.includes(letter)) {
          letterColor = 'yellow'
        } else {
          letterColor = 'grey'
        }

        this.keyColors[letter] = letterColor
      }

      if (guessString === this.rightGuessString) {
        alert('You guessed right! The flag is ' + this.rightGuessString)
        this.guessesRemaining = 0
      } else {
        this.guessesRemaining--
        this.currentGuess = []
        this.nextLetter = 0

        if (this.guessesRemaining === 0) {
          alert("You've run out of guesses! Game over!")
          alert('Try reverse engineering the code to discover the correct "word"!')
        }
      }
    }
  }
}
</script>

<style>
h1 {
  text-align: center;
}
h3 {
  text-align: center;
  font-style: italic;
  color: grey;
}

#game-board {
  display: flex;
  align-items: center;
  flex-direction: column;
}

.letter-box {
  border: 2px solid gray;
  border-radius: 3px;
  margin: 2px;
  font-size: 2.5rem;
  font-weight: 700;
  height: 3rem;
  width: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;
  text-transform: uppercase;
}

.filled-box {
  border: 2px solid black;
}

.letter-row {
  display: flex;
}

#keyboard-cont {
  margin: 1rem 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

#keyboard-cont div {
  display: flex;
}

.second-row {
  margin: 0.5rem 0;
}

.keyboard-button {
  font-size: 1rem;
  font-weight: 700;
  padding: 0.5rem;
  margin: 0 2px;
  cursor: pointer;
  text-transform: uppercase;
}
</style>
