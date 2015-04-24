(ns app.undo)
  
(defonce undo-stack (atom []))
(defonce redo-stack (atom []))

(defn undo!
  "Adds the most recent undo state to the redo stack. Pops the most recent state off the undo stack. Returns the new most recent undo state."
  []
  ;; > 1 becsuse we don't want to undo the initial state.
  (when (> (count @undo-stack) 1)
    (swap! redo-stack conj (last @undo-stack))
    (swap! undo-stack pop)
    (last @undo-stack)))

(defn redo!
  "Adds the most recent redo state to the undo stack. Pops the most recent state off the redo stack. Returns the new most recent undo state, or nil if there are no more redo states."
  []
  (when (seq @redo-stack)
    (swap! undo-stack conj (last @redo-stack))
    (swap! redo-stack pop)
    (last @undo-stack)))

(defn save!
  "Save a new state into the undo stack, but only if it's different than the previous state. Destroys the redo stack. Returns nil."
  [state]
  (when (not= state (last @undo-stack))
        (reset! redo-stack [])
        (swap! undo-stack conj state))
  nil)
