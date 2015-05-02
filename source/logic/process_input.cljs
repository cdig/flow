(ns logic.process-input)

(defn- safe-print [v] (print v) v)

(defn- process-keyboard-state
  "If the given event is a keyboard event, set some input state accordingly."
  [world [event-type event-data]]
  (or
    (case event-type
      :key-down (case event-data
                      #{:space}     (assoc world :mode :navigating)
                      #{:c}         (assoc world :mode :drawing)
                      #{:z}         (assoc world :action :undo)
                      #{:shift :z}  (assoc world :action :redo)
                      nil)
      :key-up (assoc world :mode nil)
      nil)
    world))

;; PUBLIC

(defn tick
  [world event]
  (-> world
      (process-keyboard-state event)))
