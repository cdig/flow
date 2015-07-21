(ns logic.scale-grid
  (:require [core.math :as math]))

(defn tick
  [world [event-type event-data]]
  world)
  ; (or
  ;   (when (and (= (:mode world) :scaling)
  ;              (= event-type :mouse-drag))
  ;     (grid/set-pitch world (math/pow (grid/get-pitch world)
  ;      (- 1
  ;         (* 0.001
  ;            (get-in event-data [:rel :y]))))))
  ;   world))
