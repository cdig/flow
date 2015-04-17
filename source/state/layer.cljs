;; Side Effects √
;; State X
;; Deps X

(ns state.layer
  (:require [graphics.surface :as surface]))

(defn create! [] "Creates a new layer"
  {:surface (surface/create!)
   :entities []})

(defn add-entity [surface entity] "Add a new entity to the layer"
  (update-in surface [:entities] conj entity))
