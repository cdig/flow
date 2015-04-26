(ns facet.dye.random-fill
  (:require [core.color :refer [random-color]]))

;; PUBLIC

(defn create
  [_]
  {:fill (random-color)})

(defn render
  [state]
  state)
