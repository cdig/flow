(ns debug.testem
  (:require [app.ider :as ider]
            [entity.entity :as entity]
            [core.color :refer [random-color]]))

;; Test circles

(defn- create-entity
  [world i]
  (entity/create world {:grid-pos {:x (mod i 50) :y (quot i 50)}
                        :circle 8
                        :random-fill nil
                        :stroke-rgb 40}))

(defn- test-circles
  [world]
  (loop [i (dec 1) world world]
    (if (neg? i)
        world
        (recur (dec i) (create-entity world i)))))

;; PUBLIC

(defn setup
  [world]
  (-> world
      test-circles))
