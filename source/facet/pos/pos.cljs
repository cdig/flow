(ns facet.pos.pos
  (:require [gui.viewport :as viewport]))

(defn create
  [state]
  state)

(defn render
  [world state]
  (merge-with + state (viewport/get-pos world)))
