(ns hangman.core)
(require 'hangman.data.words)
(refer 'hangman.data.words)

(require 'hangman.views.views)
(refer 'hangman.views.views)

(use '[clojure.string :only (join split trim includes?)])

(defn get-valid-word
  "Gets a valid word from word list."
  [words]
  (let [word (rand-nth words)]
    (if (or 
         (includes? word "-")
         (includes? word " "))
      (get-valid-word words)
      word)))

(defn get-current-word
  "Takes a word and replaces letters not found in used_letters set with a underscore character."
  [word used_letters]
  (join (map 
         (fn [c] (if (contains? used_letters c) c "_") ) 
         (split word #""))))

(defn get-input
  "Gets a letter from user."
  []
  (println "Guess a letter:")
  (trim (read-line)))

(defn correct-guessing?
  [word_letters letter]
  (if (contains? word_letters letter) 0 1))

(defn hangman
  "Prints current word, lives left and used letters. Valids ame over or asks for guessing a new letter. "
  [word word_letters lives used_letters]
  (println (get views lives))
  (println (str "Current word: " (get-current-word word used_letters) ))
  (println (str "You have " lives " lives left and you have used these letters: " (join " " used_letters)))
  (if (and (> lives 0) (> (count word_letters) 0))    
    (let [letter (get-input)]
      (hangman word (disj word_letters letter) (- lives (correct-guessing? word_letters letter)) (conj used_letters letter)))
    (if (= lives 0)
      (println (str "You died, sorry. The word was: " word))
      (println "YAY! You guessed the word!"))))

(defn play
  "Starts a new game."
  []
  (let [word (get-valid-word words)]
    (println word)
    (hangman word (set (split word #"")) 7 #{})))

(defn -main
  [& args]
  (println "Get ready to play hangman!")
  (play))


