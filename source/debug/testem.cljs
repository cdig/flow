(ns debug.testem
  (:require [entity.entity :as entity]
            [core.color :refer [random-color]]))

(defn create-entity
  [world i]
  (entity/create world {:grid-pos [(mod i 50) (quot i 50)]
                       :circle 8
                       :random-fill nil
                       :stroke-rgb 40}))

(defn setup [world]
  (loop [i (dec 10) world world]
    (if (neg? i)
        world
        (recur (dec i) (create-entity world i)))))
