(ns logic.scale-grid
  (:require [gui.grid :as grid]
            [core.math :as math]))

(defn tick
  [world [event-type event-data]]
  (or
    (when (and (= (:mode world) :scaling)
               (= event-type :mouse-drag))
      (grid/set-pitch world (math/pow (grid/get-pitch world)
       (- 1
          (* 0.001
             (get-in event-data [:rel :y]))))))
    world))
