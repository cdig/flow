(ns logic.window-size)

(defn tick
  [world [event-type event-data]]
  (if (= event-type :resize)
    (assoc world :window-size event-data)
    world))
