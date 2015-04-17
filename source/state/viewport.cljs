;; Side Effects √
;; State √
;; Deps X

(ns state.viewport)

(defonce state (atom {:x 0
                     :y 0
                     :zoom 1}))

(defn view [] "Returns the current view state"
  @state)

(defn move! [x y] "Sets the current x/y position"
  (reset! state (assoc @state :x x :y y)))

(defn zoom! [z] "Sets the current zoom"
  (reset! state (assoc @state :zoom z)))
