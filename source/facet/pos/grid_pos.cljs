(ns facet.pos.grid-pos
  (:require [facet.pos.pos :as pos]))

;; PUBLIC

(defn create
  "Takes a position in grid units (unscaled) in the form {:x x :y y}. If nil, assumes 0 0."
  [state]
  (or state {:x 0 :y 0}))

(defn render
  [world state]
  (let [pitch 30]
    (pos/render world {:x (* pitch (:x state))
                       :y (* pitch (:y state))})))
