;; Look at the raw events, and process them into instructions for what the system should do.

(ns logic.inputs)

(defn- clear-input-action
  "Actions only last 1 tick."
  [world]
  (assoc world :action nil))

(defn- process-keyboard-state
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

(defn act
  [world event]
  (-> world
      (clear-input-action)
      (process-keyboard-state event)))
