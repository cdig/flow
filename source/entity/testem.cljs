(ns entity.testem
  (:require [entity.entity :as entity]
            [core.color :refer [random-color]]))

(defn- xpos [i]
  (-> i
      (mod 50)
      (* 30)
      (+ 15)))

(defn- ypos [i]
  (-> i
      (quot 50)
      (* 30)
      (+ 15)))

(defn create-entity
  [world i]
  (entity/create
    world
    {:pos {:x (xpos i) :y (ypos i)}
     :geo {:type :circle :r 8}
     :dye {:fill (random-color)
           :stroke "rgb(40, 40, 40)"}}))

(defn setup [world]
  (loop [i (dec 100) world world]
    (if (neg? i)
        world
        (recur (dec i) (create-entity world i)))))
