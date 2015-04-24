;; Look at the raw events, and process them into instructions for what the GUI should do.

(ns logic.inputs)

(defn- determine-intent
  [world [event-type event-data]]
  (case event-type
    
    :key-down
      (assoc world :mode (case (first event-data)
        :c :drawing
        :space :navigating
        nil))
        
    :key-up
      (assoc world :mode nil)
    
    nil))

;; PUBLIC

(defn act
  [world event]
  (or (determine-intent world event)
      world))
